package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.entity.InvoiceLineEntity;
import com.example.freelance_demo.enums.InvoiceStatus;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.InvoiceLineMapper;
import com.example.freelance_demo.mapper.InvoiceMapper;
import com.example.freelance_demo.model.request.InvoiceLineRequest;
import com.example.freelance_demo.model.request.InvoiceRequest;
import com.example.freelance_demo.model.response.InvoiceResponse;
import com.example.freelance_demo.repositories.ClientRepository;
import com.example.freelance_demo.repositories.InvoiceRepository;
import com.example.freelance_demo.repositories.ProjectRepository;
import com.example.freelance_demo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final ClientRepository clientRepository;
    private final InvoiceLineMapper invoiceLineMapper;
    private final ProjectRepository projectRepository;


    @Override
    @Transactional
    public InvoiceResponse create(InvoiceRequest request) {
        ClientEntity client = clientRepository.findById(request.getClientId()).orElseThrow(()-> new ResourceNotFoundException(
                "Client not found."
        ));

        String invoiceNumber = generateInvoiceNumber();

        InvoiceEntity invoice = invoiceMapper.toEntity(request,client,invoiceNumber);

        if (request.getProjectId() != null) {
            projectRepository.findById(request.getProjectId())
                    .ifPresent(invoice::setProject);
        }

        for(InvoiceLineRequest lineRequest:request.getLines()){
            InvoiceLineEntity line=invoiceLineMapper.toEntity(lineRequest);
            line.setInvoice(invoice);

            if (line.getQuantity() != null && line.getUnitPrice() != null) {
                line.setAmount(line.getQuantity()
                        .multiply(line.getUnitPrice())
                        .setScale(2, RoundingMode.HALF_UP));
            }


            invoice.addLine(line);

        }

        BigDecimal subTotal = invoice.getLines()
                .stream()
                .map(InvoiceLineEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setSubTotal(subTotal);
        invoice.setTotalAmount(subTotal.subtract(
                invoice.getDiscountAmount() != null ? invoice.getDiscountAmount() : BigDecimal.ZERO
        ));


        InvoiceEntity saved = invoiceRepository.save(invoice);
        invoiceRepository.flush();

        InvoiceEntity fresh = invoiceRepository.findById(saved.getId()).orElseThrow();

        return invoiceMapper.toResponse(fresh);
    }

    @Override
    @Transactional
    public InvoiceResponse update(Long id, InvoiceRequest request) {
        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));


        if (!invoice.getClient().getId().equals(request.getClientId())) {
            ClientEntity client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found: " + request.getClientId()));
            invoice.setClient(client);
        }


        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setNotes(request.getNotes());
        invoice.setPaymentTerms(request.getPaymentTerms());


        invoice.clearLines();
        for (InvoiceLineRequest lineRequest : request.getLines()) {
            InvoiceLineEntity line = invoiceLineMapper.toEntity(lineRequest);
            line.setInvoice(invoice);
            invoice.addLine(line);
        }


        InvoiceEntity saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot delete paid invoice: " + id);
        }

        invoiceRepository.delete(invoice);
    }

    @Override
    public InvoiceResponse getById(Long id) {
        return invoiceRepository.findById(id)
                .map(invoiceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
    }

    @Override
    public Page<InvoiceResponse> list(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
        }

        return invoiceRepository.findAll(pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    public Page<InvoiceResponse> findByClientId(Long clientId, Pageable pageable) {
        return invoiceRepository.findByClientId(clientId, pageable)
                .map(invoiceMapper::toResponse);
    }


    @Override
    public Page<InvoiceResponse> findByStatus(InvoiceStatus status, Pageable pageable) {
        return invoiceRepository.findByStatus(status, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    public Page<InvoiceResponse> findOverdue(LocalDate date, Pageable pageable) {
        if (date == null) {
            date = LocalDate.now();
        }
        return invoiceRepository.findByDueDateBeforeAndStatusNot(date, InvoiceStatus.PAID, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    @Transactional
    public InvoiceResponse updateStatus(Long id, InvoiceStatus newStatus) {
        InvoiceEntity invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        validateStatusTransition(invoice.getStatus(), newStatus);

        invoice.setStatus(newStatus);


        if (newStatus == InvoiceStatus.PAID && invoice.getPaidAt() == null) {
            invoice.setPaidAt(LocalDateTime.now());
        } else if (newStatus != InvoiceStatus.PAID) {
            invoice.setPaidAt(null);
        }

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));

    }

    @Override
    public BigDecimal calculateRevenue(LocalDate start, LocalDate end) {
        if (start == null) {
            start = LocalDate.MIN;
        }
        if (end == null) {
            end = LocalDate.MAX;
        }
        return invoiceRepository.sumTotalAmountByStatusAndDateRange(
                InvoiceStatus.PAID, start, end);
    }

    private void validateStatusTransition(InvoiceStatus current, InvoiceStatus next) {
        boolean valid = switch (current) {
            case DRAFT -> next == InvoiceStatus.SENT || next == InvoiceStatus.CANCELLED;
            case SENT -> next == InvoiceStatus.PAID || next == InvoiceStatus.OVERDUE || next == InvoiceStatus.CANCELLED;
            case PAID, CANCELLED -> false; // Son state-lər
            case OVERDUE -> next == InvoiceStatus.PAID || next == InvoiceStatus.CANCELLED;
        };

        if (!valid) {
            throw new IllegalStateException(
                    String.format("Invalid status transition: %s → %s", current, next));
        }
    }

    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return String.format("INV-%d-%05d", LocalDate.now().getYear(), count);
    }
}
