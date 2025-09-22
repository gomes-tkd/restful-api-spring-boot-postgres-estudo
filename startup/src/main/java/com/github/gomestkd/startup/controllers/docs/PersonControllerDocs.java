package com.github.gomestkd.startup.controllers.docs;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PersonControllerDocs {

    @Operation(
            summary = "Get all people (paginated)",
            description = "Retrieves all people with pagination and sorting options.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "People retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "No people found"),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @Parameter(description = "Page number (0-based index)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Page size", example = "12")
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @Parameter(description = "Sorting direction: asc or desc", example = "asc")
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Export people",
            description = "Exports a page of people in PDF, XLSX or CSV format.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File generated successfully",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE)
                            }
                    ),
                    @ApiResponse(responseCode = "204", description = "No people found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<Resource> exportPage(
            @Parameter(description = "Page number", example = "0") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Page size", example = "12") @RequestParam(value = "size", defaultValue = "12") Integer size,
            @Parameter(description = "Sorting direction", example = "asc") @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    @Operation(
            summary = "Massive people creation",
            description = "Creates multiple people by uploading a CSV or XLSX file.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "People created successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid file format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    Set<PersonDTO> massCreation(
            @Parameter(description = "CSV or XLSX file containing people data", required = true)
            MultipartFile file
    );

    @Operation(
            summary = "Find people by first name",
            description = "Retrieves people whose first name matches the given value.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "People retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))
                    ),
                    @ApiResponse(responseCode = "204", description = "No people found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @Parameter(description = "First name to search", example = "John")
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Export person data as PDF",
            description = "Exports a specific person's data by ID as a PDF.",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "PDF generated successfully", content = @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<Resource> export(
            @Parameter(description = "ID of the person", example = "1") @PathVariable("id") Long id,
            HttpServletRequest request
    );

    @Operation(
            summary = "Get person by ID",
            description = "Retrieves details of a specific person by ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person found", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(
            summary = "Add new person",
            description = "Creates a new person by passing their JSON representation.",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Person created successfully", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(
            summary = "Update person",
            description = "Updates details of an existing person.",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person updated successfully", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(
            summary = "Disable person",
            description = "Disables a specific person by their ID (soft delete).",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Person disabled successfully", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    @Operation(
            summary = "Delete person",
            description = "Deletes a specific person by their ID (hard delete).",
            tags = {"People"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
