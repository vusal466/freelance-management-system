package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.InvoiceLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLineEntity, Long> {

    List<InvoiceLineEntity> findByInvoiceId(Long invoiceId);

    List<InvoiceLineEntity> findByTaskId(Long taskId);


}
