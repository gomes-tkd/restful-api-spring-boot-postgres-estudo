package com.github.gomestkd.startup.controllers.docs;

import com.github.gomestkd.startup.data.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {

    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and returns a JWT Token." +
                    "The token must be used in subsequent requests with the 'Authorization: Bearer {token}' header.",
            tags = {"Authentication Endpoint"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Token Example",
                                            value = "{ \"accessToken\": \"eyJhbGciOi...\", \"refreshToken\": \"eyJhbGciOi...\" }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    ResponseEntity<?> signin(
            @Parameter(description = "User credentials (username and password)", required = true)
            AccountCredentialsDTO credentials
    );

    @Operation(
            summary = "Refresh JWT token",
            description = "Generates a new access token using a valid refresh token. " +
                    "The username must match the token's subject.",
            tags = {"Authentication Endpoint"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token refreshed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Token Example",
                                            value = "{ \"accessToken\": \"eyJhbGciOi...\", \"refreshToken\": \"eyJhbGciOi...\" }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    ResponseEntity<?> refreshToken(
            @Parameter(description = "Username linked to the refresh token", required = true, example = "john.doe")
            String username,
            @Parameter(description = "Valid refresh token", required = true, example = "eyJhbGciOi...")
            String refreshToken
    );

    @Operation(
            summary = "Register new user",
            description = "Creates a new user in the system. " +
                    "The username must be unique and the password must meet security requirements.",
            tags = {"Authentication Endpoint"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "User Example",
                                            value = "{ \"username\": \"john.doe\", \"roles\": [\"USER\"] }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            }
    )
    AccountCredentialsDTO create(
            @Parameter(description = "User credentials for registration", required = true)
            AccountCredentialsDTO credentials
    );
}
