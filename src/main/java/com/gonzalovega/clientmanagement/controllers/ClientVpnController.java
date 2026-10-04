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
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnRequestDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnResponseDto;
import com.gonzalovega.clientmanagement.dto.ClientVpnDto.ClientVpnSearchDto;
import com.gonzalovega.clientmanagement.services.ClientVpnService.IClientVpnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/client-vpn")
@RequiredArgsConstructor
@Tag(name = "client-VPN", description = "Endpoints for CLIENT-VPN.")
public class ClientVpnController {

    private final IClientVpnService  clientVpnService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all active Client Vpn ",
            description = "Returns a list of all active Client Vpn. Inactive or deleted Client Vpn are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Client Vpn retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ClientVpnResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ClientVpnResponseDto>> getAllClientVpn() {
        return ResponseEntity.ok(clientVpnService.getAllCV());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active Client VPN ",
            description = "Returns a list of all active Client VPN . Inactive or disabled  are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Client VPN  retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ClientVpnResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ClientVpnResponseDto>> getAllActiveClientVpn() {
        return ResponseEntity.ok(clientVpnService.getActiveCV());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get all active Client VPN ",
            description = "Returns a list of all active Client VPN . Inactive or disabled  are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Client VPN  retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ClientVpnResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientVpnResponseDto> getAllClientVpnByClientVpnId(
            @Parameter(description = "Numeric ID of the Client VPN  to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(clientVpnService.getCVById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new Client VPN ",
            description = "Creates and persists a new Client VPN  with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client VPN  created successfully",
                            content = @Content(schema = @Schema(implementation = ClientVpnResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientVpnResponseDto> createNewClientVpn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new Client VPN ")
            @RequestBody ClientVpnRequestDto dto) {
        return ResponseEntity.ok(clientVpnService.createCV(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing Client VPN ",
            description = "Updates the data of an existing and active Client VPN  by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client VPN updated successfully",
                            content = @Content(schema = @Schema(implementation = ClientVpnResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Client VPN not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ClientVpnResponseDto> updateClientVpn(
            @Parameter(description = "Numeric ID of the Client VPN to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the Client VPN")
            @Valid @RequestBody ClientVpnRequestDto dto) {
        return ResponseEntity.ok(clientVpnService.updateCV(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a Client VPN by ID",
            description = "Logically disables an existing Client VPN  by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client VPN  disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Client VPN  not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteClientVpn(
            @Parameter(description = "Numeric ID of the Client VPN to disable", required = true, example = "1")
            @PathVariable Integer id) {
        clientVpnService.deleteVPN(id);
        log.info("delete  with id {}", id);
        return ResponseEntity.ok(" client vpn with id "  + id + " disabled successfully.");
    }

    @PostMapping(path = "/search")
    @Operation(

            extensions = @Extension(properties = @ExtensionProperty(name = "x-is-read", value = "true")),
            summary = "Search client VPN by multiple criteria",
            description = "Returns a paginated list of Client VPN  filtered by the provided criteria.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Page of Client VPN  retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PageResponse<ClientVpnResponseDto>> searchClientVpn(
            @RequestBody(required = false) ClientVpnSearchDto filter,

            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size) {
        log.info("Searching client VPN by filter {}, page {}, size {}", filter, page, size);
        return ResponseEntity.ok(PageResponse.from(clientVpnService.SearchCV(filter, page, size)));
    }

}
