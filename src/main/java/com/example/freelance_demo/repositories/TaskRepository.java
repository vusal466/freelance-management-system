package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.enums.TaskPriority;
import com.example.freelance_demo.enums.TaskStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

    /*----------------- basic lookup ----------------*/

    List<TaskEntity> findByProjectId(Long id);

    List<TaskEntity> findByStatus(TaskStatus status);

    List<TaskEntity> findByCategoryId(Long id);

    List<TaskEntity> findByPriority(TaskPriority priority);


    /*--------------- combined search ----------------*/

    List<TaskEntity> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    List<TaskEntity> findByDeadlineBetween(LocalDate start, LocalDate end);

    List<TaskEntity> findByCompletedAtBetween(LocalDateTime start, LocalDateTime end);


    /*------------ billable---------------------*/

    List<TaskEntity> findByIsBillableTrue();

    List<TaskEntity> findByIsBillableFalse();

    /*------------ pagination ------------------*/

    Page<TaskEntity> findByProjectId(Long projectId, Pageable pageable);

    Page<TaskEntity> findByStatus(TaskStatus status, Pageable pageable);

    /*------------- Optional ----------------*/

    Optional<TaskEntity> findTopByProjectIdOrderByCreatedAtDesc(Long projectId);

    /*------------- query examples -------------*/

    @Query("select t from TaskEntity t where" +
            " t.project.id = :projectId and  t.status <> :status")
    List<TaskEntity> findByProjectIdAndStatusNot(@Param("projectId") Long projectId,
                                                 @Param("status") TaskStatus status);


    @Query("select t from TaskEntity t where" +
            " t.assignedDate is null and t.status='TODO'")
    List<TaskEntity> findUnassignedTodoTask();

}
