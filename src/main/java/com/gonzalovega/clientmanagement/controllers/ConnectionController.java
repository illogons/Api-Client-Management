package com.gonzalovega.clientmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gonzalovega.clientmanagement.dto.*;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionRequestDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionResponseDto;
import com.gonzalovega.clientmanagement.dto.ConnectionDto.ConnectionSearchRequestDto;
import com.gonzalovega.clientmanagement.services.ConnectionService.IConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/connections")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "connections", description = "Connection endpoints for managing connections with soft-delete (active=false).")
public class ConnectionController {

    private final IConnectionService connectionService;

    @GetMapping
    @Operation(
            summary = "Get all active connections ",
            description = "Returns a list of all active connections. Inactive or deleted connections are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active connections retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ConnectionResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ConnectionResponseDto>> getAllConnections() {
        log.info("Fetching all active connections");
        return ResponseEntity.ok(connectionService.getAllConnections());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active connection by ID",
            description = "Returns a single active connection by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connection found successfully",
                            content = @Content(schema = @Schema(implementation = ConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ConnectionResponseDto> getConnectionById(
            @Parameter(description = "Numeric ID of the connection to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(connectionService.getConnectionById(id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new connection",
            description = "Creates and persists a new active connection with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Connection created successfully",
                            content = @Content(schema = @Schema(implementation = ConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ConnectionResponseDto createConnection(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new connection")
            @Valid @RequestBody ConnectionRequestDto dto) {
        return connectionService.createConnection(dto);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active connection",
            description = "Updates the data of an existing and active connection by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Connection updated successfully",
                            content = @Content(schema = @Schema(implementation = ConnectionResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ConnectionResponseDto> updateConnection(
            @Parameter(description = "Numeric ID of the connection to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the connection")
            @Valid @RequestBody ConnectionRequestDto dto) {
        return ResponseEntity.ok(connectionService.updateConnection(id, dto));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Disable a connection by ID",
            description = "Logically disables an existing active connection by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Connection disabled successfully, no content returned"),
                    @ApiResponse(responseCode = "404", description = "Connection not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteConnection(
            @Parameter(description = "Numeric ID of the connection to disable", required = true, example = "1")
            @PathVariable Integer id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.ok("Connection with id " + id + " disabled successfully.");
    }

    @PostMapping( path= "/search")
    @Operation(
            summary = "Search connections by multiple criteria",
            description = "Returns a page of Files by Filter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of Legacy Files"),

                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }

    )

    public ResponseEntity<PageResponse<ConnectionResponseDto>> searchConnections(
            @RequestBody(required = false) ConnectionSearchRequestDto filter,

            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25"  ) Integer size) {
        return ResponseEntity.ok(PageResponse.from(connectionService.searchConnections(filter, page, size)));
    }
}