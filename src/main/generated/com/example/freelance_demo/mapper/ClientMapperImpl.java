package com.example.freelance_demo.mapper;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.model.request.ClientRequest;
import com.example.freelance_demo.model.response.ClientResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T21:51:24+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientEntity toEntity(ClientRequest request) {
        if ( request == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setFullName( request.getFullName() );
        clientEntity.setCompanyName( request.getCompanyName() );
        clientEntity.setAddress( request.getAddress() );
        clientEntity.setEmail( request.getEmail() );
        clientEntity.setPhone( request.getPhone() );
        clientEntity.setNotes( request.getNotes() );

        return clientEntity;
    }

    @Override
    public ClientResponse toResponse(ClientEntity client) {
        if ( client == null ) {
            return null;
        }

        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setId( client.getId() );
        clientResponse.setFullName( client.getFullName() );
        clientResponse.setCompanyName( client.getCompanyName() );
        clientResponse.setEmail( client.getEmail() );
        clientResponse.setPhone( client.getPhone() );
        clientResponse.setAddress( client.getAddress() );
        clientResponse.setNotes( client.getNotes() );
        clientResponse.setActive( client.isActive() );
        clientResponse.setCreatedAt( client.getCreatedAt() );

        return clientResponse;
    }

    @Override
    public void updateEntityFromRequest(ClientRequest request, ClientEntity entity) {
        if ( request == null ) {
            return;
        }

        entity.setFullName( request.getFullName() );
        entity.setCompanyName( request.getCompanyName() );
        entity.setAddress( request.getAddress() );
        entity.setEmail( request.getEmail() );
        entity.setPhone( request.getPhone() );
        entity.setNotes( request.getNotes() );
    }
}
