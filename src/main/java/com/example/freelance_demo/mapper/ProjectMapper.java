package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.model.request.ProjectRequest;
import com.example.freelance_demo.model.response.ProjectResponse;
import jakarta.persistence.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "clientId",source = "client.id")
    @Mapping(target = "clientName",source = "client.fullName")
    @Mapping(target = "categoryId",source = "category.id")
    @Mapping(target = "categoryName",source = "category.categoryName")
    ProjectResponse toResponse(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status",constant = "PLANNING")
    @Mapping(target = "completedAt",ignore = true)
    @Mapping(target = "client",ignore = true)
    @Mapping(target = "category",ignore = true)
    Project toEntity(ProjectRequest request);

    List<ProjectResponse> toResponseList(List<Project> byStatus);
}
