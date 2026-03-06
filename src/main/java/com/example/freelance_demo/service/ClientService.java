package com.example.freelance_demo.service;
import com.example.freelance_demo.model.request.ClientRequest;
import com.example.freelance_demo.model.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    ClientResponse create(ClientRequest request);

    ClientResponse update(Long id, ClientRequest request);

    void delete(Long id);

    ClientResponse getById(Long id);

    ClientResponse getByEmail(String email);

    List<ClientResponse> getAll();

    List<ClientResponse> getAllActive();

    List<ClientResponse> searchByName(String name);

    List<ClientResponse> searchByCompany(String company);

    List<ClientResponse> globalSearch(String keyword);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Page<ClientResponse> list(Pageable pageable);
    long countActive();



}
