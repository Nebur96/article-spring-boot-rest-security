package com.nebur.spring.article.dto;

import javax.validation.constraints.NotNull;

public class LoginDto {

    @NotNull(message = "Please, insert a valid username.")
    private String username;

    @NotNull(message = "Please, insert a valid password.")
    private String password;

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
}
