package com.nebur.spring.article.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoClientFoundException extends ResponseStatusException {

    public NoClientFoundException(final String NIF) {
        super(HttpStatus.NOT_FOUND, "No client found by: " + NIF);
    }

    public NoClientFoundException(long id) {
        super(HttpStatus.NOT_FOUND, "No client with Id: " + id);
    }
}
