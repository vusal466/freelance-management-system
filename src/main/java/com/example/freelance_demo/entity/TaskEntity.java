package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import com.example.freelance_demo.enums.TaskPriority;
import com.example.freelance_demo.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="tasks")
@ToString(exclude = {"project","category"})
@EqualsAndHashCode(callSuper = true, exclude = {"project","category"})
public class TaskEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "task_name",nullable = false)
    private String taskName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "estimated_hours")
    @DecimalMin(value = "0.01", message = "Estimated hours must be positive")
    private BigDecimal estimatedHours;

    private LocalDate deadline;
    private LocalDate assignedDate;

    private LocalDateTime completedAt;

    @Column(name = "is_billable")
    private boolean isBillable = true;

    private String notes;

    @PrePersist
    @PreUpdate
    private void syncCompletedAt(){
        if (status==TaskStatus.COMPLETED){
            if (completedAt==null) completedAt=LocalDateTime.now();
        } else{
            completedAt=null;
        }
    }

    @AssertTrue(message = "Deadline cannot be before assigned date")
    private boolean isDateValid(){
        return assignedDate==null || deadline==null ||
                !deadline.isBefore(assignedDate);
    }
}
