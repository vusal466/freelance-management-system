package com.example.freelance_demo.entity;

import com.example.freelance_demo.entity.base.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "time_entries")
@ToString(exclude = "task")
@EqualsAndHashCode(callSuper = true, exclude = "task")
public class TimeEntriesEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;


    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String description;

    @Column(name = "is_billable")
    private boolean isBillable = false;

    private BigDecimal hourlyRate;

    @Column(nullable = false)
    private LocalDate date;



    @AssertTrue(message = "End time cannot be before start time!")
    private boolean isValidTime(){
        return endTime==null || startTime==null || !endTime
                .isBefore(startTime);
    }

    @PrePersist
    @PreUpdate
    private void calcDuration(){
        if (startTime!=null && endTime!=null) {
            durationMinutes=(int)java.time.Duration
                    .between(startTime,endTime).toMinutes();
        }
        else {
            durationMinutes = 0;
        }

        if(date==null && startTime !=null){
            date=startTime.toLocalDate();
        }
    }

    public BigDecimal getEarned(){
        if(!isBillable || hourlyRate==null) return BigDecimal.ZERO;

        BigDecimal hours = BigDecimal.valueOf(durationMinutes)
                .divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_UP);
        return hourlyRate.multiply(hours);
    }
}
