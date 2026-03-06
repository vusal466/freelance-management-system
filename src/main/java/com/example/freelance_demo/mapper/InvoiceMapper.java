package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.entity.InvoiceLineEntity;
import com.example.freelance_demo.model.request.InvoiceRequest;
import com.example.freelance_demo.model.response.InvoiceResponse;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = InvoiceLineMapper.class)
public interface InvoiceMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "invoiceNumber",ignore = true)
    @Mapping(target = "status",constant = "DRAFT")
    @Mapping(target = "totalAmount",ignore = true)
    @Mapping(target = "client",ignore = true)
    @Mapping(target = "project",ignore = true)
    @Mapping(target = "lines", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    InvoiceEntity toEntity(InvoiceRequest request,
                           @Context ClientEntity client,
                           @Context String invoiceNumber);

    @AfterMapping
    default void afterToEntity(@MappingTarget InvoiceEntity entity,
                               InvoiceRequest request,
                               @Context ClientEntity client,
                               @Context String invoiceNumber){
        entity.setClient(client);
        entity.setInvoiceNumber(invoiceNumber);

    }

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName", source = "client.fullName")
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "lines", source = "lines")
    InvoiceResponse toResponse(InvoiceEntity entity);


}
