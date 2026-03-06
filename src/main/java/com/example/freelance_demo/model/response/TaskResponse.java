package com.example.freelance_demo.model.response;

import com.example.freelance_demo.enums.TaskPriority;
import com.example.freelance_demo.enums.TaskStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {

    private Long id;
    private String taskName;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private BigDecimal estimatedHours;
    private LocalDate deadline;
    private LocalDate assignedDate;
    private boolean billable;

    private Long projectId;
    private String projectTitle;

    private Long categoryId;
    private String categoryName;

    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
