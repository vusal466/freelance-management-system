package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.enums.TaskStatus;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.TaskMapper;
import com.example.freelance_demo.model.request.TaskRequest;
import com.example.freelance_demo.model.response.TaskResponse;
import com.example.freelance_demo.repositories.CategoryRepository;
import com.example.freelance_demo.repositories.ProjectRepository;
import com.example.freelance_demo.repositories.TaskRepository;

import com.example.freelance_demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public TaskResponse create(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        TaskEntity task = taskMapper.toEntity(request);
        task.setProject(project);

        if (request.getCategoryId() != null) {
            task.setCategory(categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }

        task.setStatus(TaskStatus.TODO);
        TaskEntity saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        task.setProject(project);
        task.setTaskName(request.getTaskName());
        task.setDescription(request.getDescription());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setDeadline(request.getDeadline());
        task.setAssignedDate(request.getAssignedDate());
        task.setPriority(request.getPriority());
        task.setBillable(request.isBillable());

        if (request.getCategoryId() != null) {
            task.setCategory(categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        } else {
            task.setCategory(null);
        }

        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id))
            throw new ResourceNotFoundException("Task not found");
        taskRepository.deleteById(id);
    }

    @Override
    public TaskResponse getById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public List<TaskResponse> findByProject(Long projectId) {
        return taskMapper.toResponseList(taskRepository.findByProjectId(projectId));
    }

    @Override
    public List<TaskResponse> findByStatus(TaskStatus status) {
        return taskMapper.toResponseList(taskRepository.findByStatus(status));
    }

    @Override
    public Page<TaskResponse> listByProject(Long projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    public Page<TaskResponse> list(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    @Transactional
    public TaskResponse updateStatus(Long id, TaskStatus status) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        validateStatusTransition(task.getStatus(), status);

        task.setStatus(status);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        boolean valid = switch (current) {
            case TODO -> next == TaskStatus.IN_PROGRESS || next == TaskStatus.CANCELLED;
            case IN_PROGRESS -> next == TaskStatus.COMPLETED || next == TaskStatus.CANCELLED || next == TaskStatus.TODO;
            case COMPLETED, CANCELLED -> false;
            default -> true;
        };

        if (!valid) {
            throw new IllegalStateException(
                    String.format("Invalid status transition: %s → %s", current, next));
        }
    }

}
