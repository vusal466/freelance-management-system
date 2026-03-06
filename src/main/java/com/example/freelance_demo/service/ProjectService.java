package com.example.freelance_demo.service;

import com.example.freelance_demo.enums.ProjectStatus;
import com.example.freelance_demo.model.request.ProjectRequest;
import com.example.freelance_demo.model.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    ProjectResponse update(Long id,ProjectRequest request);
    void delete(Long id);
    ProjectResponse getById(Long id);
    List<ProjectResponse> findByClient(Long clientId);
    List<ProjectResponse> findByStatus(ProjectStatus status);
    Page<ProjectResponse> listByClient(Long clientId, Pageable pageable);
    Page<ProjectResponse> list(Pageable pageable);
    ProjectResponse updateStatus(Long id, ProjectStatus status);
    BigDecimal totalBudgetByClient(Long clientId);
    long countByStatus(ProjectStatus status);
}
