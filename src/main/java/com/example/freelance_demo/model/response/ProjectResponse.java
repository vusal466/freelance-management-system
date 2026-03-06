package com.example.freelance_demo.model.response;

import com.example.freelance_demo.enums.ProjectPriority;
import com.example.freelance_demo.enums.ProjectStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {

    private Long id;
    private String projectName;
    private String description;

    private Long clientId;
    private String clientName;

    private Long categoryId;
    private String categoryName;

    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate deadline;
    private BigDecimal budget;
    private BigDecimal hourlyRate;
    private BigDecimal totalHoursEstimated;
    private ProjectPriority priority;
    private String color;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;

}
