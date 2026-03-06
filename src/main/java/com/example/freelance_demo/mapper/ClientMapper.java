package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.model.request.ClientRequest;
import com.example.freelance_demo.model.response.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity toEntity(ClientRequest request);

    ClientResponse toResponse(ClientEntity client);

    void updateEntityFromRequest(ClientRequest request, @MappingTarget ClientEntity entity);


}
