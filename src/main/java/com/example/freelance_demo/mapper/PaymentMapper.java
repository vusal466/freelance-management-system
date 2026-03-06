package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.PaymentEntity;
import com.example.freelance_demo.model.request.PaymentRequest;
import com.example.freelance_demo.model.response.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "invoiceId",source = "invoice.id")
    PaymentResponse toResponse(PaymentEntity entity);

    List<PaymentResponse> toResponseList(List<PaymentEntity> entities);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "invoice",ignore = true)
    PaymentEntity toEntity(PaymentRequest request);
}
