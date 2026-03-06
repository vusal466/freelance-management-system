package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.TaskEntity;
import com.example.freelance_demo.entity.TimeEntriesEntity;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.TimeEntryMapper;
import com.example.freelance_demo.model.request.TimeEntryRequest;
import com.example.freelance_demo.model.response.TimeEntryResponse;
import com.example.freelance_demo.repositories.TaskRepository;
import com.example.freelance_demo.repositories.TimeEntriesRepository;
import com.example.freelance_demo.service.TimeEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeEntryServiceImpl implements TimeEntryService {

    private final TimeEntryMapper timeEntryMapper;
    private final TimeEntriesRepository timeEntriesRepository;
    private final TaskRepository taskRepository;



    @Override
    @Transactional
    public TimeEntryResponse create(TimeEntryRequest request) {

        TaskEntity task = taskRepository.findById(request.getTaskId()).orElseThrow(()->  new ResourceNotFoundException("Task not found"));

        Optional<TimeEntriesEntity> opt = timeEntriesRepository
                .findByTaskIdAndEndTimeIsNull(request.getTaskId())
                .stream()
                .findFirst();

        if(opt.isPresent()) throw new IllegalStateException("There is already a running time entry for this task");

        TimeEntriesEntity entry = timeEntryMapper.toEntity(request);
        entry.setTask(task);

        TimeEntriesEntity saved = timeEntriesRepository.save(entry);
        return timeEntryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TimeEntryResponse update(Long id, TimeEntryRequest request) {
        TimeEntriesEntity entry = timeEntriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time entry not found"));

        TaskEntity task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        entry.setTask(task);
        entry.setStartTime(request.getStartTime());
        entry.setEndTime(request.getEndTime());
        entry.setDescription(request.getDescription());
        entry.setBillable(request.isBillable());
        entry.setHourlyRate(request.getHourlyRate());
        return timeEntryMapper.toResponse(timeEntriesRepository.save(entry));

    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!timeEntriesRepository.existsById(id)) throw new ResourceNotFoundException("Time entry not found");
        timeEntriesRepository.deleteById(id);
    }

    @Override
    public TimeEntryResponse getById(Long id) {
        return timeEntriesRepository.findById(id)
                .map(timeEntryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Time entry not found"));
    }

    @Override
    public List<TimeEntryResponse> findByTask(Long taskId) {
        return timeEntryMapper.toResponseList(timeEntriesRepository.findByTaskId(taskId));
    }

    @Override
    public List<TimeEntryResponse> findByTaskAndBillable(Long taskId) {
        return timeEntryMapper.toResponseList(timeEntriesRepository.findByTaskAndBillableTrue(taskId));
    }

    @Override
    public Page<TimeEntryResponse> listByTask(Long taskId, Pageable pageable) {
        return timeEntriesRepository.findByTaskId(taskId,pageable).map(timeEntryMapper::toResponse);
    }

    @Override
    public Integer totalBillableMinutes(Long taskId) {
        return timeEntriesRepository.sumBillableMinutesByTask(taskId);
    }

    @Override
    public TimeEntryResponse findRunningByTask(Long taskId) {
        return timeEntriesRepository.findByTaskIdAndEndTimeIsNull(taskId)
                .map(timeEntryMapper::toResponse).orElse(null);
    }

    @Override
    public List<TimeEntryResponse> findAllRunning() {
        return timeEntryMapper.toResponseList(timeEntriesRepository.findByEndTimeIsNull());

    }

    @Override
    @Transactional
    public TimeEntryResponse stopTimer(Long id) {
        TimeEntriesEntity entry = timeEntriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time entry not found"));

        if (entry.getEndTime() != null) {
            throw new IllegalStateException("Timer already stopped");
        }

        LocalDateTime endTime = LocalDateTime.now();
        entry.setEndTime(endTime);

        long minutes = java.time.Duration.between(entry.getStartTime(), endTime).toMinutes();
        entry.setDurationMinutes((int) minutes);

        return timeEntryMapper.toResponse(timeEntriesRepository.save(entry));
    }
}
