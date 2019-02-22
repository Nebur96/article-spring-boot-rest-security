package com.nebur.spring.article.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ClientDto {

    private long id;

    @NotNull(message = "Please, insert a valid username.")
    private String username;

    @NotNull(message = "Please, insert a valid password.")
    private String password;

    @NotNull(message = "Please, insert a valid name.")
    private String name;

    @NotNull(message = "Please, insert a valid NIF.")
    @Size(min = 9, max = 9)
    private String nif;

    @NotNull(message = "Please, insert a valid county.")
    private String county;

    public ClientDto(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
