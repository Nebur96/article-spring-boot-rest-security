package com.nebur.spring.article.persistence.repositories;

import com.nebur.spring.article.persistence.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findById(long id);

    Client findByNif(String NIF);

    Client findByUsername(String username);
}