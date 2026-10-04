package com.gonzalovega.clientmanagement.services.ResponsibleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ResponsibleAssembler;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleRequestDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleResponseDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleSearchDto;
import com.gonzalovega.clientmanagement.models.ResponsibleModel;
import com.gonzalovega.clientmanagement.repository.ResponsibleRespository;
import com.gonzalovega.clientmanagement.specifications.PartnersSpecification;
import com.gonzalovega.clientmanagement.specifications.ResponsibleSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ResponsibleServicesImpl implements IResponsibleService{

    private final ResponsibleRespository responsibleRespository;
    private final ResponsibleAssembler responsibleAssembler;

    @Override
    public List<ResponsibleResponseDto> getAllResponsibles() {

        List<ResponsibleResponseDto> lista= responsibleRespository.findAll()
                .stream()
                .map(responsibleAssembler::toResponse)
                .toList();

        return lista;
    }

    @Override
    public List<ResponsibleResponseDto> getActiveResponsibles() {

        List<ResponsibleResponseDto> trues= responsibleRespository.findAllByActiveTrue().stream()
                .filter(ResponsibleModel::getActive)
                .map(responsibleAssembler::toResponse)
                .toList();

        return trues;

    }

    @Override
    public ResponsibleResponseDto getResponsiblesById(Integer id) {

        ResponsibleModel responsibleModel = responsibleRespository.findById(id)
                .orElseThrow(() -> {
                    log.warn("getResponsible - not found or inactive (id={})", id);
                    return new RuntimeException("Client not found with ID: " + id);
                });

        return responsibleAssembler.toResponse(responsibleModel);
    }

    @Override
    public ResponsibleResponseDto createResponsibles(ResponsibleRequestDto dto) {

        ResponsibleModel model= responsibleAssembler.toModel(dto);
        ResponsibleModel responsibleModel = responsibleRespository.save(model);

        return responsibleAssembler.toResponse(responsibleModel);
    }

    @Override
    public ResponsibleResponseDto updateResponsibles(Integer id, ResponsibleRequestDto dto) {

        ResponsibleModel buscar = responsibleRespository.findById(id).orElseThrow(() -> {
            log.warn("getResponsible - not found(id={})", id);
            return new RuntimeException("Client not found with ID: " + id);
        });

        if(!buscar.getVersionLock().equals(dto.getVersionLock())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("Data was modified by another user. Please refresh.");
        }
        if(!buscar.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        responsibleAssembler.toUpdate(dto, buscar);
        ResponsibleModel updated = responsibleRespository.save(buscar);

        log.info("update() - end" );
        return responsibleAssembler.toResponse(updated);

    }

    @Override
    public void deleteResponsibles(Integer id) {

        ResponsibleModel buscar = responsibleRespository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Active operative system not found or inactive: " + id));

        buscar.setActive(false);
        responsibleRespository.save(buscar);
        log.info("delete() - end" );
    }

    @Override
    public Page<ResponsibleResponseDto> SearchResponsibles(ResponsibleSearchDto filter, int page, int size) {
        log.info("searchPartners - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ResponsibleResponseDto> results = responsibleRespository
                .findAll(ResponsibleSpecifications.byFilter(filter), pageable)
                .map(responsibleAssembler::toResponse);

        log.info("searchPartners - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;

    }


}
