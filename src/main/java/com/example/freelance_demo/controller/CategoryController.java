package com.example.freelance_demo.controller;


import com.example.freelance_demo.model.request.CategoryRequest;
import com.example.freelance_demo.model.response.CategoryResponse;
import com.example.freelance_demo.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Project/Task category management")
public class CategoryController {

    private final CategoryService categoryService;

    /* ---------- CREATE ---------- */
    @Operation(summary = "Create new category", description = "Creates a new project/task category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody @Parameter(description = "Category details") CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    /* ---------- READ (all) ---------- */
    @Operation(summary = "Get all categories", description = "Retrieves list of all categories")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    /* ---------- READ (one) ---------- */
    @Operation(summary = "Get category by ID", description = "Retrieves a single category by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    /* ---------- UPDATE ---------- */
    @Operation(summary = "Update category", description = "Updates an existing category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Category name already taken")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody @Parameter(description = "Updated category details") CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    /* ---------- DELETE ---------- */
    @Operation(summary = "Delete category", description = "Deletes a category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}