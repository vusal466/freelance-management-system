package com.example.freelance_demo.model.request;

import com.example.freelance_demo.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class PaymentRequest {
    @NotNull
    private Long invoiceId;

    @NotNull
    @PastOrPresent
    private LocalDate paymentDate;

    @Positive
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;

    private String referenceNumber;
    private String notes;
}
