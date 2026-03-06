package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.enums.TaskStatus;
import com.example.freelance_demo.model.request.TaskRequest;
import com.example.freelance_demo.model.response.TaskResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:24+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskEntity toEntity(TaskRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTaskName( dto.getTaskName() );
        taskEntity.setDescription( dto.getDescription() );
        taskEntity.setPriority( dto.getPriority() );
        taskEntity.setEstimatedHours( dto.getEstimatedHours() );
        taskEntity.setDeadline( dto.getDeadline() );
        taskEntity.setAssignedDate( dto.getAssignedDate() );
        taskEntity.setBillable( dto.isBillable() );

        taskEntity.setStatus( TaskStatus.TODO );

        return taskEntity;
    }

    @Override
    public TaskResponse toResponse(TaskEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setProjectId( entityProjectId( entity ) );
        taskResponse.setProjectTitle( entityProjectProjectName( entity ) );
        taskResponse.setCategoryId( entityCategoryId( entity ) );
        taskResponse.setCategoryName( entityCategoryCategoryName( entity ) );
        taskResponse.setId( entity.getId() );
        taskResponse.setTaskName( entity.getTaskName() );
        taskResponse.setDescription( entity.getDescription() );
        taskResponse.setStatus( entity.getStatus() );
        taskResponse.setPriority( entity.getPriority() );
        taskResponse.setEstimatedHours( entity.getEstimatedHours() );
        taskResponse.setDeadline( entity.getDeadline() );
        taskResponse.setAssignedDate( entity.getAssignedDate() );
        taskResponse.setBillable( entity.isBillable() );
        taskResponse.setCompletedAt( entity.getCompletedAt() );
        taskResponse.setCreatedAt( entity.getCreatedAt() );
        taskResponse.setUpdatedAt( entity.getUpdatedAt() );

        return taskResponse;
    }

    @Override
    public List<TaskResponse> toResponseList(List<TaskEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TaskResponse> list = new ArrayList<TaskResponse>( entities.size() );
        for ( TaskEntity taskEntity : entities ) {
            list.add( toResponse( taskEntity ) );
        }

        return list;
    }

    private Long entityProjectId(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }
        Project project = taskEntity.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityProjectProjectName(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }
        Project project = taskEntity.getProject();
        if ( project == null ) {
            return null;
        }
        String projectName = project.getProjectName();
        if ( projectName == null ) {
            return null;
        }
        return projectName;
    }

    private Long entityCategoryId(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }
        CategoryEntity category = taskEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCategoryCategoryName(TaskEntity taskEntity) {
        if ( taskEntity == null ) {
            return null;
        }
        CategoryEntity category = taskEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        String categoryName = category.getCategoryName();
        if ( categoryName == null ) {
            return null;
        }
        return categoryName;
    }
}
