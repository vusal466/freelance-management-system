package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.TaskRequest;
import com.example.freelance_demo.model.response.TaskResponse;
import com.example.freelance_demo.enums.TaskStatus;
import com.example.freelance_demo.service.TaskService;
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

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "Task management APIs")
@SecurityRequirement(name = "BearerAuth")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create task", description = "Creates a new task for a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request));
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<TaskResponse> getAll(Pageable pageable) {
        return taskService.list(pageable);
    }

    @Operation(summary = "Get task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public TaskResponse getById(@Parameter(description = "Task ID") @PathVariable Long id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Get tasks by project")
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<TaskResponse> getByProject(@Parameter(description = "Project ID") @PathVariable Long projectId) {
        return taskService.findByProject(projectId);
    }

    @Operation(summary = "Get tasks by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<TaskResponse> getByStatus(@Parameter(description = "Status: TODO, IN_PROGRESS, COMPLETED") @PathVariable TaskStatus status) {
        return taskService.findByStatus(status);
    }

    @Operation(summary = "Update task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "404", description = "Task or project not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TaskResponse update(@Parameter(description = "Task ID") @PathVariable Long id,
                               @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @Operation(summary = "Update task status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public TaskResponse updateStatus(@Parameter(description = "Task ID") @PathVariable Long id,
                                     @RequestParam TaskStatus status) {
        return taskService.updateStatus(id, status);
    }

    @Operation(summary = "Delete task", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}