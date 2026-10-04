package com.gonzalovega.clientmanagement.services.DatabaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.DatabaseAssembler;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseRequestDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseResponseDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseSearchRequestDto;
import com.gonzalovega.clientmanagement.models.DatabaseModel;
import com.gonzalovega.clientmanagement.repository.ClientResourceRepository;
import com.gonzalovega.clientmanagement.repository.DatabaseRepository;
import com.gonzalovega.clientmanagement.specifications.DatabaseSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseServiceImpl implements IDatabaseService {
    private final DatabaseRepository databaseRepository;
    private final ClientResourceRepository clientResourceRepository;
    private final DatabaseAssembler databaseAssembler;

    @Override
    @Transactional(readOnly = true)
    public List<DatabaseResponseDto> getAllDatabases() {
        log.info("getAllDatabases - start");

        List<DatabaseResponseDto> entities = databaseRepository.findAllByActiveTrue()
                .stream()
                .map(databaseAssembler::toResponse)
                .toList();

        log.info("getAllDatabases - end (returnedElements={})", entities.size());
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public DatabaseResponseDto getDatabaseById(Integer id) {
        log.info("Getting Database by id: {}", id);
        DatabaseModel entity = databaseRepository.findById(id)
                .filter(DatabaseModel::getActive)
                .orElseThrow(() -> {
                    log.warn("getDatabaseById - not found or inactive (id={})", id);
                    return new RuntimeException("Database not found with id: " + id);
                });

        log.debug("getDatabaseById - found (id={}, name={}, active={})",
                entity.getId(), entity.getName(), entity.getActive());

        log.info("Found Database entity: {} with data {}", id, entity.getName());
        return databaseAssembler.toResponse(entity);
    }

    @Override
    @Transactional
    public DatabaseResponseDto createDatabase(DatabaseRequestDto dto) {

        log.info("Creating a new type from the database with name: {}", dto.getName());
        DatabaseModel entity = databaseAssembler.toModel(dto);
        DatabaseModel savedEntity = databaseRepository.save(entity);
        log.info("Successfully created data base: {}", savedEntity.getName());
        return databaseAssembler.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public DatabaseResponseDto updateDatabase(Integer id, DatabaseRequestDto dto) {

        log.info("updateDatabase - start (urlId={}, bodyId={}, name={})", id, dto.getId(), dto.getName());

        DatabaseModel existingDataBase = databaseRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("updateDatabase - not found or inactive (id={})", id);
                    return new RuntimeException("Database not found with id: " + id);
                });

        if (dto.getId() == null || !id.equals(dto.getId())) {
            log.warn("updateDatabase - ID mismatch (urlId={}, bodyId={})", id, dto.getId());
            throw new IllegalArgumentException("IDs provided does not match with the ones in request body");
        }

        if (!existingDataBase.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateDatabase - version mismatch (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, existingDataBase.getVersionLock(), dto.getVersionLock());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data was modified by another user. Please refresh.");
        }

        databaseAssembler.updateModel(dto, existingDataBase);

        DatabaseModel updatedEntity = databaseRepository.save(existingDataBase);

        log.info("updateDatabase - end (updated id={}, name={}, versionLock={})",
                updatedEntity.getId(), updatedEntity.getName(), updatedEntity.getVersionLock());

        return databaseAssembler.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteDatabase(Integer id) {
        log.info("Deleting Db with id: {}", id);


        DatabaseModel entity = databaseRepository.findById(id)
                .filter(DatabaseModel::getActive)
                .orElseThrow(() -> {
                    log.error("Db with id {} not found for deletion", id);
                    return new RuntimeException("Db not found: " + id);
                });

        entity.setActive(false);
        databaseRepository.save(entity);

        log.info("Db with id {} marked as inactive (soft deleted)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatabaseResponseDto> searchDatabases(DatabaseSearchRequestDto filter, int page, int size) {
        log.info("searchDatabases - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<DatabaseResponseDto> results = databaseRepository
                .findAll(DatabaseSpecifications.byFilter(filter), pageable)
                .map(databaseAssembler::toResponse);

        log.info("searchDatabases - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}
