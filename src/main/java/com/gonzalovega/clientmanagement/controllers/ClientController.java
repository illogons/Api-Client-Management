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
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.ClientSearchRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientDto.VersionHistoryDTO;
import com.gonzalovega.clientmanagement.services.ClientService.IClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "clients", description="Endpoints for managing clients, CRUD operations with database interactions.")
public class ClientController {

    private final IClientService IClientService;

    @GetMapping
    @Operation(
            summary = "Get all active Clients",
            description = "Returns a list of all active Clients. Inactive or deleted versions are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active clients retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ClientResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ClientResponseDto>> getAllClients() {
        return ResponseEntity.ok(IClientService.getAllClients());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get an active client by ID",
            description = "Returns a single active client by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client found successfully",
                            content = @Content(schema = @Schema(implementation = ClientResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResponseDto> getClientById (
            @Parameter(description = "Numeric ID of the client to retrieve", required = true, example = "1")
            @PathVariable Integer id){
        return ResponseEntity.ok(IClientService.getClientById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new client",
            description = "Creates and persists a new active client with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client created successfully",
                            content = @Content(schema = @Schema(implementation = ClientResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResponseDto> createClient(
            @Valid @RequestBody ClientRequestDto dto){

        log.info("Creating id {}", dto);
        return ResponseEntity.ok(IClientService.createClient(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active client",
            description = "Updates the data of an existing and active client by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client updated successfully",
                            content = @Content(schema = @Schema(implementation = ClientResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientResponseDto> updateClient(
            @Parameter(description = "Numeric ID of the client to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the client")
            @Valid @RequestBody ClientRequestDto dto){

        log.info("Updating id with id {}: {}", id, dto);
        return ResponseEntity.ok(IClientService.updateClient(id, dto));
    }

    @GetMapping("/{clientId}/version-history")
    public ResponseEntity<List<VersionHistoryDTO>> getVersionHistory(
            @PathVariable Integer clientId  ) {
        return ResponseEntity.ok(IClientService.getVersionHistoryByClient(clientId));
    }

    @DeleteMapping("/{id}")
    public void deleteClient(
            @PathVariable Integer id){

        log.info("Disabling id with id {}", id);
        IClientService.deleteClient(id);
    }

    @PostMapping(path = "/search")
    @Operation(

            summary = "Search clients by multiple criteria",
            description = "Returns a paginated list of clients filtered by the provided criteria.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client found successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PageResponse<ClientResponseDto>> searchClients(

            @RequestBody(required = false) ClientSearchRequestDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25"  ) Integer size) {
        return ResponseEntity.ok(PageResponse.from(IClientService.searchClients(filter, page, size)));
    }
}