package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);

    List<InvoiceEntity> findByClientIdOrderByInvoiceDateDesc(Long clientId);

    List<InvoiceEntity> findByStatus(InvoiceStatus status);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.status <> 'PAID'")
    List<InvoiceEntity> findByStatusNotPaid();

    @Query("select i from InvoiceEntity i where i.invoiceDate" +
            " between :start and :end order by i.invoiceDate desc")
    List<InvoiceEntity> findBetween(@Param("start")LocalDate start,
                                    @Param("end") LocalDate end);

    @Query("select coalesce(sum(i.totalAmount),0)" +
            " from InvoiceEntity i where i.status='PAID'")
    BigDecimal sumPaid();

    Page<InvoiceEntity> findByClientId(Long clientId, Pageable pageable);

    @Query("select i from InvoiceEntity i where i.invoiceDate>= :since" +
            " order by i.invoiceDate desc ")
    List<InvoiceEntity> findRecent(@Param("since") LocalDate since);

    Page<InvoiceEntity> findByStatus(InvoiceStatus status, Pageable pageable);

    Page<InvoiceEntity> findByDueDateBeforeAndStatusNot(LocalDate date,
                                                        InvoiceStatus status,
                                                        Pageable pageable);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM InvoiceEntity i " +
            "WHERE i.status = :status AND i.invoiceDate BETWEEN :start AND :end")
    BigDecimal sumTotalAmountByStatusAndDateRange(@Param("status") InvoiceStatus status,
                                                  @Param("start") LocalDate start,
                                                  @Param("end") LocalDate end);
}

