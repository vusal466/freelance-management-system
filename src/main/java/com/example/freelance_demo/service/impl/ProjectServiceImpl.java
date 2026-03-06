package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.enums.ProjectStatus;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.ProjectMapper;
import com.example.freelance_demo.model.request.ProjectRequest;
import com.example.freelance_demo.model.response.ProjectResponse;
import com.example.freelance_demo.repositories.CategoryRepository;
import com.example.freelance_demo.repositories.ClientRepository;
import com.example.freelance_demo.repositories.ProjectRepository;
import com.example.freelance_demo.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(()->new ResourceNotFoundException("Client not found"));

        Project project = projectMapper.toEntity(request);
        project.setClient(client);
        if(request.getCategoryId()!=null){
            project.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(()->new ResourceNotFoundException("Category not found")));
        }
        project.setStatus(ProjectStatus.PLANNING);
        Project saved = projectRepository.save(project);
        return projectMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("project not found"));
        ClientEntity client = clientRepository.findById(request.getClientId()).orElseThrow(()-> new ResourceNotFoundException("client not found"));

        project.setClient(client);
        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setDeadline(request.getDeadline());
        project.setBudget(request.getBudget());
        project.setHourlyRate(request.getHourlyRate());
        project.setTotalHoursEstimated(request.getTotalHoursEstimated());
        project.setPriority(request.getPriority());
        project.setColor(request.getColor());

        if(request.getCategoryId()!=null) {
            project.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        }else {
            project.setCategory(null);}

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!projectRepository.existsById(id)) throw  new ResourceNotFoundException("Project not found");
        projectRepository.deleteById(id);
    }


    @Override
    public ProjectResponse getById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toResponse)
                .orElseThrow(()-> new ResourceNotFoundException("project not found"));
    }

    @Override
    public List<ProjectResponse> findByClient(Long clientId) {
        return projectRepository.findByClientId(clientId).stream().map(projectMapper::toResponse).toList();
    }

    @Override
    public List<ProjectResponse> findByStatus(ProjectStatus status) {

        return projectMapper.toResponseList(projectRepository.findByStatus(status));
    }

    @Override
    public Page<ProjectResponse> listByClient(Long clientId, Pageable pageable) {
        return projectRepository.findByClientId(clientId,pageable).map(projectMapper::toResponse);

    }

    @Override
    public Page<ProjectResponse> list(Pageable pageable) {
        return projectRepository.findAll(pageable).map(projectMapper::toResponse);
    }
    @Override
    @Transactional
    public ProjectResponse updateStatus(Long id, ProjectStatus status) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));


        validateStatusTransition(project.getStatus(), status);

        project.setStatus(status);


        if (status == ProjectStatus.COMPLETED && project.getCompletedAt() == null) {
            project.setCompletedAt(LocalDateTime.now());
        } else if (status != ProjectStatus.COMPLETED) {
            project.setCompletedAt(null);
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public BigDecimal totalBudgetByClient(Long clientId) {
        return projectRepository.sumBudgetByClientId(clientId);
    }

    @Override
    public long countByStatus(ProjectStatus status) {
        return projectRepository.countByStatus(status);
    }

    private void validateStatusTransition(ProjectStatus current, ProjectStatus next) {
        boolean valid = switch (current) {
            case PLANNING -> next == ProjectStatus.IN_PROGRESS || next == ProjectStatus.CANCELLED;
            case IN_PROGRESS ->
                    next == ProjectStatus.ON_HOLD || next == ProjectStatus.COMPLETED || next == ProjectStatus.CANCELLED;
            case ON_HOLD -> next == ProjectStatus.IN_PROGRESS || next == ProjectStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new IllegalStateException(
                    String.format("Invalid status transition: %s → %s", current, next));
        }
    }
}
