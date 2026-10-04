package com.gonzalovega.clientmanagement.services.ConnectionService;

import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionSearchRequestDto;
import com.gonzalovega.clientmanagement.specifications.ConnectionSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ConnectionAssembler;
import com.gonzalovega.clientmanagement.models.ConnectionModel;
import com.gonzalovega.clientmanagement.repository.ClientRepository;
import com.gonzalovega.clientmanagement.repository.ConnectionRepository;
import com.gonzalovega.clientmanagement.repository.TypeConnectionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements IConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ClientRepository clientRepository;
    private final TypeConnectionRepository typeConnectionRepository;
    private final ConnectionAssembler connectionAssembler;


    @Override
    @Transactional(readOnly = true)
    public List<ConnectionResponseDto> getAllConnections() {
        log.info("getAllConnections - start");

        List<ConnectionResponseDto> connections = connectionRepository.findByActiveTrue()
                .stream()
                .map(connectionAssembler::toResponse)
                .toList();

        log.info("getAllConnections - end (returnedElements={})", connections.size());
        return connections;
    }

    @Override
    @Transactional(readOnly = true)
    public ConnectionResponseDto getConnectionById(Integer id) {
        log.info("Fetching active Connection by id={}", id);

        ConnectionModel connect = connectionRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("getConnectionById - not found or inactive (id={})", id);
                    return new RuntimeException("Connection not found with ID: " + id);
                });

        log.debug("getConnectionById - found (id={}, clientId={},DmUuid={}, typeConnectionId={}, versionLock={})",
                connect.getId(),
                connect.getClientId() != null ? connect.getClientId().getId() : null,
                connect.getDmUuid() != null ? connect.getDmUuid() : null,
                connect.getTypeConnectionId() != null ? connect.getTypeConnectionId().getId() : null,
                connect.getVersionLock());

        log.info("getConnectionById - end (id={})", id);
        return connectionAssembler.toResponse(connect);
    }

    @Override
    @Transactional
    public ConnectionResponseDto createConnection(ConnectionRequestDto dto) {
        log.info("createConnection - start (clientId={}, typeConnectionId={})",
                dto.getClientId(), dto.getTypeConnectionId());

        clientRepository.findByIdAndActiveTrue(dto.getClientId())
                .orElseThrow(() -> {
                    log.warn("createConnection - rejected: client inactive or not found (clientId={})", dto.getClientId());
                    return new ResponseStatusException(HttpStatus.CONFLICT,
                            "Client is inactive or not found: " + dto.getClientId());
                });

        typeConnectionRepository.findByIdAndActiveTrue(dto.getTypeConnectionId())
                .orElseThrow(() -> {
                    log.warn("createConnection - rejected: typeConnection inactive or not found (typeConnectionId={})", dto.getTypeConnectionId());
                    return new ResponseStatusException(HttpStatus.CONFLICT,
                            "TypeConnection is inactive or not found: " + dto.getTypeConnectionId());
                });

        ConnectionModel entity = connectionAssembler.toModel(dto);
        entity.setClientId(clientRepository.getReferenceById(dto.getClientId()));
        entity.setTypeConnectionId(typeConnectionRepository.getReferenceById(dto.getTypeConnectionId()));

        ConnectionModel saved = connectionRepository.save(entity);

        log.info("createConnection - end (id={}, clientId={}, typeConnectionId={})",
                saved.getId(), dto.getClientId(), dto.getTypeConnectionId());

        return connectionAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public ConnectionResponseDto updateConnection(Integer id, ConnectionRequestDto dto) {
        log.info("updateConnection - start (id={}, clientId={}, typeConnectionId={})",
                id, dto.getClientId(), dto.getTypeConnectionId());

        ConnectionModel existing = connectionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("updateConnection - not found or inactive (id={})", id);
                    return new RuntimeException("Connection not found with ID: " + id);
                });

        log.debug("updateConnection - DB state (id={}, dbVersionLock={}, clientId={}, typeConnectionId={})",
                id, existing.getVersionLock(),
                existing.getClientId() != null ? existing.getClientId().getId() : null,
                existing.getTypeConnectionId() != null ? existing.getTypeConnectionId().getId() : null);

        if (!existing.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateConnection - optimistic lock conflict (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, existing.getVersionLock(), dto.getVersionLock());
            throw new ObjectOptimisticLockingFailureException(ConnectionModel.class, id);
        }

        clientRepository.findByIdAndActiveTrue(dto.getClientId())
                .orElseThrow(() -> {
                    log.warn("updateConnection - rejected: client not found or inactive (clientId={})", dto.getClientId());
                    return new RuntimeException("Client not found with ID: " + dto.getClientId());
                });

        typeConnectionRepository.findByIdAndActiveTrue(dto.getTypeConnectionId())
                .orElseThrow(() -> {
                    log.warn("updateConnection - rejected: typeConnection not found or inactive (typeConnectionId={})", dto.getTypeConnectionId());
                    return new RuntimeException("TypeConnection not found with ID: " + dto.getTypeConnectionId());
                });

        connectionAssembler.updateModel(dto, existing);
        existing.setClientId(clientRepository.getReferenceById(dto.getClientId()));
        existing.setTypeConnectionId(typeConnectionRepository.getReferenceById(dto.getTypeConnectionId()));

        ConnectionModel updated = connectionRepository.save(existing);

        log.info("updateConnection - end (id={}, versionLock={})", updated.getId(), updated.getVersionLock());
        return connectionAssembler.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteConnection(Integer id) {
        log.info("Soft deleting Connection id={}", id);

        ConnectionModel existing = connectionRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("Delete rejected: Active Connection not found. id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Active id not found: " + id);
                });

        existing.setActive(false);
        connectionRepository.save(existing);

        log.info("Connection deactivated successfully. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConnectionResponseDto> searchConnections(ConnectionSearchRequestDto filterDto, int page, int size) {
        log.info("searchConnections - start (filter={}, page={}, size={})", filterDto, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ConnectionResponseDto> results = connectionRepository
                .findAll(ConnectionSpecifications.byFilter(filterDto), pageable)
                .map(connectionAssembler::toResponse);

        log.info("searchConnections - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}