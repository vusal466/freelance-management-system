package com.example.freelance_demo.service;

import com.example.freelance_demo.model.request.PaymentRequest;
import com.example.freelance_demo.model.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    PaymentResponse update(Long id, PaymentRequest request);
    void delete(Long id);
    PaymentResponse getById(Long id);
    List<PaymentResponse> findByInvoice(Long invoiceId);
    BigDecimal totalPaid(Long invoiceId);
    Page<PaymentResponse> listByInvoice(Long invoiceId, Pageable pageable);
    BigDecimal calculateBalance(Long invoiceId);
}
