package com.gonzalovega.clientmanagement.services.ConnectionService;

import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code Connection} entities.
 * <p>
 * Defines CRUD and search operations for connections. Implementations typically:
 * <ul>
 *   <li>Return only active connections for read operations (soft-delete behavior)</li>
 *   <li>Validate referenced dependencies (Client and TypeConnection) ensuring they exist and are active</li>
 *   <li>Optionally enforce optimistic locking on updates (e.g., using {@code versionLock})</li>
 *   <li>Prevent disabling a connection that still has active associated files</li>
 * </ul>
 * </p>
 */
public interface IConnectionService {

    /**
     * Retrieves all active connections.
     *
     * @return a list of {@link ConnectionResponseDto} containing only active records
     */
    List<ConnectionResponseDto> getAllConnections();

    /**
     * Searches connections using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filterDto} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code ConnectionSpecifications#byFilter(ConnectionSearchRequestDto)}.
     * </p>
     *
     * @param filterDto optional search criteria (for example, q, client, typeConnectionId,
     *                  details, active, createdById, createdAtFrom, createdAtTo,
     *                  updatedAtFrom, updatedAtTo)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link ConnectionResponseDto} matching the provided filters
     */
    Page<ConnectionResponseDto> searchConnections(ConnectionSearchRequestDto filterDto, int page, int size);

    /**
     * Retrieves a single active connection by its identifier.
     *
     * @param id identifier of the connection
     * @return the corresponding {@link ConnectionResponseDto}
     * @throws RuntimeException if the connection does not exist or is inactive
     */
    ConnectionResponseDto getConnectionById(Integer id);

    /**
     * Creates a new connection.
     * <p>
     * Implementations should validate that the referenced client and type-connection exist and are active
     * before persisting.
     * </p>
     *
     * @param dto request payload
     * @return created connection as {@link ConnectionResponseDto}
     * @throws org.springframework.web.server.ResponseStatusException if referenced client/type-connection is inactive or not found
     */
    ConnectionResponseDto createConnection(ConnectionRequestDto dto);

    /**
     * Updates an existing active connection.
     * <p>
     * Implementations typically enforce optimistic locking using {@code versionLock} and validate
     * referenced client/type-connection before saving.
     * </p>
     *
     * @param id  identifier of the connection to update
     * @param dto request payload containing the new values (including {@code versionLock})
     * @return updated connection as {@link ConnectionResponseDto}
     * @throws RuntimeException if the connection does not exist/is inactive or referenced entities are not found
     * @throws org.springframework.orm.ObjectOptimisticLockingFailureException if optimistic locking fails
     */
    ConnectionResponseDto updateConnection(Integer id, ConnectionRequestDto dto);

    /**
     * Soft-deletes a connection by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the connection has active associated files.
     * </p>
     *
     * @param id identifier of the connection to disable
     * @throws org.springframework.web.server.ResponseStatusException if the connection does not exist or has active files
     */
    void deleteConnection(Integer id);
}