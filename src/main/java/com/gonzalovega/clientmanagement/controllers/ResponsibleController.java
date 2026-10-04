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
import com.gonzalovega.clientmanagement.dto.PageResponse;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleRequestDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleResponseDto;
import com.gonzalovega.clientmanagement.dto.ResponsibleDto.ResponsibleSearchDto;
import com.gonzalovega.clientmanagement.services.ResponsibleService.IResponsibleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/Responsible")
@Tag(name = "responsible", description = "Resource endpoints for managing responsibles with soft-delete.")
public class ResponsibleController {

    private final IResponsibleService responsibleServices;

    @GetMapping
    @Operation(
            summary = "Get all responsibles",
            description = "Returns a list of all responsibles. Inactive or deleted responsibles are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of responsibles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ResponsibleResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ResponsibleResponseDto>> getAllResponsibles() {
        log.info("Get all responsibles");
        return ResponseEntity.ok(responsibleServices.getAllResponsibles());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active responsibles",
            description = "Returns a list of all active responsibles. Inactive or disabled responsibles are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active responsibles retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ResponsibleResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ResponsibleResponseDto>> getAllActiveResponsibles() {
        return ResponseEntity.ok(responsibleServices.getActiveResponsibles());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a responsible by ID",
            description = "Returns a single active responsible by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsible found successfully",
                            content = @Content(schema = @Schema(implementation = ResponsibleResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsible not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResponsibleResponseDto> getAllResponsiblesByUserId(
            @Parameter(description = "Numeric ID of the responsible to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(responsibleServices.getResponsiblesById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new responsible",
            description = "Creates and persists a new active responsible with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsible created successfully",
                            content = @Content(schema = @Schema(implementation = ResponsibleResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResponsibleResponseDto> createResponsible(
            @Valid @RequestBody ResponsibleRequestDto dto) {
        log.info("Creating responsible {}", dto);
        return ResponseEntity.ok(responsibleServices.createResponsibles(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active responsible",
            description = "Updates the data of an existing and active responsible by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsible updated successfully",
                            content = @Content(schema = @Schema(implementation = ResponsibleResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Responsible not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ResponsibleResponseDto> updateResponsibles(
            @Parameter(description = "Numeric ID of the responsible to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the responsible")
            @Valid @RequestBody ResponsibleRequestDto dto) {
        log.info("Updating responsible with id {}: {}", id, dto);
        return ResponseEntity.ok(responsibleServices.updateResponsibles(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a responsible by ID",
            description = "Logically disables an existing active responsible by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Responsible disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Responsible not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteResponsibles(
            @Parameter(description = "Numeric ID of the responsible to disable", required = true, example = "1")
            @PathVariable Integer id) {
        responsibleServices.deleteResponsibles(id);
        return ResponseEntity.ok("Responsible with id " + id + " disabled successfully.");
    }

    @PostMapping(path = "/search")
    @Operation(
            summary = "Search responsibles by multiple criteria",
            description = "Returns a paginated list of responsibles filtered by the provided criteria.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Responsibles found successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PageResponse<ResponsibleResponseDto>> searchResponsibles(
            @RequestBody(required = false) ResponsibleSearchDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25") Integer size) {
        return ResponseEntity.ok(PageResponse.from(responsibleServices.SearchResponsibles(filter, page, size)));
    }
}