package com.gonzalovega.clientmanagement.services.UpdateService;

import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateRequestDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateResponseDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;
/**
 * Service contract for managing {@code Client} entities.
 * <p>
 * Defines CRUD and search operations for clients. Implementations typically:
 * <ul>
 *   <li>Return only active updates for read operations (soft-delete behavior)</li>
 *   <li>Validate business rules (e.g., joinDate must not be after terminationDate)</li>
 *   <li>Optionally enforce optimistic locking on updates (e.g., versionLock)</li>
 *   <li>Prevent disabling a client that has active dependencies (e.g., connections/resources)</li>
 * </ul>
 * </p>
 */

public interface IUpdateService {

    /**
     * Retrives all active updates
     *
     * @return a list of {@link UpdateRequestDto} containing only active clients¡
     */

    List<UpdateResponseDto> getAllUpdates();

    /**
     * Retrives one active update
     *
     * @param id
     * @return a update
     */
    UpdateResponseDto getUpdateById(Integer id);


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
     * @return a page of {@link UpdateResponseDto} matching the provided filters
     */

    Page<UpdateResponseDto> searchUpdates(UpdateSearchRequestDto filter, int page, int size);

    /**
     * Updates an existing active updates resource.
     * <p>
     * Implementations usually update the persisted entity instance to preserve fields not present in the DTO
     * (e.g., {@code active}, {@code versionLock}) and may enforce optimistic locking.
     * </p>
     *
     * @param dto request payload containing the new values
     * @return updated entity as {@link UpdateResponseDto }
     * @throws jakarta.persistence.EntityNotFoundException if any dependency does not exist or is inactive
     * @throws RuntimeException if the entity does not exist/is inactive
     * @throws IllegalStateException if a uniqueness constraint is violated (e.g., UNIQUE(client_id, resource_id))
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if optimistic locking fails
     */
    UpdateResponseDto updateUpdate(Integer id, UpdateRequestDto dto);

    /**
     * Creates a new update resource.
     * <p>
     * Implementations should validate referenced dependencies and date consistency
     * (e.g., startDate must not be after endDate) before persisting.
     * </p>
     *
     * @param dto request payload
     * @return created entity as {@link UpdateResponseDto}
     * @throws jakarta.persistence.EntityNotFoundException if any dependency does not exist or is inactive
     */
    UpdateResponseDto createUpdate(UpdateRequestDto dto);

    /**
     * Soft-deletes a client resource (marks it as inactive).
     *
     * @throws RuntimeException if the client resource does not exist or is already inactive
     */
    void deleteUpdate(Integer id);






}
