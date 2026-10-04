package com.gonzalovega.clientmanagement.services.ClientService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.assemblers.ClientAssembler;
import com.gonzalovega.clientmanagement.assemblers.ClientVpnAssembler;
import com.gonzalovega.clientmanagement.assemblers.PartnersAssembler;
import com.gonzalovega.clientmanagement.assemblers.ResponsibleAssembler;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientSearchRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.VersionHistoryDTO;
import com.gonzalovega.clientmanagement.models.*;
import com.gonzalovega.clientmanagement.repository.*;
import com.gonzalovega.clientmanagement.specifications.ClientSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientServicesImpl implements IClientService {

    private final PartnersRepository partnersRepository;
    private final PartnersAssembler partnersAssembler;
    private final DatabaseRepository databaseRepository;
    private final OperativeSystemRepository operativeSystemRepository;
    private final ClientRepository clientRepository;
    private final ClientAssembler clientAssembler;
    private final ConnectionRepository connectionRepository;
    private final UpdateRepository updateRepository;
    private final ClientResourceRepository clientResourceRepository;
    private final ClientVpnRepository clientVPNRepository;
    private final ClientVpnAssembler clientVpnAssembler;
    private final ResponsibleRespository  responsibleRespository;
    private final ResponsibleAssembler responsibleAssembler;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAllClients() {
        log.info("getAllClients - start");

        List<ClientResponseDto> clients = clientRepository.findByActiveTrue()
                .stream()
                .map(clientAssembler::toResponse)
                .toList();

        log.info("getAllClients - end (returnedElements={})", clients.size());
        return clients;
    }

    public List<VersionHistoryDTO> getVersionHistoryByClient(Integer clientId) {
        return updateRepository.findByClientIdId(clientId)
                .stream()
                .sorted(Comparator.comparing(UpdateModel::getLaunchDate).reversed()
                        .thenComparing(Comparator.comparing(UpdateModel::getId).reversed()))
                .map(u -> {
                    VersionHistoryDTO dto = new VersionHistoryDTO();
                    dto.setUpdateId(u.getId());
                    dto.setLaunchDate(u.getLaunchDate());
                    dto.setTerminationDate(u.getTerminationDate());
                    dto.setActive(u.getActive());
                    dto.setVersionName(u.getVersionId().getVersion());
                    dto.setChangelog(u.getVersionId().getChangelog());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getClientById(Integer id) {

        log.info("getClientById - start (id={})", id);

        ClientModel client = clientRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.warn("getClientById - not found or inactive (id={})", id);
                    return new RuntimeException("Client not found with ID: " + id);
                });
        log.debug("getClientById - found (id={}, username={}, active={}, joinDate={}, terminationDate={})",
                client.getId(), client.getCreatedBy(), client.getActive(), client.getJoinDate(), client.getTerminationDate());

        log.info("getClientById - end (id={})", id);

        return clientAssembler.toResponse(client);
    }

    @Override
    @Transactional
    public ClientResponseDto createClient(ClientRequestDto dto) {
        log.info("createClient - start (username={})", dto.getName());

        isDateValid(dto.getJoinDate(), dto.getTerminationDate());

        ClientModel client = clientAssembler.toModel(dto);

        if (dto.getDatabaseId() != null) {
            DatabaseModel dataBase = databaseRepository.findById(dto.getDatabaseId())
                    .orElseThrow(() -> new EntityNotFoundException("DataBase not found"));
            client.setDatabaseId(dataBase);
        }

        if (dto.getOperativeSystemId() != null) {
            OperativeSystemModel os = operativeSystemRepository.findById(dto.getOperativeSystemId())
                    .orElseThrow(() -> new EntityNotFoundException("OperativeSystem not found"));
            client.setOperativeSystemId(os);
        }

        if( dto.getPartnerId() != null){
            PartnersModel pm= partnersRepository.findPartnersById(dto.getPartnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Partners not found"));
            client.setPartnerId(pm);

        }

        if( dto.getResponsibleId() != null){
            ResponsibleModel rm= responsibleRespository.findById(dto.getResponsibleId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsible not found"));
            client.setResponsibleId(rm);

        }

        log.info("Creating new id in the database");
        ClientModel savedClient = clientRepository.save(client);
        log.info("createClient - end (created id={}, username={})", savedClient.getId(), savedClient.getCreatedBy());

        return clientAssembler.toResponse(savedClient);
    }

    @Override
    @Transactional
    public ClientResponseDto updateClient(Integer id, ClientRequestDto dto) {
        if(!id.equals(dto.getId())){
            log.warn("updateClient - ID mismatch between URL and body (urlId=({}), bodyId=({}))",
                    id, dto.getId());

            throw new IllegalArgumentException("ID provided does not match with the one in request body");
        }


        log.info("updateClient - start (id={}, username={})", id, dto.getName());

        ClientModel existingClient = clientRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                log.warn("updateClient - not found or inactive (id={})", id);
                return new RuntimeException("Client not found with ID: " + id);
                });

        log.debug("updateClient - current DB state (id={}, versionLock={}, active={}, username={})",
                id, existingClient.getVersionLock(), existingClient.getActive(), existingClient.getCreatedBy());


        isDateValid(dto.getJoinDate(), dto.getTerminationDate());

        if (!existingClient.getVersionLock().equals(dto.getVersionLock())) {
            log.warn("updateConnection - optimistic lock conflict (id={}, dbVersionLock={}, reqVersionLock={})",
                    id, existingClient.getVersionLock(), dto.getVersionLock());
            throw new ObjectOptimisticLockingFailureException(ConnectionModel.class, id);
        }

        existingClient.setId(id);
        existingClient.setUrl(dto.getUrl());
        existingClient.setComment(dto.getComment());
        existingClient.setSupport(dto.getSupport());
        existingClient.setName(dto.getName());
        existingClient.setEmail(dto.getEmail());
        existingClient.setAddress(dto.getAddress());
        existingClient.setPhoneNumber(dto.getPhoneNumber());
        existingClient.setJoinDate(dto.getJoinDate());
        existingClient.setTerminationDate(dto.getTerminationDate());

        if (dto.getDatabaseId() != null) {
            DatabaseModel dataBase = databaseRepository.findById(dto.getDatabaseId())
                    .orElseThrow(() -> new EntityNotFoundException("DataBase not found"));
            existingClient.setDatabaseId(dataBase);
        }

        if (dto.getOperativeSystemId() != null) {
            OperativeSystemModel os = operativeSystemRepository.findById(dto.getOperativeSystemId())
                    .orElseThrow(() -> new EntityNotFoundException("OperativeSystem not found"));
            existingClient.setOperativeSystemId(os);
        }

        if( dto.getPartnerId() !=null){
            PartnersModel part = partnersRepository.findPartnersById(dto.getPartnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Partner not found"));
            existingClient.setPartnerId(part);
        }

        if( dto.getResponsibleId() != null){
            ResponsibleModel rm= responsibleRespository.findById(dto.getResponsibleId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsible not found"));
            existingClient.setResponsibleId(rm);

        }

        ClientModel saved = clientRepository.save(existingClient);

        return clientAssembler.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteClient(Integer id) {

        log.info("deleteClient - start (id={})", id);

        ClientModel client = clientRepository.findByIdAndActiveTrue(id).orElseThrow(() -> {
                    log.warn("deleteClient - client not found (id={})", id);
                    return new RuntimeException("Cannot disable. Client not found with ID: " + id);
                });



        List<ClientResourceModel> Clientresource = clientResourceRepository.findAllByClientIdIdAndActiveTrue(id);
        List<UpdateModel> activeUpdates = updateRepository.findAllByClientIdIdAndActiveTrue(id);
        List<ClientVPNModel> clientvpn = clientVPNRepository.findAllByClientIdIdAndActiveTrue(id);
        List<ConnectionModel> connection= connectionRepository.findAllByClientIdIdAndActiveTrue(id);

        if (!Clientresource.isEmpty()) {
            log.info("deleteClientResource - deactivating {} client resources (clientId={})", Clientresource.size(), id);
            Clientresource.forEach(resource -> resource.setActive(false));
            clientResourceRepository.saveAll(Clientresource);
            log.debug("deleteClient - client resources deactivated (clientId={})", id);
        } else {
            log.debug("deleteClient - no active client resources found (clientId={})", id);
        }

        if (!clientvpn.isEmpty()) {
            clientvpn.forEach(updateModel -> {updateModel.setActive(false); });
            clientVPNRepository.saveAll(clientvpn);
            log.debug( "deleteClient -  client resources deactivated (clientId={});", id);
        }else{
            log.debug("deleteClient - no active client vpn found (clientId={})", id);
        }

        if(!connection.isEmpty()) {
            connection.forEach(resource -> {resource.setActive(false);});
            connectionRepository.saveAll(connection);
            log.debug("deleteClient - client resources deactivated (clientId={})", id);

        }else{
            log.debug("deleteClient - no active connections found (clientId={})", id);
        }

        if(!activeUpdates.isEmpty()) {
            log.info("delete UpdateVersion - deactivating {} update client (clientId={}))", activeUpdates.size(), id);
            activeUpdates.forEach(update -> update.setActive(false));
            updateRepository.saveAll(activeUpdates);
            log.debug("delete UpdateVersion - update client (clientId={})", id);
        }else{
            log.debug("delete UpdateVersion - no update client (clientId={})", id);
        }

        client.setActive(false);
        clientRepository.save(client);

        log.info("deleteClient - end (client disabled id={})", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClientResponseDto> searchClients(ClientSearchRequestDto filter, int page, int size) {
        log.info("searchClients - start (filter={}, page={}, size={})", filter, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ClientResponseDto> results = clientRepository
                .findAll(ClientSpecifications.byFilter(filter), pageable)
                .map(clientAssembler::toResponse);

        log.info("searchClients - end (returnedElements={}, totalElements={}, totalPages={})",
                results.getNumberOfElements(), results.getTotalElements(), results.getTotalPages());

        return results;
    }


    private void isDateValid(LocalDate join, LocalDate termination) {
        log.debug("isDateValid - validating dates (join={}, termination={})", join, termination);

        if (join != null && termination != null && join.isAfter(termination)) {
            log.warn("isDateValid - invalid range (join={}, termination={})", join, termination);
            throw new RuntimeException("Chronological error: 'Since' date must be before 'Until' date.");
        }
    }

}
