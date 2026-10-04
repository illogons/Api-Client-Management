package com.gonzalovega.clientmanagement.services.UpdateService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.UpdateAssembler;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateRequestDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateResponseDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateSearchRequestDto;
import com.gonzalovega.clientmanagement.models.*;
import com.gonzalovega.clientmanagement.repository.ClientRepository;
import com.gonzalovega.clientmanagement.repository.UpdateRepository;
import com.gonzalovega.clientmanagement.repository.UsersRepository;
import com.gonzalovega.clientmanagement.repository.VersionRepository;
import com.gonzalovega.clientmanagement.specifications.UpdateSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UpdateServiceImpl implements IUpdateService {

    private final ClientRepository clientRepository;
    private final UpdateRepository updateRepository;
    private final VersionRepository versionRepository;
    private final UsersRepository usersRepository;
    private final UpdateAssembler updateAssembler;

    @Override
    @Transactional
    public List<UpdateResponseDto> getAllUpdates() {
        log.info("getAllUpdate - Start");

        List<UpdateResponseDto> entities = updateRepository.findAllByActiveTrue()
                .stream().map(updateAssembler::toResponse)
                .toList();

        log.info("getAllUpdate  - end (returnedElements={})", entities.size());
        return  entities;

    }

    @Override
    @Transactional
    public UpdateResponseDto getUpdateById (Integer id) {
         log.info("getUpdateById - Start");

         UpdateModel entiti = updateRepository.findById(id)
                 .filter(model -> Boolean.TRUE.equals(model.getActive()))
                 .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

         log.info("getUpdateById - end (returnedElements={})", entiti);

        return updateAssembler.toResponse(entiti);

    }

    @Scheduled(fixedRate = 60_000)
    public void processFutureDates() {
        List<UpdateModel> vencidos = updateRepository
                .findByFutureDateIsNotNullAndFutureDateBefore(LocalDateTime.now());
        log.info("processFutureDates - {} registros con futureDate vencida", vencidos.size());
    }

    @Override
    @Transactional
    public Page<UpdateResponseDto> searchUpdates(UpdateSearchRequestDto filter, int page, int size) {
        log.info("searchUpdates - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<UpdateResponseDto> results = updateRepository
                .findAll(UpdateSpecifications.byFilter(filter), pageable)
                .map(updateAssembler::toResponse);

        log.info("searchUpdates - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }

    @Override
    @Transactional
    public UpdateResponseDto updateUpdate(Integer id, UpdateRequestDto dto){
        log.info("Updating Resource with id: {}", id);

        isDateValid(dto.getLaunchDate(), dto.getTerminationDate());
        validateIds(dto);
        validateDate(dto.getFutureDate());


        UpdateModel buscar = updateRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        if(!buscar.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }
        if (!buscar.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateConnection - optimistic lock conflict (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, buscar.getVersionLock(), dto.getVersionLock());
            throw new ObjectOptimisticLockingFailureException(ConnectionModel.class, id);
        }

        ClientModel client = clientRepository.findByIdAndActiveTrue(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + dto.getClientId()));

        UsersModel user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        VersionModel version = versionRepository.findById(dto.getVersionId())
                .orElseThrow(() -> new RuntimeException("Version not found with ID: " + dto.getVersionId()));


        updateAssembler.updateModel(dto, buscar);
        buscar.setClientId(client);
        buscar.setVersionId(version);
        buscar.setUserId(user);

        UpdateModel saved = updateRepository.save(buscar);
        log.info("Successfully updated update with ID: {}", id);
        return updateAssembler.toResponse(saved);


    }

    @Override
    @Transactional
    public UpdateResponseDto createUpdate(UpdateRequestDto dto){

        log.info("Creating new update from the database");

        isDateValid(dto.getLaunchDate(), dto.getTerminationDate());
        validateIds(dto);
        validateDate(dto.getFutureDate());



        ClientModel client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + dto.getClientId()));

        UsersModel user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        VersionModel version = versionRepository.findById(dto.getVersionId())
                .orElseThrow(() -> new RuntimeException("Version not found with ID: " + dto.getVersionId()));

        UpdateModel model = updateAssembler.toModel(dto);
        model.setClientId(client);
        model.setVersionId(version);
        model.setUserId(user);


        UpdateModel saved = updateRepository.save(model);
        log.info("Successfully created update");
        return updateAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteUpdate(Integer id){
        log.info("Deleting update from the database");

        UpdateModel vivo= updateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delete not found with ID: " + id));

        vivo.setActive(false);
        updateRepository.save(vivo);
        log.info("Successfully deleted resource");
    }

    private void isDateValid(LocalDate launch, LocalDate termination) {
        if (launch != null && termination != null && launch.isAfter(termination)) {
            throw new RuntimeException("Chronological error: launch date must be before termination date.");
        }
    }

    private void validateDate(LocalDateTime future) {
        if (future != null && future.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Chronological error: future date must not be in the past.");
        }
    }

    private void validateIds(UpdateRequestDto dto) {
        if (dto.getClientId() == null || dto.getClientId() <= 0) {
            throw new IllegalArgumentException("clientId is required and must be valid");
        }
        if (dto.getUserId() == null || dto.getUserId() <= 0) {
            throw new IllegalArgumentException("userId is required and must be valid");
        }
        if (dto.getVersionId() == null || dto.getVersionId() <= 0) {
            throw new IllegalArgumentException("versionId is required and must be valid");
        }
    }
}
