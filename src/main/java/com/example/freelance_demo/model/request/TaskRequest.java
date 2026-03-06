package com.example.freelance_demo.model.request;

import com.example.freelance_demo.enums.TaskPriority;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotNull
    private Long projectId;

    private Long categoryId;

    @NotBlank
    @Size(max = 255)
    private String taskName;

    private String description;

    @NotNull
    private TaskPriority priority = TaskPriority.MEDIUM;

    @DecimalMin(value = "0.01")
    private BigDecimal estimatedHours;

    private LocalDate deadline;
    private LocalDate assignedDate;

    private boolean billable = true;
}
