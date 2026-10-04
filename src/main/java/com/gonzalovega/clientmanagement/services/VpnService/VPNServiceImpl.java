package com.gonzalovega.clientmanagement.services.VpnService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.VPNAssembler;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNRequestDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNResponseDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNSearchDto;
import com.gonzalovega.clientmanagement.models.VPNModel;
import com.gonzalovega.clientmanagement.repository.VPNRepository;
import com.gonzalovega.clientmanagement.specifications.VPNSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class VPNServiceImpl implements IVPNService {

    private final VPNRepository vpnRepository;
    private final VPNAssembler vpnAssembler;

    @Override
    @Transactional
    public List<VPNResponseDto> getAllVPN() {
        log.info("getUsers() - start");

        List<VPNResponseDto> lista = vpnRepository.findAll()
                .stream()
                .map(vpnAssembler::toResponse)
                .toList();

        log.info("getUsers() - end");
        return lista;
    }

    @Override
    public List<VPNResponseDto> getActiveVPN() {
        log.info("getUsers() - start");

        List<VPNResponseDto> lista = vpnRepository.findAll()
                .stream()
                .filter(model -> Boolean.TRUE.equals(model.getActive()))
                .map(vpnAssembler::toResponse)
                .toList();

        log.info("getUsers() - end");
        return lista;

    }

    @Override
    public VPNResponseDto getVPNUserById(Integer id) {

        log.info("getUsers() - start");

        VPNModel lista = vpnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        log.info("getUsers() - end");

        return vpnAssembler.toResponse(lista);
    }

    @Override
    public VPNResponseDto createVPN(VPNRequestDto dto) {
        log.info("createVPN() - start");
        VPNModel lista = vpnAssembler.toModel(dto);
        VPNModel createdVpn = vpnRepository.save(lista);
        log.info("createVPN() - end");
        return  vpnAssembler.toResponse(createdVpn);
    }

    @Override
    public VPNResponseDto updateVPNUpdate(Integer id, VPNRequestDto dto) {
        log.info("updateVPN() - start");

        VPNModel lista = vpnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        if (!lista.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("Version mismatch for ID {}: DB={}, Request={}", id, lista.getVersionLock(), dto.getVersionLock());
            throw new RuntimeException("Data was modified by another user. Please refresh.");
        }

        if(!lista.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        vpnAssembler.toUpdate(dto, lista);
        VPNModel saved = vpnRepository.save(lista);
        log.info("updateVPN() - end");
        return vpnAssembler.toResponse(saved);

    }

    @Override
    public Page<VPNResponseDto> SearchVPN(VPNSearchDto filter, int page, int size) {
        log.info("searchConnections - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<VPNResponseDto> results = vpnRepository
                .findAll(VPNSpecifications.byFilter(filter), pageable)
                .map(vpnAssembler::toResponse);

        log.info("searchConnections - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;    }

    @Override
    public void deleteVPN(Integer id) {

        log.info("deleteVPN() - start");
        VPNModel lista = vpnRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));

        lista.setActive(false);
        vpnRepository.save(lista);
        log.info("deleteVPN() - end");
    }
}
