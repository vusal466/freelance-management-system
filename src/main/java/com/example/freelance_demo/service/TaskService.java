package com.example.freelance_demo.service;

import com.example.freelance_demo.enums.TaskStatus;
import com.example.freelance_demo.model.request.TaskRequest;
import com.example.freelance_demo.model.response.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponse create(TaskRequest request);
    TaskResponse update(Long id, TaskRequest request);
    void delete(Long id);
    TaskResponse getById(Long id);
    List<TaskResponse> findByProject(Long projectId);
    List<TaskResponse> findByStatus(TaskStatus status);
    Page<TaskResponse> listByProject(Long projectId, Pageable pageable);
    Page<TaskResponse> list(Pageable pageable);
    TaskResponse updateStatus(Long id, TaskStatus status);
}
