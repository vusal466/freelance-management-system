package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.entity.TimeEntriesEntity;
import com.example.freelance_demo.model.request.TimeEntryRequest;
import com.example.freelance_demo.model.response.TimeEntryResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-06T00:40:41+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class TimeEntryMapperImpl implements TimeEntryMapper {

    @Override
    public TimeEntriesEntity toEntity(TimeEntryRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TimeEntriesEntity timeEntriesEntity = new TimeEntriesEntity();

        timeEntriesEntity.setStartTime( dto.getStartTime() );
        timeEntriesEntity.setEndTime( dto.getEndTime() );
        timeEntriesEntity.setDescription( dto.getDescription() );
        timeEntriesEntity.setBillable( dto.isBillable() );
        timeEntriesEntity.setHourlyRate( dto.getHourlyRate() );

        return timeEntriesEntity;
    }

    @Override
    public TimeEntryResponse toResponse(TimeEntriesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TimeEntryResponse timeEntryResponse = new TimeEntryResponse();

        timeEntryResponse.setTaskId( entityTaskId( entity ) );
        timeEntryResponse.setTaskName( entityTaskTaskName( entity ) );
        timeEntryResponse.setEarned( entity.getEarned() );
        timeEntryResponse.setId( entity.getId() );
        timeEntryResponse.setStartTime( entity.getStartTime() );
        timeEntryResponse.setEndTime( entity.getEndTime() );
        timeEntryResponse.setDurationMinutes( entity.getDurationMinutes() );
        timeEntryResponse.setHourlyRate( entity.getHourlyRate() );
        timeEntryResponse.setBillable( entity.isBillable() );
        timeEntryResponse.setDescription( entity.getDescription() );
        timeEntryResponse.setCreatedAt( entity.getCreatedAt() );
        timeEntryResponse.setUpdatedAt( entity.getUpdatedAt() );

        return timeEntryResponse;
    }

    @Override
    public List<TimeEntryResponse> toResponseList(List<TimeEntriesEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TimeEntryResponse> list = new ArrayList<TimeEntryResponse>( entities.size() );
        for ( TimeEntriesEntity timeEntriesEntity : entities ) {
            list.add( toResponse( timeEntriesEntity ) );
        }

        return list;
    }

    private Long entityTaskId(TimeEntriesEntity timeEntriesEntity) {
        if ( timeEntriesEntity == null ) {
            return null;
        }
        TaskEntity task = timeEntriesEntity.getTask();
        if ( task == null ) {
            return null;
        }
        Long id = task.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTaskTaskName(TimeEntriesEntity timeEntriesEntity) {
        if ( timeEntriesEntity == null ) {
            return null;
        }
        TaskEntity task = timeEntriesEntity.getTask();
        if ( task == null ) {
            return null;
        }
        String taskName = task.getTaskName();
        if ( taskName == null ) {
            return null;
        }
        return taskName;
    }
}
