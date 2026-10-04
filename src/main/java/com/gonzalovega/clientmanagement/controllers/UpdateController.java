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
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateRequestDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateResponseDto;
import com.gonzalovega.clientmanagement.dto.UpdateDto.UpdateSearchRequestDto;
import com.gonzalovega.clientmanagement.services.UpdateService.IUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/Update")
@Tag(name = "update", description = "Endspoits endpoints for managing ClientVersionupdates.")
public class UpdateController {
    private final IUpdateService updateService;

    @GetMapping
    @Operation(
            summary = "Get all active Updates ",
            description = "Returns a list of all active Update. Inactive or deleted Update are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Update retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UpdateResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<UpdateResponseDto>> getAllActiveUpdates() {
        return ResponseEntity.ok(updateService.getAllUpdates());
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active update by ID",
            description = "Returns a single active update by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update found successfully",
                            content = @Content(schema = @Schema(implementation = UpdateResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Update not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UpdateResponseDto> getUpdateById(
            @Parameter(description = "Numeric ID of the update to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(updateService.getUpdateById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new update",
            description = "Creates and persists a new active update with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update created successfully",
                            content = @Content(schema = @Schema(implementation = UpdateResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UpdateResponseDto> createUpdate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new update")
            @Valid @RequestBody UpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(updateService.createUpdate(updateRequestDto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active update",
            description = "Updates the data of an existing and active update by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update modified successfully",
                            content = @Content(schema = @Schema(implementation = UpdateResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Update not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UpdateResponseDto> updateUpdate(
            @Parameter(description = "Numeric ID of the update to modify", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the update")
            @Valid @RequestBody UpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(updateService.updateUpdate(id, updateRequestDto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable an active update by ID",
            description = "Logically disables an existing active update by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Update disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Update not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteUpdate(
            @Parameter(description = "Numeric ID of the update to disable", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Disabling update with id {}", id);
        updateService.deleteUpdate(id);
        return ResponseEntity.ok("Update with id " + id + " disabled successfully.");
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

    public ResponseEntity<PageResponse<UpdateResponseDto>> searchUpdates(
            @RequestBody(required = false) UpdateSearchRequestDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size) {
        return ResponseEntity.ok(PageResponse.from(updateService.searchUpdates(filter, page, size)));
    }
}
