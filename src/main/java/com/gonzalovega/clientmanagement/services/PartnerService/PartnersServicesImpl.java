package com.gonzalovega.clientmanagement.services.PartnerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.PartnersAssembler;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersRequestDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersSearchDTO;
import com.gonzalovega.clientmanagement.models.PartnersModel;
import com.gonzalovega.clientmanagement.repository.PartnersRepository;
import com.gonzalovega.clientmanagement.specifications.PartnersSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PartnersServicesImpl implements IPartnersService {

    private final PartnersRepository partnersRepository;
    private final PartnersAssembler partnersAssembler;

    @Override
    @Transactional(readOnly = true)
    public List<PartnersResponseDTO> getAllPartners() {
        log.info("getAllPartners() - start" );

        List<PartnersResponseDTO> list = partnersRepository.findAll()
                .stream()
                .map(partnersAssembler::toResponse)
                .toList();

        log.info("getAllPartners() - end" );
        return list;
    }

    @Override
    public List<PartnersResponseDTO> getActivePartners() {

        log.info("getActivePartners() - start" );

        List<PartnersResponseDTO> list = partnersRepository.findAllByActiveTrue()
                .stream()
                .map(partnersAssembler::toResponse)
                .toList();

        log.info("getActivePartners() - end" );
        return list;



    }

    @Override
    public PartnersResponseDTO getPartnersById(Integer id) {

        PartnersModel found = partnersRepository.findPartnersById(id)
                .orElseThrow(() -> {
                    log.warn("getClientById - not found or inactive (id={})", id);
                    return new RuntimeException("Client not found with ID: " + id);
                });

        return partnersAssembler.toResponse(found);
    }

    @Override
    public PartnersResponseDTO createPartners(PartnersRequestDTO dto) {
            log.info("createPartners() - start" );

            PartnersModel model= partnersAssembler.toModel(dto);
            PartnersModel partnersModel = partnersRepository.save(model);

            log.info("createPartners() - end" );
            return partnersAssembler.toResponse(partnersModel);


    }

    @Override
    public PartnersResponseDTO updatePartners(Integer id, PartnersRequestDTO dto) {
        log.info("updatePartners() - start" );

        PartnersModel found = partnersRepository.findPartnersById(id)
                .orElseThrow(() -> {
                    log.warn("getClientById - not found or inactive (id={})", id);
                    return new RuntimeException("Client not found with ID: " + id);
                });

        if(!found.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!found.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, found.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }


        partnersAssembler.updateModel(found, dto);
        PartnersModel updated = partnersRepository.save(found);

        log.info("updatePartners() - end" );
        return partnersAssembler.toResponse(updated);

    }

    @Override
    public Page<PartnersResponseDTO> SearchPartners(PartnersSearchDTO filter, int page, int size) {
        log.info("searchPartners - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<PartnersResponseDTO> results = partnersRepository
                .findAll(PartnersSpecification.byFilter(filter), pageable)
                .map(partnersAssembler::toResponse);

        log.info("searchPartners - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }



    @Override
    public void deletePartners(Integer id) {

        log.info("deletePartners() - start" );

        PartnersModel found = partnersRepository.findPartnersById(id)
                .orElseThrow(() -> {
                    log.warn("getClientById - not found or inactive (id={})", id);
                    return new RuntimeException("Client not found with ID: " + id);
                });

        found.setActive(false);
        partnersRepository.save(found);
        log.info("deletePartners() - end" );

    }
}
