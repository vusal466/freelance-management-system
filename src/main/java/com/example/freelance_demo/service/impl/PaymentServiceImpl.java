package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.entity.PaymentEntity;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.PaymentMapper;
import com.example.freelance_demo.model.request.PaymentRequest;
import com.example.freelance_demo.model.response.PaymentResponse;
import com.example.freelance_demo.repositories.InvoiceRepository;
import com.example.freelance_demo.repositories.PaymentRepository;
import com.example.freelance_demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        InvoiceEntity invoice=invoiceRepository.findById(request.getInvoiceId()).orElseThrow(()->new ResourceNotFoundException("Invoice not found"));
        PaymentEntity payment = paymentMapper.toEntity(request);
        payment.setInvoice(invoice);

        PaymentEntity saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PaymentResponse update(Long id, PaymentRequest request) {
        PaymentEntity payment = paymentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Payment not found"));

        InvoiceEntity invoice = invoiceRepository.findById(request.getInvoiceId()).orElseThrow(()->new ResourceNotFoundException("invoice not found"));

        payment.setInvoice(invoice);
        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setNotes(request.getNotes());

        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!paymentRepository.existsById(id)) throw new ResourceNotFoundException("payment not found");
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentResponse getById(Long id) {
        return paymentRepository.findById(id).map(paymentMapper::toResponse).orElseThrow(()->new ResourceNotFoundException("payment not found"));
    }

    @Override
    public List<PaymentResponse> findByInvoice(Long invoiceId) {
        return paymentMapper.toResponseList(paymentRepository.findByInvoiceId(invoiceId));
    }

    @Override
    public BigDecimal totalPaid(Long invoiceId) {
        return paymentRepository.sumPaidByInvoice(invoiceId);
    }

    @Override
    public Page<PaymentResponse> listByInvoice(Long invoiceId, Pageable pageable) {
        return paymentRepository.findByInvoiceId(invoiceId,pageable).map(paymentMapper::toResponse);
    }

    @Override
    public BigDecimal calculateBalance(Long invoiceId) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        BigDecimal totalAmount = invoice.getTotalAmount();
        BigDecimal paid = paymentRepository.totalPaidByInvoice(invoiceId);

        return totalAmount.subtract(paid);

    }
}
