package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    boolean existsByProjectName(String projectName);

    Optional<Project> findByProjectName(String projectName);
    List<Project> findByClientId(Long clientId);
    List<Project> findByStatus(ProjectStatus status);

    @Query("select p from Project p where p.createdAt between :start and :end")
    List<Project> findBetween(@Param("start")LocalDate start, @Param("end") LocalDate end);


    @Query("select p from Project p where p.startDate>=:since")
    List<Project> findRecent(@Param("since") LocalDate since);

    Page<Project> findByClientId(Long ClienId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.budget), 0) FROM Project p WHERE p.client.id = :clientId")
    BigDecimal sumBudgetByClientId(@Param("clientId") Long clientId);

    long countByStatus(ProjectStatus status);
}
