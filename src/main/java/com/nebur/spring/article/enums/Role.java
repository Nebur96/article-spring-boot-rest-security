package com.nebur.spring.article.enums;

public enum Role {

    USER("User"),
    ADMIN("Administrator");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
