package com.example.freelance_demo.service;

import com.example.freelance_demo.enums.InvoiceStatus;
import com.example.freelance_demo.model.request.InvoiceRequest;
import com.example.freelance_demo.model.response.InvoiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceService {

    InvoiceResponse create(InvoiceRequest request);
    InvoiceResponse update(Long id, InvoiceRequest request);
    void delete(Long id);
    InvoiceResponse getById(Long id);
    Page<InvoiceResponse> list(Pageable pageable);
    Page<InvoiceResponse> findByClientId(Long clientId,Pageable pageable);

    Page<InvoiceResponse> findByStatus(InvoiceStatus status, Pageable pageable);
    Page<InvoiceResponse> findOverdue(LocalDate date, Pageable pageable);
    InvoiceResponse updateStatus(Long id, InvoiceStatus newStatus);
    BigDecimal calculateRevenue(LocalDate start, LocalDate end);
}
