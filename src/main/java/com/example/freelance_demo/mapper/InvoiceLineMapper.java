package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.InvoiceLineEntity;
import com.example.freelance_demo.model.request.InvoiceLineRequest;
import com.example.freelance_demo.model.response.InvoiceLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceLineMapper {
    InvoiceLineResponse toResponse(InvoiceLineEntity entity);
    List<InvoiceLineResponse> toResponseList(List<InvoiceLineEntity> entities);

    @Mapping(target = "quantity", expression = "java(java.math.BigDecimal.valueOf(request.getQuantity()))")
    InvoiceLineEntity toEntity(InvoiceLineRequest request);

}
