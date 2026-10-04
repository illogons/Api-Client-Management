package com.gonzalovega.clientmanagement.services.TypeConnectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.TypeConnectionAssembler;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionSearchRequestDto;
import com.gonzalovega.clientmanagement.models.TypeConnectionModel;
import com.gonzalovega.clientmanagement.repository.ConnectionRepository;
import com.gonzalovega.clientmanagement.repository.TypeConnectionRepository;
import com.gonzalovega.clientmanagement.specifications.TypeConnectionSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TypeConnectionServiceImpl implements ITypeConnectionService {

    private final TypeConnectionRepository typeConnectionRepository;
    private final TypeConnectionAssembler typeConnectionAssembler;
    private final ConnectionRepository connectionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TypeConnectionResponseDto> getAllTypeConnections() {
        log.info("getAllTypeConnections - start");

        List<TypeConnectionResponseDto> typeConnections = typeConnectionRepository.findByActiveTrue()
                .stream()
                .map(typeConnectionAssembler::toResponse)
                .toList();

        log.info("getAllTypeConnections - end (returnedElements={})", typeConnections.size());
        return typeConnections;
    }

    @Override
    @Transactional(readOnly = true)
    public TypeConnectionResponseDto getTypeConnectionById(Integer id) {

        log.info("Attempting to fetch TypeConnection with ID: {}", id);
        TypeConnectionModel typeConnection = typeConnectionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
            log.error("Fetch failed: Type Connection with ID {} not found or inactive", id);
            return new RuntimeException("Type connection not found with ID: " + id);
        });

        log.info("Successfully retrieved TypeConnection: {}", typeConnection.getName());
        return typeConnectionAssembler.toResponse(typeConnection);
    }

    @Override
    @Transactional
    public TypeConnectionResponseDto createTypeConnection(TypeConnectionRequestDto dto) {

        log.info("Creating a new type from the database with name: {}", dto.getName());
        TypeConnectionModel typeConnection = typeConnectionAssembler.toModel(dto);
        TypeConnectionModel savedTypeConnection = typeConnectionRepository.save(typeConnection);

        log.info("Successfully created TypeConnection with name: {}", savedTypeConnection.getName());
        return typeConnectionAssembler.toResponse(savedTypeConnection);
    }

    @Override
    @Transactional
    public TypeConnectionResponseDto updateTypeConnection(Integer id, TypeConnectionRequestDto dto) {

        TypeConnectionModel existingTypeConnection = typeConnectionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.error("Fetch failed: Type Connection with ID {} not found or inactive", id);
                    return new RuntimeException("Type connection not found with ID: " + id);
                });

        if(!existingTypeConnection.getId().equals(dto.getId())) {
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!existingTypeConnection.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, existingTypeConnection.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }

        typeConnectionAssembler.updateModel(dto, existingTypeConnection);

        TypeConnectionModel saved = typeConnectionRepository.save(existingTypeConnection);
        log.info("Successfully updated TypeConnection with and name: {}", saved.getName());
        return typeConnectionAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteTypeConnection(Integer id) {

        log.info("Request to disable Type Connection ID: id={}", id);

        TypeConnectionModel typeConnectionHide = typeConnectionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Disable failed: TypeConnection ID {} not found", id);
                    return new RuntimeException("Type connection not found with ID: " + id);
                });

        if (!Boolean.TRUE.equals(typeConnectionHide.getActive())) {
            log.warn("TypeConnection ID {} is already inactive", id);
            throw new IllegalStateException("Type connection is already inactive");
        }

        if (connectionRepository.existsByTypeConnectionId_IdAndActiveTrue(id)) {
            log.error("Constraint violation: Cannot disable TypeConnection ID {} because it has active connections", id);
            throw new IllegalStateException("Cannot disable: used by active connections.");
        }

        typeConnectionHide.setActive(false);
        typeConnectionRepository.save(typeConnectionHide);

        log.info("Successfully disabled TypeConnection ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeConnectionResponseDto> searchTypeConnections(
            TypeConnectionSearchRequestDto searchFilter,
            int page,
            int size) {

        log.info("searchTypeConnections - start (filter={}, page={}, size={})", searchFilter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<TypeConnectionResponseDto> results = typeConnectionRepository
                .findAll(TypeConnectionSpecifications.byFilter(searchFilter), pageable)
                .map(typeConnectionAssembler::toResponse);

        log.info("searchTypeConnections - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}
