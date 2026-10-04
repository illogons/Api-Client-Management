package com.gonzalovega.clientmanagement.services.OperativeSystemService;

import com.gonzalovega.clientmanagement.security.SecurityUtils;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemSearchRequestDto;
import com.gonzalovega.clientmanagement.repository.ClientResourceRepository;
import com.gonzalovega.clientmanagement.specifications.OperativeSystemSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.OperativeSystemAssembler;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemRequestDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemResponseDto;
import com.gonzalovega.clientmanagement.models.OperativeSystemModel;
import com.gonzalovega.clientmanagement.repository.OperativeSystemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperativeSystemServiceImpl implements IOperativeSystemService {

    private final OperativeSystemRepository operativeSystemRepository;
    private final OperativeSystemAssembler operativeSystemAssembler;
    private final ClientResourceRepository clientResourceRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional(readOnly = true)
    public List<OperativeSystemResponseDto> getAllOperativeSystems() {
        String token = securityUtils.getCurrentToken();
        log.info("token: {}", token);

        log.info("getAllOperativeSystems - start");

        List<OperativeSystemResponseDto> operativeSystems = operativeSystemRepository.findByActiveTrue()
                .stream()
                .map(operativeSystemAssembler::toResponse)
                .toList();

        log.info("getAllOperativeSystems - end (returnedElements={})", operativeSystems.size());
        return operativeSystems;
    }

    @Override
    @Transactional(readOnly = true)
    public OperativeSystemResponseDto getOperativeSystemById(Integer id) {
        OperativeSystemModel os = operativeSystemRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Active operative system not found: " + id));
        return operativeSystemAssembler.toResponse(os);
    }

    @Override
    @Transactional
    public OperativeSystemResponseDto createOperativeSystem(OperativeSystemRequestDto dto) {

        log.info("Creating a new operative system from the database with name: {}", dto.getName());
        OperativeSystemModel model = operativeSystemAssembler.toModel(dto);
        OperativeSystemModel saved = operativeSystemRepository.save(model);

        log.info("Successfully created operative system with name: {}", saved.getName());
        return operativeSystemAssembler.toResponse(saved);
    }


    @Override
    @Transactional
    public OperativeSystemResponseDto updateOperativeSystem(Integer id, OperativeSystemRequestDto dto) {

        log.info("updateOperativeSystem - start (urlId={}, bodyId={}, name={}, version={}, reqVersionLock={})",
                id, dto.getId(), dto.getName(), dto.getVersion(), dto.getVersionLock());

        if (dto.getId() == null || !id.equals(dto.getId())) {
            log.warn("updateOperativeSystem - ID mismatch (urlId={}, bodyId={})", id, dto.getId());
            throw new IllegalArgumentException("IDs provided does not match with the ones in request body");
        }

        OperativeSystemModel existingSO = operativeSystemRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("updateOperativeSystem - not found or inactive (id={})", id);
                    return new RuntimeException("Operative system not found with ID: " + id);
                });

        log.debug("updateOperativeSystem - current DB state (id={}, dbVersionLock={}, name={}, version={})",
                id, existingSO.getVersionLock(), existingSO.getName(), existingSO.getVersion());

        if (!existingSO.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateOperativeSystem - version mismatch (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, existingSO.getVersionLock(), dto.getVersionLock());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data was modified by another user. Please refresh.");
        }

        operativeSystemAssembler.updateModel(dto, existingSO);

        OperativeSystemModel saved = operativeSystemRepository.save(existingSO);

        log.info("updateOperativeSystem - end (updated id={}, name={}, version={}, versionLock={})",
                saved.getId(), saved.getName(), saved.getVersion(), saved.getVersionLock());

        return operativeSystemAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteOperativeSystem(Integer id) {

        log.info("Disabling operative system with ID: {}", id);
        OperativeSystemModel osHide = operativeSystemRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Active operative system not found: " + id));

        osHide.setActive(false);
        operativeSystemRepository.save(osHide);
        log.info("Disabling operative system with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperativeSystemResponseDto> searchOperativeSystems(
            OperativeSystemSearchRequestDto filter,
            int page,
            int size) {

        log.info("searchOperativeSystems - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<OperativeSystemResponseDto> results = operativeSystemRepository
                .findAll(OperativeSystemSpecifications.byFilter(filter), pageable)
                .map(operativeSystemAssembler::toResponse);

        log.info("searchOperativeSystems - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }
}