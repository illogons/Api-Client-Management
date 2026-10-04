package com.gonzalovega.clientmanagement.services.ClientService;

import com.gonzalovega.clientmanagement.dto.ClientDto.ClientRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientSearchRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.VersionHistoryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code Client} entities.
 * <p>
 * Defines CRUD and search operations for clients. Implementations typically:
 * <ul>
 *   <li>Return only active clients for read operations (soft-delete behavior)</li>
 *   <li>Validate business rules (e.g., joinDate must not be after terminationDate)</li>
 *   <li>Optionally enforce optimistic locking on updates (e.g., versionLock)</li>
 *   <li>Prevent disabling a client that has active dependencies (e.g., connections/resources)</li>
 * </ul>
 * </p>
 */
public interface IClientService {

    /**
     * Retrieves all active clients.
     *
     * @return a list of {@link ClientResponseDto} containing only active clients
     */
    List<ClientResponseDto> getAllClients();

    /**
     * Searches clients using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filter} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code ClientSpecifications#byFilter(ClientSearchRequestDto)}.
     * </p>
     *
     * @param filter optional search criteria (for example, q, name, email, phoneNumber,
     *               address, active, createdById, joinDateFrom, joinDateTo,
     *               terminationDateFrom, terminationDateTo)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link ClientResponseDto} matching the provided filters
     */
    Page<ClientResponseDto> searchClients(ClientSearchRequestDto filter, int page, int size);

    /**
     * Retrieves a single active client by its identifier.
     *
     * @param id identifier of the client
     * @return the corresponding {@link ClientResponseDto}
     * @throws RuntimeException if the client does not exist or is inactive
     */
    ClientResponseDto getClientById(Integer id);

    /**
     * Creates a new client.
     * <p>
     * Implementations should validate date consistency (e.g., joinDate <= terminationDate) before persisting.
     * </p>
     *
     * @param dto request payload
     * @return created client as {@link ClientResponseDto}
     * @throws RuntimeException if business validations fail (e.g., invalid date range)
     */
    ClientResponseDto createClient(ClientRequestDto dto);

    /**
     * Updates an existing active client.
     * <p>
     * Implementations usually update the persisted entity instance to preserve fields not present in the DTO
     * (e.g., {@code active}) and may enforce optimistic locking using {@code versionLock}.
     * </p>
     *
     * @param id  identifier of the client to update
     * @param dto request payload containing the new values
     * @return updated client as {@link ClientResponseDto}
     * @throws RuntimeException if the client does not exist/is inactive or if business validations fail
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if optimistic locking fails
     */
    ClientResponseDto updateClient(Integer id, ClientRequestDto dto);

    /**
     * Soft-deletes (disables) a client by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the client has active dependencies
     * (e.g., active connections or active client resources).
     * </p>
     *
     * @param id identifier of the client to disable
     * @throws RuntimeException if the client does not exist
     * @throws IllegalStateException if the client is already inactive or has active dependencies
     */
    void deleteClient(Integer id);

    List<VersionHistoryDTO> getVersionHistoryByClient(Integer clientId);

    }