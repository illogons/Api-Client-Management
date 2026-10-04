package com.gonzalovega.clientmanagement.services.ResourceService;

import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceResponseDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceSearchRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service contract for managing {@code Resource} entities.
 * <p>
 * Defines CRUD and search operations for resources. Implementations typically:
 * <ul>
 *   <li>Return only active resources for read operations (soft-delete behavior)</li>
 *   <li>Support dynamic searching via filter DTOs (often using Specifications)</li>
 *   <li>Prevent disabling a resource that is referenced by active {@code ClientResource} associations</li>
 * </ul>
 * </p>
 */
public interface IResourceService {

    /**
     * Retrieves all active resources.
     *
     * @return a list of {@link ResourceResponseDto} containing only active records
     */
    List<ResourceResponseDto> getAllResources();

    /**
     * Searches resources using dynamic filters with pagination.
     * <p>
     * All filter fields are optional. When {@code filter} is {@code null},
     * implementations may return all records paginated.
     * Filtering is commonly delegated to a specification such as
     * {@code ResourceSpecifications#byFilter(ResourceSearchRequestDto)}.
     * </p>
     *
     * @param filter optional search criteria (for example, q, name, description,
     *               typeApp, launchDateFrom, launchDateTo, active)
     * @param page zero-based page index
     * @param size page size
     * @return a page of {@link ResourceResponseDto} matching the provided filters
     */
    Page<ResourceResponseDto> searchResources(ResourceSearchRequestDto filter, int page, int size);

    /**
     * Retrieves a single active resource by its identifier.
     *
     * @param id identifier of the resource
     * @return the corresponding {@link ResourceResponseDto}
     * @throws RuntimeException if the resource does not exist or is inactive
     */
    ResourceResponseDto getResourceById(Integer id);

    /**
     * Creates a new resource.
     *
     * @param dto request payload
     * @return created resource as {@link ResourceResponseDto}
     */
    ResourceResponseDto createResource(ResourceRequestDto dto);

    /**
     * Updates an existing active resource.
     *
     * @param id  identifier of the resource to update
     * @param dto request payload containing the new values
     * @return updated resource as {@link ResourceResponseDto}
     * @throws RuntimeException if the resource does not exist or is inactive
     */
    ResourceResponseDto updateResource(Integer id, ResourceRequestDto dto);

    /**
     * Soft-deletes a resource by marking it as inactive.
     * <p>
     * Implementations may prevent disabling when the resource is referenced by active client-resource associations.
     * </p>
     *
     * @param id identifier of the resource to disable
     * @throws RuntimeException if the resource does not exist/is inactive or is referenced by active client resources
     */
    void deleteResource(Integer id);
}