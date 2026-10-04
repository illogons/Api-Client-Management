package com.gonzalovega.clientmanagement.services.DatabaseService;

import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseRequestDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseResponseDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code Database} entities.
 * <p>
 * Defines CRUD and search operations for databases. Implementations typically:
 * <ul>
 *   <li>Return only active databases for read operations (soft-delete behavior)</li>
 *   <li>Support dynamic searching via filter DTOs (often using Specifications)</li>
 *   <li>Prevent disabling a database that is referenced by active {@code ClientResource} associations</li>
 * </ul>
 * </p>
 */
public interface IDatabaseService {

    /**
     * Retrieves all active databases.
     *
     * @return a list of {@link DatabaseResponseDto} containing only active records
     */
    List<DatabaseResponseDto> getAllDatabases();

    /**
     * Searches databases using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filter} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code DatabaseSpecifications#byFilter(DatabaseSearchRequestDto)}.
     * </p>
     *
     * @param filter optional search criteria (for example, q, name, active)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link DatabaseResponseDto} matching the provided filters
     */
    Page<DatabaseResponseDto> searchDatabases(DatabaseSearchRequestDto filter, int page, int size);

    /**
     * Retrieves a single active database by its identifier.
     *
     * @param id identifier of the database
     * @return the corresponding {@link DatabaseResponseDto}
     * @throws RuntimeException if the database does not exist or is inactive
     */
    DatabaseResponseDto getDatabaseById(Integer id);

    /**
     * Creates a new database.
     *
     * @param dto request payload
     * @return created database as {@link DatabaseResponseDto}
     */
    DatabaseResponseDto createDatabase(DatabaseRequestDto dto);

    /**
     * Updates an existing active database.
     *
     * @param id  identifier of the database to update
     * @param dto request payload containing the new values
     * @return updated database as {@link DatabaseResponseDto}
     * @throws RuntimeException if the database does not exist or is inactive
     */
    DatabaseResponseDto updateDatabase(Integer id, DatabaseRequestDto dto);

    /**
     * Soft-deletes a database by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the database is referenced by active client-resource associations.
     * </p>
     *
     * @param id identifier of the database to disable
     * @throws RuntimeException if the database does not exist/is inactive or is referenced by active client resources
     */
    void deleteDatabase(Integer id);
}