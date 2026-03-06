package com.example.freelance_demo.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TimeEntryResponse {
    private Long id;
    private Long taskId;
    private String taskName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private BigDecimal hourlyRate;
    private boolean billable;
    private String description;

    private BigDecimal earned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
