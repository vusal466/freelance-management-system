package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.CategoryEntity;
import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.enums.ProjectStatus;
import com.example.freelance_demo.model.request.ProjectRequest;
import com.example.freelance_demo.model.response.ProjectResponse;
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
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectResponse toResponse(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectResponse projectResponse = new ProjectResponse();

        projectResponse.setClientId( projectClientId( project ) );
        projectResponse.setClientName( projectClientFullName( project ) );
        projectResponse.setCategoryId( projectCategoryId( project ) );
        projectResponse.setCategoryName( projectCategoryCategoryName( project ) );
        projectResponse.setId( project.getId() );
        projectResponse.setProjectName( project.getProjectName() );
        projectResponse.setDescription( project.getDescription() );
        projectResponse.setStatus( project.getStatus() );
        projectResponse.setStartDate( project.getStartDate() );
        projectResponse.setDeadline( project.getDeadline() );
        projectResponse.setBudget( project.getBudget() );
        projectResponse.setHourlyRate( project.getHourlyRate() );
        projectResponse.setTotalHoursEstimated( project.getTotalHoursEstimated() );
        projectResponse.setPriority( project.getPriority() );
        projectResponse.setColor( project.getColor() );
        projectResponse.setCompletedAt( project.getCompletedAt() );
        projectResponse.setUpdatedAt( project.getUpdatedAt() );

        return projectResponse;
    }

    @Override
    public Project toEntity(ProjectRequest request) {
        if ( request == null ) {
            return null;
        }

        Project project = new Project();

        project.setProjectName( request.getProjectName() );
        project.setDescription( request.getDescription() );
        project.setStartDate( request.getStartDate() );
        project.setDeadline( request.getDeadline() );
        project.setBudget( request.getBudget() );
        project.setHourlyRate( request.getHourlyRate() );
        project.setTotalHoursEstimated( request.getTotalHoursEstimated() );
        project.setPriority( request.getPriority() );
        project.setColor( request.getColor() );

        project.setStatus( ProjectStatus.PLANNING );

        return project;
    }

    @Override
    public List<ProjectResponse> toResponseList(List<Project> byStatus) {
        if ( byStatus == null ) {
            return null;
        }

        List<ProjectResponse> list = new ArrayList<ProjectResponse>( byStatus.size() );
        for ( Project project : byStatus ) {
            list.add( toResponse( project ) );
        }

        return list;
    }

    private Long projectClientId(Project project) {
        if ( project == null ) {
            return null;
        }
        ClientEntity client = project.getClient();
        if ( client == null ) {
            return null;
        }
        Long id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectClientFullName(Project project) {
        if ( project == null ) {
            return null;
        }
        ClientEntity client = project.getClient();
        if ( client == null ) {
            return null;
        }
        String fullName = client.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private Long projectCategoryId(Project project) {
        if ( project == null ) {
            return null;
        }
        CategoryEntity category = project.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String projectCategoryCategoryName(Project project) {
        if ( project == null ) {
            return null;
        }
        CategoryEntity category = project.getCategory();
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
