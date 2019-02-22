package com.nebur.spring.article.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidNIFException extends ResponseStatusException {

    public InvalidNIFException(final String NIF) {
        super(HttpStatus.BAD_REQUEST, "Provided NIF is not valid: " + NIF);
    }
}
