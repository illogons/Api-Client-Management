package com.gonzalovega.clientmanagement.services.OperativeSystemService;

import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemRequestDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemResponseDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code OperativeSystem} entities.
 * <p>
 * Defines CRUD and search operations for operative systems. Implementations typically:
 * <ul>
 *   <li>Return only active operative systems for read operations (soft-delete behavior)</li>
 *   <li>Support dynamic searching via filter DTOs (often using Specifications)</li>
 *   <li>Prevent disabling an operative system that is referenced by active {@code ClientResource} associations</li>
 * </ul>
 * </p>
 */
public interface IOperativeSystemService {

    /**
     * Retrieves all active operative systems.
     *
     * @return a list of {@link OperativeSystemResponseDto} containing only active records
     */
    List<OperativeSystemResponseDto> getAllOperativeSystems();

    /**
     * Searches operative systems using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filter} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code OperativeSystemSpecifications#byFilter(OperativeSystemSearchRequestDto)}.
     * </p>
     *
     * @param filter optional search criteria (for example, q, name, version, active, id)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link OperativeSystemResponseDto} matching the provided filters
     */
    Page<OperativeSystemResponseDto> searchOperativeSystems(OperativeSystemSearchRequestDto filter, int page, int size);

    /**
     * Retrieves a single active operative system by its identifier.
     *
     * @param id identifier of the operative system
     * @return the corresponding {@link OperativeSystemResponseDto}
     * @throws RuntimeException if the operative system does not exist or is inactive
     */
    OperativeSystemResponseDto getOperativeSystemById(Integer id);

    /**
     * Creates a new operative system.
     *
     * @param dto request payload
     * @return created operative system as {@link OperativeSystemResponseDto}
     */
    OperativeSystemResponseDto createOperativeSystem(OperativeSystemRequestDto dto);

    /**
     * Updates an existing active operative system.
     *
     * @param id  identifier of the operative system to update
     * @param dto request payload containing the new values
     * @return updated operative system as {@link OperativeSystemResponseDto}
     * @throws RuntimeException if the operative system does not exist or is inactive
     */
    OperativeSystemResponseDto updateOperativeSystem(Integer id, OperativeSystemRequestDto dto);

    /**
     * Soft-deletes an operative system by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the operative system is referenced by active client-resource associations.
     * </p>
     *
     * @param id identifier of the operative system to disable
     * @throws RuntimeException if the operative system does not exist/is inactive or is referenced by active client resources
     */
    void deleteOperativeSystem(Integer id);
}