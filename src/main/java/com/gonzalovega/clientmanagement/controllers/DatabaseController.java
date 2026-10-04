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
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseRequestDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseResponseDto;
import com.gonzalovega.clientmanagement.dto.DatabaseDto.DatabaseSearchRequestDto;
import com.gonzalovega.clientmanagement.services.DatabaseService.IDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/databases")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "databases", description="Endpoints for managing databases.")
public class DatabaseController {

    private final IDatabaseService databaseService;

    @GetMapping
    @Operation(
            summary = "Get all active databases ",
            description = "Returns a list of all active databases. Inactive or deleted databases are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active databases retrieved successfully",
                            content = @Content(schema = @Schema(implementation = DatabaseResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )

    public ResponseEntity<List<DatabaseResponseDto>> getAllDatabases() {
        return ResponseEntity.ok(databaseService.getAllDatabases());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active database by ID",
            description = "Returns a single active database by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Database found successfully",
                            content = @Content(schema = @Schema(implementation = DatabaseResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Database not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<DatabaseResponseDto> getDatabaseById(
            @Parameter(description = "Numeric ID of the database to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(databaseService.getDatabaseById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new database",
            description = "Creates and persists a new active database with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Database created successfully",
                            content = @Content(schema = @Schema(implementation = DatabaseResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<DatabaseResponseDto> createDb(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new database")
            @Valid @RequestBody DatabaseRequestDto dto) {
        return ResponseEntity.ok(databaseService.createDatabase(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active database by ID",
            description = "Updates the data of an existing and active database by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Database updated successfully",
                            content = @Content(schema = @Schema(implementation = DatabaseResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Database not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<DatabaseResponseDto> updateDatabase(
            @Parameter(description = "Numeric ID of the database to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the database")
            @Valid @RequestBody DatabaseRequestDto dto) {
        return ResponseEntity.ok(databaseService.updateDatabase(id, dto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a database by ID",
            description = "Logically disables an existing active database by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Database disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Database not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteDatabase(
            @Parameter(description = "Numeric ID of the database to disable", required = true, example = "1")
            @PathVariable Integer id) {
        databaseService.deleteDatabase(id);
        return ResponseEntity.ok("Database with id " + id + " disabled successfully.");
    }

    @PostMapping( path= "/search")
    @Operation(
            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            summary = "Search databases by multiple criteria",
            description = "Returns a page of Files by Filter",
            responses = {

                    @ApiResponse(responseCode = "200", description = "Page of Legacy Files"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    public ResponseEntity<PageResponse<DatabaseResponseDto>> searchDatabases(
            @RequestBody(required = false) DatabaseSearchRequestDto filter,

            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25") Integer size) {
        return ResponseEntity.ok(PageResponse.from(databaseService.searchDatabases(filter, page, size)));
    }
}
