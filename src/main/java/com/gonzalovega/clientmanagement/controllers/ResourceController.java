package com.gonzalovega.clientmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.dto.*;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceResponseDto;
import com.gonzalovega.clientmanagement.dto.ResourceDto.ResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.services.ResourceService.IResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/resources")
@Tag(name = "resources", description = "Resource endpoints for managing resources with soft-delete.")
public class ResourceController {

    private final IResourceService resourceService;

    @GetMapping
    @Operation(
            summary = "Get all active Resources ",
            description = "Returns a list of all active Resources. Inactive or deleted Resources are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Resources retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ResourceResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ResourceResponseDto>> getAllResources() {
        log.info("Get all resources");
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active resource by ID",
            description = "Returns a single active resource by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resource found successfully",
                            content = @Content(schema = @Schema(implementation = ResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResourceResponseDto> getResourceById(
            @Parameter(description = "Numeric ID of the resource to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Get resource by id {}", id);
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new resource",
            description = "Creates and persists a new active resource with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Resource created successfully",
                            content = @Content(schema = @Schema(implementation = ResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResourceResponseDto> createResource(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new resource")
            @Valid @RequestBody ResourceRequestDto dto) {
        log.info("Create resource {}", dto);
        return ResponseEntity.ok(resourceService.createResource(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active resource",
            description = "Updates the data of an existing and active resource by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resource updated successfully",
                            content = @Content(schema = @Schema(implementation = ResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResourceResponseDto> updateResource(
            @Parameter(description = "Numeric ID of the resource to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the resource")
            @Valid @RequestBody ResourceRequestDto dto) {
        log.info("Update resource {}", dto);
        return ResponseEntity.ok(resourceService.updateResource(id, dto));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Disable a resource by ID",
            description = "Logically disables an existing active resource by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resource disabled successfully, no content returned"),
                    @ApiResponse(responseCode = "404", description = "Resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteResource(
            @Parameter(description = "Numeric ID of the resource to disable", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Disabling resource with id {}", id);
        resourceService.deleteResource(id);
        return ResponseEntity.ok("Resource with id " + id + " disabled successfully.");
    }

    @PostMapping( path= "/search")
    @Operation(

            description = "Returns a page of Files by Filter",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            responses = {@ApiResponse(responseCode = "200", description = "Page of Legacy Files"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    public ResponseEntity<PageResponse<ResourceResponseDto>> searchResources(

            @RequestBody(required = false) ResourceSearchRequestDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size
    ) {
        return ResponseEntity.ok(PageResponse.from(resourceService.searchResources(filter, page, size)));
    }
}