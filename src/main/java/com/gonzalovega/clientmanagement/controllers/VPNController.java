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
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNRequestDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNResponseDto;
import com.gonzalovega.clientmanagement.dto.VpnDto.VPNSearchDto;
import com.gonzalovega.clientmanagement.services.VpnService.IVPNService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/vpn")
@RequiredArgsConstructor
@Tag(name = "VPN", description = "Endpoints for VPN.")
public class VPNController {

    private final IVPNService vpnService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all active VPN ",
            description = "Returns a list of all active vpn. Inactive or deleted vpn are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active vpn retrieved successfully",
                            content = @Content(schema = @Schema(implementation = VPNResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<VPNResponseDto>> getAllVPN() {
        return ResponseEntity.ok(vpnService.getAllVPN());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active VPNs",
            description = "Returns a list of all active VPNs. Inactive or disabled VPNs are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active VPNs retrieved successfully",
                            content = @Content(schema = @Schema(implementation = VPNResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<VPNResponseDto>> getAllActiveVpn() {
        return ResponseEntity.ok(vpnService.getActiveVPN());
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get a VPN by ID",
            description = "Returns a single active VPN by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "VPN found successfully",
                            content = @Content(schema = @Schema(implementation = VPNResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "VPN not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<VPNResponseDto> getAllVPNByVpnId(
            @Parameter(description = "Numeric ID of the VPN to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(vpnService.getVPNUserById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new VPN",
            description = "Creates and persists a new active VPN with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "VPN created successfully",
                            content = @Content(schema = @Schema(implementation = VPNResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<VPNResponseDto> createNewVpn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new VPN")
            @Valid @RequestBody VPNRequestDto dto) {
        return ResponseEntity.ok(vpnService.createVPN(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active VPN",
            description = "Updates the data of an existing and active VPN by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "VPN updated successfully",
                            content = @Content(schema = @Schema(implementation = VPNResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "VPN not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<VPNResponseDto> updateVpn(
            @Parameter(description = "Numeric ID of the VPN to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the VPN")
            @Valid @RequestBody VPNRequestDto dto) {
        return ResponseEntity.ok(vpnService.updateVPNUpdate(id, dto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a VPN by ID",
            description = "Logically disables an existing active VPN by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "VPN disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "VPN not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteVpn(
            @Parameter(description = "Numeric ID of the VPN to disable", required = true, example = "1")
            @PathVariable Integer id) {
        vpnService.deleteVPN(id);
        log.info("Disabling VPN with id {}", id);
        return ResponseEntity.ok("VPN with id " + id + " disabled successfully.");
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

    public ResponseEntity<PageResponse<VPNResponseDto>> searchVpn(

            @RequestBody(required = false) VPNSearchDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25") Integer size) {

        log.info("Searching type connections by filter {}, page {}, size {}", filter, page, size);
        return ResponseEntity.ok(PageResponse.from(vpnService.SearchVPN(filter, page, size)));
    }

}
