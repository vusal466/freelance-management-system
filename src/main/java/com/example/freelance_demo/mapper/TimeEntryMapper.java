package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.TimeEntriesEntity;
import com.example.freelance_demo.model.request.TimeEntryRequest;
import com.example.freelance_demo.model.response.TimeEntryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeEntryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "durationMinutes", ignore = true)
    TimeEntriesEntity toEntity(TimeEntryRequest dto);

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "taskName", source = "task.taskName")
    @Mapping(target = "earned", source = "earned")
    TimeEntryResponse toResponse(TimeEntriesEntity entity);

    List<TimeEntryResponse> toResponseList(List<TimeEntriesEntity> entities);

    default java.math.BigDecimal calculateEarned(TimeEntriesEntity entity) {
        if (entity.getHourlyRate() == null || entity.getDurationMinutes() == null
                || entity.getDurationMinutes() == 0) {
            return java.math.BigDecimal.ZERO;
        }
        return entity.getHourlyRate()
                .multiply(java.math.BigDecimal.valueOf(entity.getDurationMinutes()))
                .divide(java.math.BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
    }

}
