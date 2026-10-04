package com.gonzalovega.clientmanagement.services.VersionService;

import com.gonzalovega.clientmanagement.dto.VersionDto.VersionRequestDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionResponseDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code Version} entities.
 * <p>
 * Defines CRUD and search operations for resource versions. Implementations typically:
 * <ul>
 *   <li>Return only active versions for read operations (soft-delete behavior)</li>
 *   <li>Support dynamic searching via filter DTOs (often using Specifications)</li>
 *   <li>Prevent disabling a resource version that is referenced by active {@code ClientResource} associations</li>
 * </ul>
 * </p>
 */
public interface IVersionService {

     /**
      * Retrieves all active resource versions.
      *
      * @return a list of {@link VersionResponseDto} containing only active records
      */
     List<VersionResponseDto> getAllVersions();

     /**
      * Searches resource versions using dynamic filters with pagination.
      * <p>
      * All filter fields are optional. When {@code filter} is {@code null},
      * implementations may return all records paginated.
      * Filtering is commonly delegated to a specification such as
      * {@code VersionSpecifications#byFilter(VersionSearchRequestDto)}.
      * </p>
      *
      * @param filter optional search criteria (for example, q, version, launchDateFrom, launchDateTo, active)
      * @param page zero-based page index
      * @param size page size
      * @return a page of {@link VersionResponseDto} matching the provided filters
      */
     Page<VersionResponseDto> searchVersions(VersionSearchRequestDto filter, int page, int size);

     /**
      * Retrieves a single active resource version by its identifier.
      *
      * @param id identifier of the resource version
      * @return the corresponding {@link VersionResponseDto}
      * @throws RuntimeException if the resource version does not exist or is inactive
      */
     VersionResponseDto getVersionById(Integer id);

     /**
      * Creates a new resource version.
      *
      * @param dto request payload
      * @return created resource version as {@link VersionResponseDto}
      */
     VersionResponseDto createVersion(VersionRequestDto dto);

     /**
      * Updates an existing active resource version.
      *
      * @param id  identifier of the resource version to update
      * @param dto request payload containing the new values
      * @return updated resource version as {@link VersionResponseDto}
      * @throws RuntimeException if the resource version does not exist or is inactive
      */
     VersionResponseDto updateVersion(Integer id, VersionRequestDto dto);

     /**
      * Soft-deletes a resource version by marking it as inactive.
      * <p>
      * Implementations may prevent disabling when the version is referenced by active client-resource associations.
      * </p>
      *
      * @param id identifier of the resource version to disable
      * @throws RuntimeException if the resource version does not exist/is inactive or is referenced by active client resources
      */
     void deleteVersion(Integer id);
}