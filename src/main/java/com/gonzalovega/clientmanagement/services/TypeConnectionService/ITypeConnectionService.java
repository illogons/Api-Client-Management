package com.gonzalovega.clientmanagement.services.TypeConnectionService;

import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code TypeConnection} entities.
 * <p>
 * Defines CRUD and search operations for connection types. Implementations typically:
 * <ul>
 *   <li>Return only active type connections for read operations (soft-delete behavior)</li>
 *   <li>Support dynamic searching via filter DTOs (often using Specifications)</li>
 *   <li>Prevent disabling a type connection that is referenced by active {@code Connection} entities</li>
 * </ul>
 * </p>
 */
public interface ITypeConnectionService {

    /**
     * Retrieves all active type connections.
     *
     * @return a list of {@link TypeConnectionResponseDto} containing only active records
     */
    List<TypeConnectionResponseDto> getAllTypeConnections();

    /**
     * Retrieves a single active type connection by its identifier.
     *
     * @param id identifier of the type connection
     * @return the corresponding {@link TypeConnectionResponseDto}
     * @throws RuntimeException if the type connection does not exist or is inactive
     */
    TypeConnectionResponseDto getTypeConnectionById(Integer id);

    /**
     * Searches type connections using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code searchCriteria} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code TypeConnectionSpecifications#byFilter(TypeConnectionSearchRequestDto)}.
     * </p>
     *
     * @param searchCriteria optional search criteria (for example, q, name, active)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link TypeConnectionResponseDto} matching the provided filters
     */
    Page<TypeConnectionResponseDto> searchTypeConnections(TypeConnectionSearchRequestDto searchCriteria, int page, int size);

    /**
     * Creates a new type connection.
     *
     * @param dto request payload
     * @return created type connection as {@link TypeConnectionResponseDto}
     */
    TypeConnectionResponseDto createTypeConnection(TypeConnectionRequestDto dto);

    /**
     * Updates an existing type connection.
     *
     * @param id  identifier of the type connection to update
     * @param dto request payload containing the new values
     * @return updated type connection as {@link TypeConnectionResponseDto}
     * @throws RuntimeException if the type connection does not exist
     */
    TypeConnectionResponseDto updateTypeConnection(Integer id, TypeConnectionRequestDto dto);

    /**
     * Soft-deletes a type connection by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the type connection is referenced by active connections.
     * </p>
     *
     * @param id identifier of the type connection to disable
     * @throws RuntimeException if the type connection does not exist
     * @throws IllegalStateException if the type connection is already inactive or is used by active connections
     */
    void deleteTypeConnection(Integer id);
}