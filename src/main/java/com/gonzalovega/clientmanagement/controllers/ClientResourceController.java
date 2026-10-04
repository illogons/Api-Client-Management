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
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientResourceDto.ClientResourceSearchRequestDto;
import com.gonzalovega.clientmanagement.services.ClientResourceService.IClientResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/client-resources")
@RequiredArgsConstructor
@Tag(name = "client-resources", description = "Endpoints for composite-id resources.")
public class ClientResourceController {



    private final IClientResourceService clientResourceService;

    @GetMapping
    @Operation(
            summary = "Get all active Clients Resources",
            description = "Returns a list of all active Clients resources. Inactive or deleted client resources are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active clients resources retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ClientResourceResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ClientResourceResponseDto>> getAllClientResources() {
        return ResponseEntity.ok(clientResourceService.getAllClientResources());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active client resource by ID",
            description = "Returns a single active client resource by its composite ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client resource found successfully",
                            content = @Content(schema = @Schema(implementation = ClientResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResourceResponseDto> getClientResourceById(
            @Parameter(description = "Numeric ID of the client resource to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(clientResourceService.getClientResourceById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new resource association",
            description = "Creates and persists a new association between a client and a resource with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client resource association created successfully",
                            content = @Content(schema = @Schema(implementation = ClientResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResourceResponseDto> createClientResource(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new client resource association")
            @Valid @RequestBody ClientResourceRequestDto dto) {
        return new ResponseEntity<>(clientResourceService.createClientResource(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing resource association",
            description = "Updates the data of an existing and active client resource association by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client resource association updated successfully",
                            content = @Content(schema = @Schema(implementation = ClientResourceResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResourceResponseDto> updateClientResource(
            @Parameter(description = "Numeric ID of the client resource to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the client resource association")
            @Valid @RequestBody ClientResourceRequestDto dto) {
        return ResponseEntity.ok(clientResourceService.updateClientResource(
               id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deactivate a resource association by ID",
            description = "Logically deactivates an existing client resource association by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Client resource deactivated successfully, no content returned"),
                    @ApiResponse(responseCode = "404", description = "Client resource not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Void> deleteClientResource(
            @Parameter(description = "Numeric ID of the client resource to deactivate", required = true, example = "1")
            @PathVariable Integer id) {
        clientResourceService.deleteClientResource(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping( path= "/search")
    @Operation(

            summary = "Search client-resources by multiple criteria",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            description = "Returns a page of Files by Filter",
            responses = {
                    @ApiResponse(responseCode = "200",
                                 description = "Page of Legacy Files"
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )

    public ResponseEntity<PageResponse<ClientResourceResponseDto>> searchClientResources(
            @RequestBody(required = false) ClientResourceSearchRequestDto filter,

            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "")
            @RequestParam(required = false ) Integer size
    ) {

        if (size == null) {
            int total = (int) clientResourceService.searchClientResources(filter, 0, 1).getTotalElements();
            size = total == 0 ? 1 : total;
        }

        return ResponseEntity.ok(
                PageResponse.from(clientResourceService.searchClientResources(filter, page, size))
        );
    }
}
