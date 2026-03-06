package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.ProjectRequest;
import com.example.freelance_demo.model.response.ProjectResponse;
import com.example.freelance_demo.enums.ProjectStatus;
import com.example.freelance_demo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "Project management APIs")
@SecurityRequirement(name = "BearerAuth")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Create new project", description = "Creates a new project for a client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request));
    }

    @Operation(summary = "Get all projects", description = "Retrieves all projects with pagination")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<ProjectResponse> getAll(Pageable pageable) {
        return projectService.list(pageable);
    }

    @Operation(summary = "Get project by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ProjectResponse getById(@Parameter(description = "Project ID") @PathVariable Long id) {
        return projectService.getById(id);
    }

    @Operation(summary = "Get projects by client")
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ProjectResponse> getByClient(@Parameter(description = "Client ID") @PathVariable Long clientId) {
        return projectService.findByClient(clientId);
    }

    @Operation(summary = "Get projects by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<ProjectResponse> getByStatus(@Parameter(description = "Status: PLANNING, IN_PROGRESS, COMPLETED") @PathVariable ProjectStatus status) {
        return projectService.findByStatus(status);
    }

    @Operation(summary = "Get projects by client (paginated)")
    @GetMapping("/client/{clientId}/page")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<ProjectResponse> getByClientPage(@Parameter(description = "Client ID") @PathVariable Long clientId,
                                                 Pageable pageable) {
        return projectService.listByClient(clientId, pageable);
    }

    @Operation(summary = "Update project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated"),
            @ApiResponse(responseCode = "404", description = "Project or client not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProjectResponse update(@Parameter(description = "Project ID") @PathVariable Long id,
                                  @Valid @RequestBody ProjectRequest request) {
        return projectService.update(id, request);
    }

    @Operation(summary = "Update project status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ProjectResponse updateStatus(@Parameter(description = "Project ID") @PathVariable Long id,
                                        @RequestParam ProjectStatus status) {
        return projectService.updateStatus(id, status);
    }

    @Operation(summary = "Delete project", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project deleted"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Project ID") @PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get total budget by client")
    @GetMapping("/client/{clientId}/total-budget")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public BigDecimal getTotalBudgetByClient(@Parameter(description = "Client ID") @PathVariable Long clientId) {
        return projectService.totalBudgetByClient(clientId);
    }

    @Operation(summary = "Count projects by status")
    @GetMapping("/count/status")
    @PreAuthorize("hasRole('ADMIN')")
    public long countByStatus(@RequestParam ProjectStatus status) {
        return projectService.countByStatus(status);
    }
}