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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.dto.*;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.TypeConnectionDto.TypeConnectionSearchRequestDto;
import com.gonzalovega.clientmanagement.services.TypeConnectionService.ITypeConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/type-connections")
@AllArgsConstructor
@Slf4j
@Tag(name = "type-connections", description = "Endpoints for managing type connections, CRUD operations with database interactions.")
public class TypeConnectionController {

    private final ITypeConnectionService typeConnectionService;

    @GetMapping
    @Operation(
            summary = "Get all active TypeConnections ",
            description = "Returns a list of all active Type Connections. Inactive or deleted  Type Connections are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active  Type Connections retrieved successfully",
                            content = @Content(schema = @Schema(implementation = TypeConnectionResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<TypeConnectionResponseDto>> getAllTypeConnections() {
        log.info("Fetching all type connections");
        return ResponseEntity.ok(typeConnectionService.getAllTypeConnections());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active type connection by ID",
            description = "Returns a single active type connection by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Type connection found successfully",
                            content = @Content(schema = @Schema(implementation = TypeConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Type connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<TypeConnectionResponseDto> getTypeConnectionById(
            @Parameter(description = "Numeric ID of the type connection to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Fetching type connection by id {}", id);
        return ResponseEntity.ok(typeConnectionService.getTypeConnectionById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new type connection",
            description = "Creates and persists a new active type connection with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Type connection created successfully",
                            content = @Content(schema = @Schema(implementation = TypeConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<TypeConnectionResponseDto> createTypeConnection(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new type connection")
            @Valid @RequestBody TypeConnectionRequestDto dto) {
        log.info("Creating type connection {}", dto);
        return ResponseEntity.ok(typeConnectionService.createTypeConnection(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active type connection",
            description = "Updates the data of an existing and active type connection by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Type connection updated successfully",
                            content = @Content(schema = @Schema(implementation = TypeConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Type connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<TypeConnectionResponseDto> updateTypeConnection(
            @Parameter(description = "Numeric ID of the type connection to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the type connection")
            @Valid @RequestBody TypeConnectionRequestDto dto) {
        log.info("Updating type connection with id {} and data {}", id, dto);
        return ResponseEntity.ok(typeConnectionService.updateTypeConnection(id, dto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a type connection by ID",
            description = "Logically disables an existing active type connection by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Type connection disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Type connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteTypeConnection(
            @Parameter(description = "Numeric ID of the type connection to disable", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Disabling type connection with id {}", id);
        typeConnectionService.deleteTypeConnection(id);
        return ResponseEntity.ok("Type connection with id " + id + " disabled successfully.");
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

    public ResponseEntity<PageResponse<TypeConnectionResponseDto>> searchTypeConnections(
            @RequestBody(required = false) TypeConnectionSearchRequestDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size) {

        log.info("Searching type connections by filter {}, page {}, size {}", filter, page, size);
        return ResponseEntity.ok(PageResponse.from(typeConnectionService.searchTypeConnections(filter, page, size)));
    }
}