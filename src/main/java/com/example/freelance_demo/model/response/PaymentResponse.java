package com.example.freelance_demo.model.response;
import com.example.freelance_demo.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long invoiceId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String referenceNumber;
    private String notes;
    private LocalDateTime createdAt;
}
