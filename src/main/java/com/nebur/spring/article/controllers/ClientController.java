package com.nebur.spring.article.controllers;

import com.nebur.spring.article.dto.ClientDto;
import com.nebur.spring.article.dto.LoginDto;
import com.nebur.spring.article.exceptions.InvalidNIFException;
import com.nebur.spring.article.exceptions.InvalidTokenException;
import com.nebur.spring.article.persistence.models.Client;
import com.nebur.spring.article.security.JwtTokenUtil;
import com.nebur.spring.article.service.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil){
        this.clientService = clientService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // EndPoint #1
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Client> signupClient(@Valid @RequestBody ClientDto clientDto) {
        Client client = clientService.save(clientDto);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    // EndPoint #2
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody LoginDto loginDto) throws AuthenticationException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        final Client client = clientService.findByUsername(loginDto.getUsername());
        final String token = jwtTokenUtil.generateToken(client);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    // EndPoint #3
    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public ResponseEntity<String> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            throw new InvalidTokenException();
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Client client = clientService.findByUsername(userDetails.getUsername());

        String response = "You are " + client.getName() + " and you live in " + client.getCounty() + "!";

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // EndPoint #4
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Client> getByClientId(@PathVariable(value = "id") long clientId) {
        Client client = clientService.findById(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    // EndPoint #5
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    // EndPoint #6
    @RequestMapping(value = "/nif", method = RequestMethod.GET)
    public ResponseEntity<Client> getByClientNIF(@RequestParam(value = "nif") String nif) {
        if (!nif.matches("[0-9]+") || nif.length() != 9) {
            throw new InvalidNIFException(nif);
        }
        Client client = clientService.findByNIF(nif);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    // EndPoint #7
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Client> updateClient(@Valid @RequestBody ClientDto clientDto) {
        Client client = clientService.update(clientDto);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    // EndPoint #8
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteClient(@PathVariable(value = "id") long clientID) {
        clientService.deleteById(clientID);
        return ResponseEntity.status(HttpStatus.OK).body("Client deleted successfully.");
    }
}
