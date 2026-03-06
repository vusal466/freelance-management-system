package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.InvoiceEntity;
import com.example.freelance_demo.entity.PaymentEntity;
import com.example.freelance_demo.model.request.PaymentRequest;
import com.example.freelance_demo.model.response.PaymentResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:24+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(PaymentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setInvoiceId( entityInvoiceId( entity ) );
        paymentResponse.setId( entity.getId() );
        paymentResponse.setPaymentDate( entity.getPaymentDate() );
        paymentResponse.setAmount( entity.getAmount() );
        paymentResponse.setPaymentMethod( entity.getPaymentMethod() );
        paymentResponse.setReferenceNumber( entity.getReferenceNumber() );
        paymentResponse.setNotes( entity.getNotes() );
        paymentResponse.setCreatedAt( entity.getCreatedAt() );

        return paymentResponse;
    }

    @Override
    public List<PaymentResponse> toResponseList(List<PaymentEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<PaymentResponse> list = new ArrayList<PaymentResponse>( entities.size() );
        for ( PaymentEntity paymentEntity : entities ) {
            list.add( toResponse( paymentEntity ) );
        }

        return list;
    }

    @Override
    public PaymentEntity toEntity(PaymentRequest request) {
        if ( request == null ) {
            return null;
        }

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setPaymentDate( request.getPaymentDate() );
        paymentEntity.setAmount( request.getAmount() );
        paymentEntity.setPaymentMethod( request.getPaymentMethod() );
        paymentEntity.setReferenceNumber( request.getReferenceNumber() );
        paymentEntity.setNotes( request.getNotes() );

        return paymentEntity;
    }

    private Long entityInvoiceId(PaymentEntity paymentEntity) {
        if ( paymentEntity == null ) {
            return null;
        }
        InvoiceEntity invoice = paymentEntity.getInvoice();
        if ( invoice == null ) {
            return null;
        }
        Long id = invoice.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
