package com.github.gomestkd.startup.controllers.docs;

import com.github.gomestkd.startup.data.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Email Management", description = "Endpoints for sending emails")
public interface EmailControllerDocs {

    @Operation(
            summary = "Send e-mail",
            description = "Sends a simple email with subject and body.",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "E-mail sent successfully",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Email sent successfully to john.doe@example.com")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid e-mail request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<String> sendEmail(
            @Parameter(description = "E-mail request data", required = true)
            EmailRequestDTO emailRequestDTO
    );

    @Operation(
            summary = "Send e-mail with attachment",
            description = "Sends an email with subject, body and file attachment.",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "E-mail with attachment sent successfully",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "Email with attachment sent successfully to john.doe@example.com")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid e-mail request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<String> sendEmailWithAttachment(
            @Parameter(description = "E-mail request JSON", example = "{ \"to\": \"john.doe@example.com\", \"subject\": \"Report\", \"body\": \"Attached file.\" }")
            String emailRequestJson,
            @Parameter(description = "Attachment file", required = true)
            MultipartFile multipartFile
    );
}