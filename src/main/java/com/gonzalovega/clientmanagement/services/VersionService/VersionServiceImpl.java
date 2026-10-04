package com.gonzalovega.clientmanagement.services.VersionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.VersionAssembler;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionRequestDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionResponseDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionSearchRequestDto;
import com.gonzalovega.clientmanagement.models.VersionModel;
import com.gonzalovega.clientmanagement.repository.ClientResourceRepository;
import com.gonzalovega.clientmanagement.repository.VersionRepository;
import com.gonzalovega.clientmanagement.specifications.VersionSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VersionServiceImpl implements IVersionService {
    private final VersionRepository resourceVersionRepository;
    private final ClientResourceRepository clientResourceRepository;
    private final VersionAssembler resourceVersionAssembler;

    @Override
    @Transactional(readOnly = true)
    public List<VersionResponseDto> getAllVersions() {
        log.info("getAllVersions - start");

        List<VersionResponseDto> entities = resourceVersionRepository.findAllByActiveTrue()
                .stream()
                .sorted(Comparator.comparing(VersionModel::getLaunchDate).reversed()
                        .thenComparing(Comparator.comparing(VersionModel::getId).reversed()))
                .map(resourceVersionAssembler::toResponse)
                .toList();

        log.info("getAllVersions - end (returnedElements={})", entities.size());
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public VersionResponseDto getVersionById(Integer id) {

        log.info("Getting Version by id: {}", id);
        VersionModel entity = resourceVersionRepository.findById(id)
                .filter(VersionModel::getActive)
                .orElseThrow(() -> {
                    log.error("Version with id {} not found", id);
                    return new RuntimeException("Version not found with id: " + id);
                });
        log.info("Found Version entity: {} with data {}", id, entity.getVersion());
        return resourceVersionAssembler.toResponse(entity);
    }

    @Override
    @Transactional
    public VersionResponseDto createVersion(VersionRequestDto dto) {

        log.info("Creating a new Resource Version {}", dto.getVersion());
        VersionModel entity = resourceVersionAssembler.toModel(dto);
        VersionModel savedEntity = resourceVersionRepository.save(entity);
        log.info("Successfully created TypeConnection with name: {}", savedEntity.getVersion());
        return resourceVersionAssembler.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public VersionResponseDto updateVersion(Integer id, VersionRequestDto dto) {

        log.info("Updating Version with id: {}", id);
        VersionModel existingVersion = resourceVersionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.error("Resource Version with id {} not found for update", id);
                    return new RuntimeException("Resource Version not found with id: " + id);
                });

        if(!existingVersion.getId().equals(dto.getId())) {
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!existingVersion.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, existingVersion.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }

        resourceVersionAssembler.updateModel(dto, existingVersion);

        VersionModel updatedEntity = resourceVersionRepository.save(existingVersion);
        log.info("Updated Resource Version entity: {} with new version {}", id, dto.getVersion());
        return resourceVersionAssembler.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteVersion(Integer id) {

        log.info("Deleting Version with id: {}", id);
        VersionModel entity = resourceVersionRepository.findById(id)
                .filter(VersionModel::getActive)
                .orElseThrow(() -> {
                    log.error("Version with id {} not found for deletion", id);
                    return new RuntimeException("Version not found with id: " + id);
                });
        entity.setActive(false);
        resourceVersionRepository.save(entity);
        log.info("Resource Version with id {} marked as inactive (soft deleted)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VersionResponseDto> searchVersions(
            VersionSearchRequestDto filter,
            int page,
            int size) {

        log.info("searchVersions - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<VersionResponseDto> results = resourceVersionRepository
                .findAll(VersionSpecifications.byFilter(filter), pageable)
                .map(resourceVersionAssembler::toResponse);

        log.info("searchVersions - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}