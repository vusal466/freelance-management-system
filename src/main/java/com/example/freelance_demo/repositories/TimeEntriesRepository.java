package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.entity.TimeEntriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeEntriesRepository extends JpaRepository<TimeEntriesEntity,Long> {

    /*------------basic lookup -----------*/
    List<TimeEntriesEntity> findByDate(LocalDate date);

    List<TimeEntriesEntity> findByTaskId(Long id);

    List<TimeEntriesEntity> findByDateBetween(LocalDate start, LocalDate end);

    List<TimeEntriesEntity> findByIsBillableTrue();

    List<TimeEntriesEntity> findByIsBillableFalse();

    /*------------- task and time --------------------*/

    List<TimeEntriesEntity> findByTaskIdAndDateBetween(Long taskId, LocalDate start, LocalDate end);

    Optional<TimeEntriesEntity> findByTaskIdAndEndTimeIsNull(Long taskId);

    List<TimeEntriesEntity> findByEndTimeIsNull();

    /*-------------pagination----------------*/

    Page<TimeEntriesEntity> findByTaskId(Long id, Pageable pageable);

    Page<TimeEntriesEntity> findByDateBetween(LocalDate start, LocalDate end,Pageable pageable);

    /*----------query-------------*/

    @Query("select coalesce(sum(te.durationMinutes),0) " +
            "from TimeEntriesEntity te where te.id=:taskId and te.isBillable=true")
    int sumBillableMinutesByTask(Long taskId);


    @Query("select coalesce(sum(te.hourlyRate*te.durationMinutes/60.0),0) " +
            "from TimeEntriesEntity te where te.date between :start and :end ")
    BigDecimal totalEarnedBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("select count(te) from TimeEntriesEntity te where te.endTime is null")
    long countRunningEntries();

    List<TimeEntriesEntity> findByTaskAndBillableTrue(Long taskId);

    Long id(Long id);
}
