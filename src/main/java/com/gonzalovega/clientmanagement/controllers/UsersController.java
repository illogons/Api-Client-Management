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
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersRequestDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersResponseDto;
import com.gonzalovega.clientmanagement.dto.UsersDto.UsersSearchDto;
import com.gonzalovega.clientmanagement.services.UserService.IUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/Users")
@Tag(name = "users" , description = "Endpoint for managing Users")
public class UsersController {

    private final IUsersService usersService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all active Users ",
            description = "Returns a list of all active Users. Inactive or deleted Users are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active Users retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UsersResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<UsersResponseDto>> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Get all active users",
            description = "Returns a list of all active users. Inactive or disabled users are not included.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of active users retrieved successfully",
                            content = @Content(schema = @Schema(implementation = UsersResponseDto[].class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<UsersResponseDto>> getAllActiveUsers() {
        return ResponseEntity.ok(usersService.getActiveUsers());
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get a user by ID",
            description = "Returns a single active user by its ID. Returns 404 if not found or inactive.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found successfully",
                            content = @Content(schema = @Schema(implementation = UsersResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UsersResponseDto> getAllUsersByUserId(
            @Parameter(description = "Numeric ID of the user to retrieve", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }


    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates and persists a new active user with the provided data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = UsersResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body or validation error"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UsersResponseDto> createNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data required to create a new user")
            @Valid @RequestBody UsersRequestDto usersRequestDto) {
        return ResponseEntity.ok(usersService.createUser(usersRequestDto));
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing active user",
            description = "Updates the data of an existing and active user by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(schema = @Schema(implementation = UsersResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID or request body"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UsersResponseDto> updateUser(
            @Parameter(description = "Numeric ID of the user to update", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated data for the user")
            @Valid @RequestBody UsersRequestDto usersRequestDto) {
        return ResponseEntity.ok(usersService.updateUpdate(id, usersRequestDto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Disable a user by ID",
            description = "Logically disables an existing active user by its ID. The record is not permanently deleted from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User disabled successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "Numeric ID of the user to disable", required = true, example = "1")
            @PathVariable Integer id) {
        usersService.deleteUser(id);
        log.info("Disabling user with id {}", id);
        return ResponseEntity.ok("User with id " + id + " disabled successfully.");
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

    public ResponseEntity<PageResponse<UsersResponseDto>> searchUsers(

            @RequestBody(required = false) UsersSearchDto filter,
            @Parameter(description = "Page number to retrieve, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "25")
            @RequestParam(defaultValue = "25") Integer size) {

        log.info("Searching type connections by filter {}, page {}, size {}", filter, page, size);
        return ResponseEntity.ok(PageResponse.from(usersService.SearchUsers(filter, page, size)));
    }





}
