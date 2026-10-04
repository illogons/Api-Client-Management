package com.gonzalovega.clientmanagement.services.ClientVpnService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ClientVpnAssembler;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnSearchDto;
import com.gonzalovega.clientmanagement.models.ClientModel;
import com.gonzalovega.clientmanagement.models.ClientVPNModel;
import com.gonzalovega.clientmanagement.models.ConnectionModel;
import com.gonzalovega.clientmanagement.models.VPNModel;
import com.gonzalovega.clientmanagement.repository.ClientRepository;
import com.gonzalovega.clientmanagement.repository.ClientVpnRepository;
import com.gonzalovega.clientmanagement.repository.VPNRepository;
import com.gonzalovega.clientmanagement.specifications.ClientVpnSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClientVpnServiceImpl implements IClientVpnService {

    private final ClientVpnRepository clientVpnRepository;
    private final ClientVpnAssembler clientVpnAssembler;
    private final ClientRepository clientRepository;
    private final VPNRepository vpnRepository;


    @Override
    public List<ClientVpnResponseDto> getAllCV() {
        log.info("getAllCV() - start" );

        List<ClientVpnResponseDto> clientVpnResponseDtos = clientVpnRepository.findAll()
                .stream()
                .map(clientVpnAssembler::toResponse)
                .toList();

        log.info("getAllCV() - end");
        return clientVpnResponseDtos;

    }

    @Override
    public List<ClientVpnResponseDto> getActiveCV() {

        log.info("getActiveCV() - start");
        List<ClientVpnResponseDto> clientVpnResponseDtos = clientVpnRepository.findAllByActiveTrue()
                .stream()
                .map(clientVpnAssembler::toResponse)
                .toList();

        log.info("getActiveCV() - end");
        return clientVpnResponseDtos;
    }

    @Override
    public ClientVpnResponseDto getCVById(Integer id) {

        log.info("getCVUserById() - start");

        ClientVPNModel lista = clientVpnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        log.info("getCVUserById() - end");
        return clientVpnAssembler.toResponse(lista);


    }

    @Override
    public ClientVpnResponseDto createCV(ClientVpnRequestDto dto) {

        log.info("createCV() - start");

        ClientModel client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + dto.getClientId()));

        VPNModel vpn = vpnRepository.findById(dto.getVpnId())
                .orElseThrow(() -> new RuntimeException("VPN not found with ID: " + dto.getVpnId()));

        if (clientVpnRepository.existsByClientId(client)) {
            log.warn("createCV() - Client already has a VPN assigned.");
            throw new IllegalArgumentException("Client already has a VPN assigned.");
        }

        ClientVPNModel model= clientVpnAssembler.toModel(dto);
        model.setClientId(client);
        model.setVpnId(vpn);

        ClientVPNModel save =  clientVpnRepository.save(model);
        log.info("createCV() - end");
        return clientVpnAssembler.toResponse(save);
    }



    @Override
    public ClientVpnResponseDto updateCV(Integer id, ClientVpnRequestDto dto) {

        log.info("updateCV() - start");


        ClientVPNModel client= clientVpnRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + id));


        if(!client.getId().equals(dto.getId())){
            log.error("ID mismatch: Path ID {} does not match DTO ID {}", id, dto.getId());
            throw new IllegalArgumentException("ID in path and request body must match.");
        }

        if (!client.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateConnection - optimistic lock conflict (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, client.getVersionLock(), dto.getVersionLock());
            throw new ObjectOptimisticLockingFailureException(ConnectionModel.class, id);
        }


        ClientModel client2= clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + id));

        VPNModel vpn = vpnRepository.findById(dto.getVpnId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + id));



        clientVpnAssembler.toUpdate(dto, client);
        client.setClientId(client2);
        client.setVpnId(vpn);
        ClientVPNModel saved = clientVpnRepository.save(client);


        log.info("updateCV() - end");
        return clientVpnAssembler.toResponse(saved);




    }

    @Override
    public Page<ClientVpnResponseDto> SearchCV(ClientVpnSearchDto filter, int page, int size) {
        log.info("searchConnections - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ClientVpnResponseDto> results = clientVpnRepository
                .findAll(ClientVpnSpecification.byFilter(filter), pageable)
                .map(clientVpnAssembler::toResponse);

        log.info("searchConnections - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }



    @Override
    public void deleteVPN(Integer id) {

        log.info("deleteVPN() - start");
        ClientVPNModel client = clientVpnRepository.findByIdAndActiveTrue(id).orElseThrow(() -> {
            log.warn("deleteClient - client not found (id={})", id);
            return new RuntimeException("Cannot disable. Client not found with ID: " + id);
        });

        client.setActive(false);
        clientVpnRepository.save(client);
        log.info("deleteVPN() - end");

    }

    private void validateDependencies(Integer clientId, Integer vpnId) {

        log.debug("validateDependencies - start (clientId={}, resourceId={})",
                clientId, vpnId);

        if (!clientRepository.existsByIdAndActiveTrue(clientId)) {
            log.warn("validateDependencies - client not found or inactive (clientId={})", clientId);
            throw new EntityNotFoundException("Client does not exist or is not active.");
        }

        if (!vpnRepository.existsByIdAndActiveTrue(vpnId)) {
            log.warn("validateDependencies - resource not found or inactive (resourceId={})", vpnId);
            throw new EntityNotFoundException("Resource does not exist or is not active.");
        }

        log.debug("validateDependencies - end (all ok)");
    }
}
