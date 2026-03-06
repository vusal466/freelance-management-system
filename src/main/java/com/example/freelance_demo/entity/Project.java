package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.ProjectPriority;
import com.example.freelance_demo.enums.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "projects")
@ToString(exclude = {"client","category"})
@EqualsAndHashCode(callSuper = true, exclude = {"client","category"})
public class Project extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="client_id",nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @Column(name = "project_name", nullable = false,length = 120)
    private String projectName;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status=ProjectStatus.PLANNING;

    private LocalDate startDate;
    private LocalDate deadline;

    @Column(nullable = false,precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Budget must be > 0")
    private BigDecimal budget;

    @Column(name = "hourly_rate", nullable = false,precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "total_hours_estimated", nullable = false,precision = 10, scale = 2)
    private BigDecimal totalHoursEstimated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectPriority priority = ProjectPriority.MEDIUM;

    @Column(length = 10)
    private String color;

    private LocalDateTime completedAt;

    @PreUpdate
    @PrePersist
    private void syncCompleted(){
        if (status!=ProjectStatus.COMPLETED){
            completedAt=null;
        } else if (completedAt==null) {
            completedAt = LocalDateTime.now();
        }
    }


}
