package com.example.freelance_demo.service.impl;

import com.example.freelance_demo.entity.ClientEntity;
import com.example.freelance_demo.exception.ResourceNotFoundException;
import com.example.freelance_demo.mapper.ClientMapper;
import com.example.freelance_demo.model.request.ClientRequest;
import com.example.freelance_demo.model.response.ClientResponse;
import com.example.freelance_demo.repositories.ClientRepository;
import com.example.freelance_demo.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public ClientResponse create(ClientRequest request) {
        if(clientRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException(
                    "Email already registered: "+request.getEmail()
            );
        }

        if(request.getPhone()!=null && clientRepository
                .existsByPhone(request.getPhone())){
            throw new IllegalArgumentException(
                    "phone already registered: "+request.getPhone()
            );
        }

        ClientEntity entity = clientMapper.toEntity(request);
        entity.setActive(true);

        ClientEntity saved =  clientRepository.save(entity);
        return clientMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {

        ClientEntity existing = clientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Client not found with id: "+id
        ));

        if(!existing.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException(
                    "Email already taken: "+request.getEmail()
            );
        }

        if(request.getPhone()!=null && !request.getPhone().equals(existing.getPhone())
        && clientRepository.existsByPhone(request.getPhone())){
            throw new IllegalArgumentException(
                    "Phon already taken: "+ request.getPhone()
            );
        }
        clientMapper.updateEntityFromRequest(request,existing);
        ClientEntity updated = clientRepository.save(existing);

        return clientMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClientEntity client = clientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Client not found with id: "+id
        ));

        client.setActive(false);
        clientRepository.save(client);

    }

    @Override
    public ClientResponse getById(Long id) {
        return clientRepository.findById(id).map(clientMapper::toResponse).orElseThrow(()->new ResourceNotFoundException(
                "Client not fount with id: "+id
        ));
    }

    @Override
    public ClientResponse getByEmail(String email) {
        return clientRepository.findByEmail(email).map(clientMapper::toResponse).orElseThrow(()->new ResourceNotFoundException(
                "Client not found with email: "+email
        ));
    }

    @Override
    public List<ClientResponse> getAll() {
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public List<ClientResponse> getAllActive() {
        return clientRepository.findAllActive().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public List<ClientResponse> searchByName(String name) {
        return clientRepository.findByFullNameContainingIgnoreCase(name).stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public List<ClientResponse> searchByCompany(String company) {
        return clientRepository.findByCompanyNameContainingIgnoreCase(company).stream().map(clientMapper::toResponse).toList();

    }

    @Override
    public List<ClientResponse> globalSearch(String keyword) {
        if(keyword==null || keyword.trim().isEmpty()){
            return List.of();
        }
            return clientRepository.search(keyword).stream().map(clientMapper::toResponse).toList();
    }

    @Override
    public boolean existsByPhone(String phone) {
        return clientRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public Page<ClientResponse> list(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::toResponse);
    }

    @Override
    public long countActive() {
        return clientRepository.countActive();
    }
}
