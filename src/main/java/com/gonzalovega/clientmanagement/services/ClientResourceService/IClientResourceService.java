package com.gonzalovega.clientmanagement.services.ClientResourceService;

import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing Client-Resource associations.
 * <p>
 * Defines CRUD and search operations for client resources. Implementations typically:
 * <ul>
 *   <li>Return only active records for read operations (soft-delete support)</li>
 *   <li>Validate referenced dependencies (Client, Resource, Version, OS, Database) before writes</li>
 *   <li>Delegate uniqueness enforcement to the database (e.g., UNIQUE(client_id, resource_id))</li>
 * </ul>
 * </p>
 */
public interface IClientResourceService {

    /**
     * Retrieves all active client-resource associations.
     *
     * @return a list of {@link ClientResourceResponseDto} containing only active records
     */
    List<ClientResourceResponseDto> getAllClientResources();

    /**
     * Searches client-resource associations using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filter} is {@code null},
     * implementations may return all records paginated.
     * </p>
     *
     * @param filter optional search criteria (for example, clientId, resourceId, databaseId,
     *               operativeSystemId, resourceVersionId, personalized, startDateFrom,
     *               startDateTo, endDateFrom, endDateTo, active)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link ClientResourceResponseDto} matching the provided filters
     */
    Page<ClientResourceResponseDto> searchClientResources(ClientResourceSearchRequestDto filter, int page, Integer size);

    /**
     * Retrieves a single active client resource by its identifier.
     *
     * @return the corresponding {@link ClientResourceResponseDto}
     * @throws RuntimeException if the client resource does not exist or is inactive
     */
    ClientResourceResponseDto getClientResourceById(
           Integer id
    );

    /**
     * Creates a new client resource.
     * <p>
     * Implementations should validate referenced dependencies and date consistency
     * (e.g., startDate must not be after endDate) before persisting.
     * </p>
     *
     * @param dto request payload
     * @return created entity as {@link ClientResourceResponseDto}
     * @throws jakarta.persistence.EntityNotFoundException if any dependency does not exist or is inactive
     * @throws IllegalStateException if a uniqueness constraint is violated (e.g., UNIQUE(client_id, resource_id))
     */
    ClientResourceResponseDto createClientResource(ClientResourceRequestDto dto);

    /**
     * Updates an existing active client resource.
     * <p>
     * Implementations usually update the persisted entity instance to preserve fields not present in the DTO
     * (e.g., {@code active}, {@code versionLock}) and may enforce optimistic locking.
     * </p>
     *
     * @param dto request payload containing the new values
     * @return updated entity as {@link ClientResourceResponseDto}
     * @throws jakarta.persistence.EntityNotFoundException if any dependency does not exist or is inactive
     * @throws RuntimeException if the entity does not exist/is inactive
     * @throws IllegalStateException if a uniqueness constraint is violated (e.g., UNIQUE(client_id, resource_id))
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if optimistic locking fails
     */
    ClientResourceResponseDto updateClientResource(Integer id, ClientResourceRequestDto dto);

    /**
     * Soft-deletes a client resource (marks it as inactive).
     *
     * @throws RuntimeException if the client resource does not exist or is already inactive
     */
    void deleteClientResource(
            Integer id);
}