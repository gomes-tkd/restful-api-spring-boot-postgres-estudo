package com.github.gomestkd.startup.controllers.docs;

import com.github.gomestkd.startup.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FileControllerDocs {

    @Operation(
            summary = "Upload a file",
            description = "Uploads a single file and returns metadata information.",
            tags = {"File Management"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File uploaded successfully",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid file"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    UploadFileResponseDTO uploadFile(
            @Parameter(description = "File to upload", required = true)
            MultipartFile file
    );

    @Operation(
            summary = "Upload multiple files",
            description = "Uploads multiple files and returns metadata information for each one.",
            tags = {"File Management"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files uploaded successfully",
                            content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid files"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    Set<UploadFileResponseDTO> uploadMultipleFiles(
            @Parameter(description = "Files to upload", required = true)
            MultipartFile[] files
    );

    @Operation(
            summary = "Download file",
            description = "Downloads a file by its name.",
            tags = {"File Management"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File downloaded successfully",
                            content = @Content(schema = @Schema(implementation = Resource.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "File not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Name of the file to download", example = "report.pdf")
            String fileName,
            HttpServletRequest request
    );
}
