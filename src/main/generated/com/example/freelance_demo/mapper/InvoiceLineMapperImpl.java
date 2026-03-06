package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.InvoiceLineEntity;
import com.example.freelance_demo.model.request.InvoiceLineRequest;
import com.example.freelance_demo.model.response.InvoiceLineResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:23+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class InvoiceLineMapperImpl implements InvoiceLineMapper {

    @Override
    public InvoiceLineResponse toResponse(InvoiceLineEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceLineResponse invoiceLineResponse = new InvoiceLineResponse();

        invoiceLineResponse.setDescription( entity.getDescription() );
        invoiceLineResponse.setQuantity( entity.getQuantity() );
        invoiceLineResponse.setUnitPrice( entity.getUnitPrice() );
        invoiceLineResponse.setAmount( entity.getAmount() );

        return invoiceLineResponse;
    }

    @Override
    public List<InvoiceLineResponse> toResponseList(List<InvoiceLineEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<InvoiceLineResponse> list = new ArrayList<InvoiceLineResponse>( entities.size() );
        for ( InvoiceLineEntity invoiceLineEntity : entities ) {
            list.add( toResponse( invoiceLineEntity ) );
        }

        return list;
    }

    @Override
    public InvoiceLineEntity toEntity(InvoiceLineRequest request) {
        if ( request == null ) {
            return null;
        }

        InvoiceLineEntity invoiceLineEntity = new InvoiceLineEntity();

        invoiceLineEntity.setDescription( request.getDescription() );
        invoiceLineEntity.setUnitPrice( request.getUnitPrice() );

        invoiceLineEntity.setQuantity( java.math.BigDecimal.valueOf(request.getQuantity()) );

        return invoiceLineEntity;
    }
}
