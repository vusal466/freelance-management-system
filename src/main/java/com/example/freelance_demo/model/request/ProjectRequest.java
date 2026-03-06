package com.example.freelance_demo.model.request;
import com.example.freelance_demo.enums.ProjectPriority;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectRequest {
    @NotNull
    @Size(max = 120)
    private String projectName;
    private String description;

    @NotNull
    private Long clientId;

    private Long categoryId;

    @NotNull
    private LocalDate startDate;
    private LocalDate deadline;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal budget;

    @NotNull
    private BigDecimal hourlyRate;

    @NotNull
    private BigDecimal totalHoursEstimated;
    private ProjectPriority priority = ProjectPriority.MEDIUM;
    private String color;

}
