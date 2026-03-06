package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.entity.Project;
import com.example.freelance_demo.enums.InvoiceStatus;
import com.example.freelance_demo.model.request.InvoiceRequest;
import com.example.freelance_demo.model.response.InvoiceResponse;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:23+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Autowired
    private InvoiceLineMapper invoiceLineMapper;

    @Override
    public InvoiceEntity toEntity(InvoiceRequest request, ClientEntity client, String invoiceNumber) {
        if ( request == null ) {
            return null;
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setInvoiceDate( request.getInvoiceDate() );
        invoiceEntity.setDueDate( request.getDueDate() );
        invoiceEntity.setNotes( request.getNotes() );
        invoiceEntity.setPaymentTerms( request.getPaymentTerms() );

        invoiceEntity.setStatus( InvoiceStatus.DRAFT );

        afterToEntity( invoiceEntity, request, client, invoiceNumber );

        return invoiceEntity;
    }

    @Override
    public InvoiceResponse toResponse(InvoiceEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceResponse invoiceResponse = new InvoiceResponse();

        invoiceResponse.setClientId( entityClientId( entity ) );
        invoiceResponse.setClientName( entityClientFullName( entity ) );
        invoiceResponse.setProjectId( entityProjectId( entity ) );
        invoiceResponse.setLines( invoiceLineMapper.toResponseList( entity.getLines() ) );
        invoiceResponse.setId( entity.getId() );
        invoiceResponse.setInvoiceNumber( entity.getInvoiceNumber() );
        invoiceResponse.setInvoiceDate( entity.getInvoiceDate() );
        invoiceResponse.setDueDate( entity.getDueDate() );
        invoiceResponse.setSubTotal( entity.getSubTotal() );
        invoiceResponse.setTotalAmount( entity.getTotalAmount() );
        invoiceResponse.setStatus( entity.getStatus() );
        invoiceResponse.setNotes( entity.getNotes() );
        invoiceResponse.setCreatedAt( entity.getCreatedAt() );
        invoiceResponse.setUpdatedAt( entity.getUpdatedAt() );

        return invoiceResponse;
    }

    private Long entityClientId(InvoiceEntity invoiceEntity) {
        if ( invoiceEntity == null ) {
            return null;
        }
        ClientEntity client = invoiceEntity.getClient();
        if ( client == null ) {
            return null;
        }
        Long id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityClientFullName(InvoiceEntity invoiceEntity) {
        if ( invoiceEntity == null ) {
            return null;
        }
        ClientEntity client = invoiceEntity.getClient();
        if ( client == null ) {
            return null;
        }
        String fullName = client.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    private Long entityProjectId(InvoiceEntity invoiceEntity) {
        if ( invoiceEntity == null ) {
            return null;
        }
        Project project = invoiceEntity.getProject();
        if ( project == null ) {
            return null;
        }
        Long id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
