package com.example.freelance_demo.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TimeEntryRequest {
    @NotNull
    private Long taskId;

    @NotNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotBlank
    @Size(max = 500)
    private String description;

    private boolean billable = true;
    private BigDecimal hourlyRate;
}
