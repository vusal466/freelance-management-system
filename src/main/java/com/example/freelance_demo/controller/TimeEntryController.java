package com.example.freelance_demo.controller;

import com.example.freelance_demo.model.request.TimeEntryRequest;
import com.example.freelance_demo.model.response.TimeEntryResponse;
import com.example.freelance_demo.service.TimeEntryService;
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
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
@Tag(name = "Time Entry", description = "Time tracking APIs")
@SecurityRequirement(name = "BearerAuth")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @Operation(summary = "Start time tracking", description = "Creates a new time entry (start timer)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Time entry started"),
            @ApiResponse(responseCode = "400", description = "Already running entry exists"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryResponse> create(@Valid @RequestBody TimeEntryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntryService.create(request));
    }

    @Operation(summary = "Get time entry by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time entry found"),
            @ApiResponse(responseCode = "404", description = "Time entry not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public TimeEntryResponse getById(@Parameter(description = "Time Entry ID") @PathVariable Long id) {
        return timeEntryService.getById(id);
    }

    @Operation(summary = "Get time entries by task")
    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<TimeEntryResponse> getByTask(@Parameter(description = "Task ID") @PathVariable Long taskId) {
        return timeEntryService.findByTask(taskId);
    }

    @Operation(summary = "Get billable time entries by task")
    @GetMapping("/task/{taskId}/billable")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<TimeEntryResponse> getBillableByTask(@Parameter(description = "Task ID") @PathVariable Long taskId) {
        return timeEntryService.findByTaskAndBillable(taskId);
    }

    @Operation(summary = "Get time entries by task (paginated)")
    @GetMapping("/task/{taskId}/page")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Page<TimeEntryResponse> getByTaskPage(@Parameter(description = "Task ID") @PathVariable Long taskId,
                                                 Pageable pageable) {
        return timeEntryService.listByTask(taskId, pageable);
    }

    @Operation(summary = "Get running time entry for task")
    @GetMapping("/task/{taskId}/running")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public TimeEntryResponse getRunning(@Parameter(description = "Task ID") @PathVariable Long taskId) {
        return timeEntryService.findRunningByTask(taskId);
    }

    @Operation(summary = "Get all running time entries (ADMIN)")
    @GetMapping("/running")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TimeEntryResponse> getAllRunning() {
        return timeEntryService.findAllRunning();
    }

    @Operation(summary = "Get total billable minutes for task")
    @GetMapping("/task/{taskId}/total-minutes")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public Integer getTotalBillableMinutes(@Parameter(description = "Task ID") @PathVariable Long taskId) {
        return timeEntryService.totalBillableMinutes(taskId);
    }

    @Operation(summary = "Update time entry", description = "Also used to stop timer (set endTime)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time entry updated"),
            @ApiResponse(responseCode = "404", description = "Time entry not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public TimeEntryResponse update(@Parameter(description = "Time Entry ID") @PathVariable Long id,
                                    @Valid @RequestBody TimeEntryRequest request) {
        return timeEntryService.update(id, request);
    }

    @Operation(summary = "Stop timer", description = "Quick endpoint to stop running timer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Timer stopped"),
            @ApiResponse(responseCode = "404", description = "No running timer found")
    })
    @PostMapping("/{id}/stop")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public TimeEntryResponse stopTimer(@Parameter(description = "Time Entry ID") @PathVariable Long id) {
        return timeEntryService.stopTimer(id);
    }

    @Operation(summary = "Delete time entry", description = "ADMIN only")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Time entry deleted"),
            @ApiResponse(responseCode = "404", description = "Time entry not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Time Entry ID") @PathVariable Long id) {
        timeEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
