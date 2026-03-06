package com.example.freelance_demo.service;

import com.example.freelance_demo.model.request.TimeEntryRequest;
import com.example.freelance_demo.model.response.TimeEntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TimeEntryService {
    TimeEntryResponse create(TimeEntryRequest request);
    TimeEntryResponse update(Long id, TimeEntryRequest request);
    void delete(Long id);
    TimeEntryResponse getById(Long id);
    List<TimeEntryResponse> findByTask(Long taskId);
    List<TimeEntryResponse> findByTaskAndBillable(Long taskId);
    Page<TimeEntryResponse> listByTask(Long taskId, Pageable pageable);
    Integer totalBillableMinutes(Long taskId);

    TimeEntryResponse findRunningByTask(Long taskId);
    List<TimeEntryResponse> findAllRunning();

    TimeEntryResponse stopTimer(Long id);
}
