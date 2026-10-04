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
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersRequestDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersResponseDTO;
import com.gonzalovega.clientmanagement.dto.PartnerDto.PartnersSearchDTO;
import com.gonzalovega.clientmanagement.services.PartnerService.IPartnersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/partners")
@Tag( name = "partners", description = "Endpoint for managing Partners")
public class PartnersController {

    private final IPartnersService partnersService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all active Partners ",
            description = "Returns a list of all active parterns. Inactive or deleted parterns are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active parterns retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PartnersResponseDTO[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<PartnersResponseDTO>> getAllPartners() {
        return  ResponseEntity.ok(partnersService.getAllPartners());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active partners",
            description = "Returns a list of all active partners. Inactive or disabled partners are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active partners retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PartnersResponseDTO[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<PartnersResponseDTO>> getAllActivePartners() {
        return ResponseEntity.ok(partnersService.getActivePartners());
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get a partner by ID",
            description = "Returns a single active partner by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Partner found successfully",
                            content = @Content(schema = @Schema(implementation = PartnersResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Partner not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PartnersResponseDTO> getAllPartnersByUserId(
            @Parameter(description = "Numeric ID of the partner to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(partnersService.getPartnersById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new partner",
            description = "Creates and persists a new active partner with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Partner created successfully",
                            content = @Content(schema = @Schema(implementation = PartnersResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PartnersResponseDTO> createPartners(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new partner")
            @Valid @RequestBody PartnersRequestDTO dto) {
        log.info("Creating partner {}", dto);
        return ResponseEntity.ok(partnersService.createPartners(dto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active partner",
            description = "Updates the data of an existing and active partner by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Partner updated successfully",
                            content = @Content(schema = @Schema(implementation = PartnersResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Partner not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<PartnersResponseDTO> updatePartners(
            @Parameter(description = "Numeric ID of the partner to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the partner")
            @Valid @RequestBody PartnersRequestDTO dto) {
        log.info("Updating partner with id {}: {}", id, dto);
        return ResponseEntity.ok(partnersService.updatePartners(id, dto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable an existing active partner by ID",
            description = "Logically disables an existing active partner by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Partner disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "Partner not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public void deletePartners(
            @Parameter(description = "Numeric ID of the partner to disable", required = true, example = "1")
            @PathVariable Integer id) {
        log.info("Disabling partner with id {}", id);
        partnersService.deletePartners(id);
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

    public ResponseEntity<PageResponse<PartnersResponseDTO>> searchPartners(
            @RequestBody(required = false) PartnersSearchDTO filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25" ) Integer size
    ) {
        return ResponseEntity.ok(PageResponse.from(partnersService.SearchPartners(filter, page, size)));
    }



}
