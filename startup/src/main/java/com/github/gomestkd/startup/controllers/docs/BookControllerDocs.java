package com.github.gomestkd.startup.controllers.docs;

import com.github.gomestkd.startup.data.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface BookControllerDocs {

    @Operation(
            summary = "Get all books (paginated)",
            description = "Retrieves all books with pagination and sorting options.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Books retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                            )
                    ),
                    @ApiResponse(responseCode = "204", description = "No books found"),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
            @Parameter(description = "Page number (0-based index)", example = "0")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Page size", example = "12")
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @Parameter(description = "Sorting direction: asc or desc", example = "asc")
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(
            summary = "Get book by ID",
            description = "Retrieves details of a specific book by its ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book found",
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Book not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    BookDTO findById(
            @Parameter(description = "ID of the book", required = true, example = "1")
            @PathVariable("id") Long id
    );

    @Operation(
            summary = "Add new book",
            description = "Creates a new book by passing its JSON representation.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book created successfully",
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid book data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    BookDTO create(
            @Parameter(description = "Book data", required = true)
            @RequestBody BookDTO book
    );

    @Operation(
            summary = "Update book",
            description = "Updates the details of an existing book.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book updated successfully",
                            content = @Content(schema = @Schema(implementation = BookDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Book not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    BookDTO update(
            @Parameter(description = "Updated book data", required = true)
            @RequestBody BookDTO book
    );

    @Operation(
            summary = "Delete book",
            description = "Deletes a book by its ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    ResponseEntity<?> delete(
            @Parameter(description = "ID of the book", required = true, example = "1")
            @PathVariable("id") Long id
    );
}