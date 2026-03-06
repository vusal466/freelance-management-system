package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.model.request.TaskRequest;
import com.example.freelance_demo.model.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.scheduling.config.Task;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "TODO")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "category", ignore = true)
    TaskEntity toEntity(TaskRequest dto);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectTitle", source = "project.projectName")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    TaskResponse toResponse(TaskEntity entity);

    List<TaskResponse> toResponseList(List<TaskEntity> entities);
}
