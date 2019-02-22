package com.nebur.spring.article.service;

import com.nebur.spring.article.dto.ClientDto;
import com.nebur.spring.article.persistence.models.Client;

import java.util.List;

public interface IClientService {

    Client save(ClientDto clientDto);

    List<Client> findAll();

    Client findById(long id);

    Client update(ClientDto clientDto);

    Client findByNIF(String nif);

    void deleteById(long clientID);

    Client findByUsername(String username);
}
