package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.InvoiceItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceItemsRepository extends JpaRepository<InvoiceItemsEntity,Long> {

    List<InvoiceItemsEntity> findByInvoiceIdOrderById(Long invoiceId);

    @Query("select coalesce(sum(i.totalPrice)) from" +
            " InvoiceItemsEntity i where i.invoice.id=:invoiceId")
    BigDecimal sumTotalByInvoiceId(@Param("invoiceId") Long invoiceId);

    void deleteByInvoiceId(Long id);

}
