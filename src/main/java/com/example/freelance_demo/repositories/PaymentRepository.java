package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {

    List<PaymentEntity> findByInvoiceId(Long invoiceId);
    @Query("select coalesce(sum(p.amount),0) from" +
            " PaymentEntity p where p.invoice.id = :invoiceId ")
    BigDecimal sumPaidByInvoice(@Param("invoiceId") Long invoiceId);

    Page<PaymentEntity> findByInvoiceId(Long invoiceId, Pageable pageable);

    void deleteByInvoiceId(Long invoiceId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentEntity p WHERE p.invoice.id = :invoiceId")
    BigDecimal totalPaidByInvoice(@Param("invoiceId") Long invoiceId);
}

