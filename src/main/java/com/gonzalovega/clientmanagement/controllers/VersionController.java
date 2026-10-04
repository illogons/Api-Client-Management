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

import com.gonzalovega.clientmanagement.dto.VersionDto.VersionRequestDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionResponseDto;
import com.gonzalovega.clientmanagement.dto.VersionDto.VersionSearchRequestDto;
import com.gonzalovega.clientmanagement.services.VersionService.IVersionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/versions")
@RequiredArgsConstructor
@Tag(name="versions", description="Endpoints for managing resource versions.")
public class VersionController {
    private final IVersionService VersionService;

    @GetMapping
    @Operation(
            summary = "Get all active resource versions",
            description = "Returns a list of all active resource versions. Inactive or deleted versions are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active versions retrieved successfully",
                            content = @Content(schema = @Schema(implementation = VersionResponseDto[].class))// le dice al swagger que estructura tiene el body de la respuesta
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<VersionResponseDto>> getAllVersions() {
        return ResponseEntity.ok(VersionService.getAllVersions());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active resource version by ID",
            description = "Returns a single active resource version by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Version found successfully",
                            content = @Content(schema = @Schema(implementation = VersionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Version not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )    public ResponseEntity<VersionResponseDto> getVersionById(
            @Parameter(description = "Numeric ID of the version to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
       return ResponseEntity.ok(VersionService.getVersionById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new resource version",
            description = "Creates and persists a new active resource version with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Version created successfully",
                            content = @Content(schema = @Schema(implementation = VersionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<VersionResponseDto> createVersion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new version")
            @Valid @RequestBody VersionRequestDto dto) {
      return ResponseEntity.ok(VersionService.createVersion(dto));
    }
    

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active resource version",
            description = "Updates the data of an existing and active resource version by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Version updated successfully",
                            content = @Content(schema = @Schema(implementation = VersionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Version not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )

    public ResponseEntity<VersionResponseDto> updateVersion(
            @Parameter(description = "Numeric ID of the version to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the version")
            @Valid @RequestBody VersionRequestDto dto) {
       return ResponseEntity.ok(VersionService.updateVersion(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a resource version by ID",
            description = "Logically disables an existing active version by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Version disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Version not found with the given ID"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteVersion(
            @Parameter(description = "ID del VPN a eliminar", example = "42")
            @PathVariable Integer id) {
        VersionService.deleteVersion(id);
        return ResponseEntity.ok("Version with id " + id + " disabled successfully.");
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

    public ResponseEntity<PageResponse<VersionResponseDto>> searchVersions(

            @RequestBody(required = false) VersionSearchRequestDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size

    ) {
        return ResponseEntity.ok(PageResponse.from(VersionService.searchVersions(filter, page, size)));
    }
}
