package com.nebur.spring.article.service.impl;

import com.nebur.spring.article.dto.ClientDto;
import com.nebur.spring.article.enums.Role;
import com.nebur.spring.article.exceptions.InvalidNIFException;
import com.nebur.spring.article.exceptions.NoClientFoundException;
import com.nebur.spring.article.persistence.models.Client;
import com.nebur.spring.article.persistence.repositories.ClientRepository;
import com.nebur.spring.article.service.IClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Transactional
@Service(value = "clientService")
public class ClientService implements UserDetailsService, IClientService {

    private ClientRepository clientRepository;
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bcryptEncoder) {
        this.clientRepository = clientRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username);
        if(client == null){
            throw new NoClientFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getRole()));
    }

    @Override
    public Client save(ClientDto clientDto) {
        Client client = new Client();
        client.setUsername(clientDto.getUsername());
        client.setPassword(bcryptEncoder.encode(clientDto.getPassword()));
        client.setCounty(clientDto.getCounty());
        client.setNif(clientDto.getNif());
        client.setName(clientDto.getName());
        return clientRepository.save(client);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(long id) {
        Client client = clientRepository.findById(id);
        if (client == null) {
            throw new NoClientFoundException(id);
        }
        return client;
    }

    @Override
    public Client update(ClientDto clientDto) {
        Client client = clientRepository.findById(clientDto.getId());
        if(client == null) {
            throw new NoClientFoundException(clientDto.getId());
        }

        BeanUtils.copyProperties(clientDto, client, "password", "username", "id");

        return clientRepository.save(client);
    }

    @Override
    public Client findByNIF(String nif) {
        Client client = clientRepository.findByNif(nif);
        if (client == null) {
            throw new InvalidNIFException(nif);
        }
        return client;
    }

    @Override
    public void deleteById(long clientID) {
        clientRepository.deleteById(clientID);
    }

    @Override
    public Client findByUsername(String username) {
        Client client = clientRepository.findByUsername(username);
        if (client == null) {
            throw new NoClientFoundException(username);
        }
        return client;
    }
}
