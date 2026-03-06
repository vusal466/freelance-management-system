package com.example.freelance_demo.repositories;

import com.example.freelance_demo.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {

    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);


    Optional<ClientEntity> findByEmail(String email);

    List<ClientEntity> findByFullNameContainingIgnoreCase(String fullName);
    List<ClientEntity> findByCompanyNameContainingIgnoreCase(String company);

    @Query("select c from ClientEntity c where" +
            " c.isActive=true order by c.createdAt desc")
    List<ClientEntity> findAllActive();

    @Query("select count(c) from ClientEntity c where c.isActive=true")
    long countActive();

    @Query("select c from ClientEntity c where " +
            "lower( c.fullName) like lower(concat( '%',:keyword,'%'))" +
            " or lower(c.companyName) like lower(concat('',:keyword,''))" +
            " or lower(c.email) like lower(concat('',:keyword,'')) or " +
            "lower(c.phone) like lower(concat('',:keyword,'')) ")
    List<ClientEntity> search(@Param("keyword") String keyword);
}
