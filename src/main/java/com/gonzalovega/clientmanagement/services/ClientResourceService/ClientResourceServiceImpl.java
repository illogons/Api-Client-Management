package com.gonzalovega.clientmanagement.services.ClientResourceService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ClientResourceAssembler;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.models.*;
import com.gonzalovega.clientmanagement.repository.*;
import com.gonzalovega.clientmanagement.specifications.ClientResourceSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientResourceServiceImpl implements IClientResourceService {

    private final ClientResourceRepository clientResourceRepository;
    private final ClientRepository clientRepository;
    private final ResourceRepository resourceRepository;
    private final ClientResourceAssembler clientResourceAssembler;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResourceResponseDto> getAllClientResources() {
        log.info("getAllClientResources - start");

        List<ClientResourceResponseDto> entities = clientResourceRepository.findAllByActiveTrue()
                .stream()
                .map(clientResourceAssembler::toResponse)
                .toList();

        log.info("getAllClientResources - end (returnedElements={})", entities.size());
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResourceResponseDto getClientResourceById(Integer id) {
        log.info("getClientResourceById - start");

        ClientResourceModel entiti = clientResourceRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found with ID: " + id));

        log.info("getClientResourceById - end");
        return clientResourceAssembler.toResponse(entiti);


    }

    @Override
    @Transactional
    public ClientResourceResponseDto createClientResource(ClientResourceRequestDto dto) {
        log.info("createClientResource - start (clientId={}, resourceId={}, dbId={}, osId={}, versionId={})",
                dto.getClientId(), dto.getResourceId());

        validateDependencies(
                dto.getClientId(),
                dto.getResourceId()
        );

        isDateValid(dto.getStartDate(), dto.getEndDate());

        ClientModel clientProxy = clientRepository.getReferenceById(dto.getClientId());
        ResourceModel resourceProxy = resourceRepository.getReferenceById(dto.getResourceId());

        ClientResourceModel entity = clientResourceAssembler.toModel(dto, clientProxy, resourceProxy);

        ClientResourceModel saved = clientResourceRepository.save(entity);
        log.info("Client Resource created successfully. id={}", saved.getId());
        return clientResourceAssembler.toResponse(saved);
    }


    @Override
    @Transactional
    public ClientResourceResponseDto updateClientResource(Integer id, ClientResourceRequestDto dto) {
        log.info("updateClientResource - start (id={})", id);

        ClientResourceModel existing = clientResourceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientResource not found with ID: " + id));

        ClientModel client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + dto.getClientId()));

        ResourceModel resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new EntityNotFoundException("Resource not found with ID: " + dto.getResourceId()));

        if (!existing.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateConnection - optimistic lock conflict (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, existing.getVersionLock(), dto.getVersionLock());
            throw new ObjectOptimisticLockingFailureException(ConnectionModel.class, id);
        }

        if (!existing.getClientId().getId().equals(dto.getClientId()) ||
                !existing.getResourceId().getId().equals(dto.getResourceId())) {
            log.warn("updateClientResource - ID mismatch (id={})", id);
            throw new IllegalArgumentException("IDs provided do not match with the existing record");
        }

        isDateValid(dto.getStartDate(), dto.getEndDate());

        existing.setClientId(client);
        existing.setResourceId(resource);
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setPersonalized(dto.getPersonalized());

        ClientResourceModel saved = clientResourceRepository.save(existing);
        log.info("updateClientResource - end (id={})", id);
        return clientResourceAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteClientResource(Integer id) {

        log.info("deleteClientResource - start (id={})", id);

        ClientResourceModel existing = clientResourceRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("deleteClientResource - not found or already inactive (id={})", id);
                    return new RuntimeException("Client Resource not found with id: " + id);
                });

        existing.setActive(false);
        clientResourceRepository.save(existing);
        log.info("deleteClientResource - end (soft-deleted id={})", id);
    }

    private void validateDependencies(Integer clientId, Integer resourceId) {

        log.debug("validateDependencies - start (clientId={}, resourceId={})",
                clientId, resourceId);

        if (!clientRepository.existsByIdAndActiveTrue(clientId)) {
            log.warn("validateDependencies - client not found or inactive (clientId={})", clientId);
            throw new EntityNotFoundException("Client does not exist or is not active.");
        }

        if (!resourceRepository.existsByIdAndActiveTrue(resourceId)) {
            log.warn("validateDependencies - resource not found or inactive (resourceId={})", resourceId);
            throw new EntityNotFoundException("Resource does not exist or is not active.");
        }

        log.debug("validateDependencies - end (all ok)");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResourceResponseDto> searchClientResources(ClientResourceSearchRequestDto filter, int page, Integer size) {
        log.info("searchClientResources - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ClientResourceResponseDto> results = clientResourceRepository
                .findAll(ClientResourceSpecifications.byFilter(filter), pageable)
                .map(clientResourceAssembler::toResponse);

        log.info("searchClientResources - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }

    private void isDateValid(LocalDate start, LocalDate end) {
        log.debug("isDateValid - validating dates (start={}, end={})", start, end);

        if (start != null && end != null && start.isAfter(end)) {
            log.warn("isDateValid - invalid range (start={}, end={})", start, end);
            throw new RuntimeException("Chronological error: 'Since' date must be before 'Until' date.");
        }
    }
}