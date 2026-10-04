package com.gonzalovega.clientmanagement.services.ResourceService;

import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.repository.ClientResourceRepository;
import com.gonzalovega.clientmanagement.specifications.ResourceSpecifications;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ResourceAssembler;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceResponseDto;
import com.gonzalovega.clientmanagement.models.ResourceModel;
import com.gonzalovega.clientmanagement.repository.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements IResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceAssembler resourceAssembler;
    private final ClientResourceRepository clientResourceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponseDto> getAllResources() {
        log.info("getAllResources - start");

        List<ResourceResponseDto> resources = resourceRepository.findByActiveTrue()
                .stream()
                .map(resourceAssembler::toResponse)
                .toList();

        log.info("getAllResources - end (returnedElements={})", resources.size());
        return resources;
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceResponseDto getResourceById(Integer id) {

        log.info("Fetching active resource by id from the database");
        ResourceModel resource = resourceRepository.findById(id)
                .filter(ResourceModel::isActive)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));
        return resourceAssembler.toResponse(resource);
    }

    @Override
    @Transactional
    public ResourceResponseDto createResource(ResourceRequestDto dto) {

        log.info("Creating new resource from the database with name: {}", dto.getName());
        ResourceModel resource = resourceAssembler.toModel(dto);
        ResourceModel resourceModel = resourceRepository.save(resource);

        log.info("Successfully created resource with name: {}", resourceModel.getName());
        return resourceAssembler.toResponse(resourceModel);
    }

    @Override
    @Transactional
    public ResourceResponseDto updateResource(Integer id, ResourceRequestDto dto) {

        log.info("Updating Resource with id: {}", id);
        ResourceModel existingResource =  resourceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.error("Resource with id {} not found for update", id);
                    return new RuntimeException("Resource not found with id: " + id);
                });

        if(!existingResource.getId().equals(dto.getId())) {
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!existingResource.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, existingResource.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }

        resourceAssembler.updateModel(dto, existingResource);

        ResourceModel updatedResource = resourceRepository.save(existingResource);
        log.info("Successfully updated TypeConnection with and name: {}", updatedResource.getName());
        return resourceAssembler.toResponse(updatedResource);
    }

    @Override
    @Transactional
    public void deleteResource(Integer id) {

        if (clientResourceRepository.existsByResourceId_IdAndActiveTrue(id)) {
        log.warn("Attempt to delete Resource ID {} failed: assigned to active ClientResources", id);
        throw new RuntimeException("Resource is assigned already and can't be disabled: " + id);
    }
        ResourceModel entity = resourceRepository.findById(id)
                .filter(ResourceModel::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Active resource not found: " + id));


        entity.setActive(false);
        resourceRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResourceResponseDto> searchResources(ResourceSearchRequestDto filter, int page, int size) {
        log.info("searchResources - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ResourceResponseDto> results = resourceRepository
                .findAll(ResourceSpecifications.byFilter(filter), pageable)
                .map(resourceAssembler::toResponse);

        log.info("searchResources - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}
