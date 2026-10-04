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
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemRequestDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemResponseDto;
import com.gonzalovega.clientmanagement.dto.OperativeSystemDto.OperativeSystemSearchRequestDto;
import com.gonzalovega.clientmanagement.services.OperativeSystemService.IOperativeSystemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/operative-systems")
@Tag(name = "operative-systems", description = "Endpoints for managing operative systems (DB-backed, soft-delete).")
public class OperativeSystemController {

    private final IOperativeSystemService operativeSystemService;

    @GetMapping
    @Operation(
            summary = "Get all active OperativeSystem ",
            description = "Returns a list of all Operative System. Inactive or deleted Operative System are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Operative System retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OperativeSystemResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<OperativeSystemResponseDto>> getAllOperativeSystems() {
        log.info("Fetching all active operative systems");
        return ResponseEntity.ok(operativeSystemService.getAllOperativeSystems());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active operative system by ID",
            description = "Returns a single active operative system by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operative system found successfully",
                            content = @Content(schema = @Schema(implementation = OperativeSystemResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Operative system not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<OperativeSystemResponseDto> getOperativeSystemById(
            @Parameter(description = "Numeric ID of the operative system to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(operativeSystemService.getOperativeSystemById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new operative system",
            description = "Creates and persists a new active operative system with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Operative system created successfully",
                            content = @Content(schema = @Schema(implementation = OperativeSystemResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<OperativeSystemResponseDto> createOperativeSystem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new operative system")
            @Valid @RequestBody OperativeSystemRequestDto dto) {
        log.info("Creating a new operative system: {}", dto);
        return ResponseEntity.ok(operativeSystemService.createOperativeSystem(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active operative system",
            description = "Updates the data of an existing and active operative system by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operative system updated successfully",
                            content = @Content(schema = @Schema(implementation = OperativeSystemResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Operative system not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<OperativeSystemResponseDto> updateOperativeSystem(
            @Parameter(description = "Numeric ID of the operative system to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the operative system")
            @Valid @RequestBody OperativeSystemRequestDto dto) {
        return ResponseEntity.ok(operativeSystemService.updateOperativeSystem(id, dto));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(

            summary = "Disable an operative system by ID",
            description = "Logically disables an existing active operative system by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Operative system disabled successfully, no content returned"),
                    @ApiResponse(responseCode = "404", description = "Operative system not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteOperativeSystem(
            @Parameter(description = "Numeric ID of the operative system to disable", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Disabling operative system with id {}", id);
        operativeSystemService.deleteOperativeSystem(id);
        return ResponseEntity.ok("Operative system with id " + id + " disabled successfully.");
    }

    @PostMapping( path= "/search")
    @Operation(

            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),

            responses = {@ApiResponse(responseCode = "200", description = "Page of Legacy Files"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    public ResponseEntity<PageResponse<OperativeSystemResponseDto>> searchOperativeSystems(
            @RequestBody(required = false) OperativeSystemSearchRequestDto filter,

            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25") Integer size) {
        return ResponseEntity.ok(
                PageResponse.from(operativeSystemService.searchOperativeSystems(filter, page, size))
        );
    }
}