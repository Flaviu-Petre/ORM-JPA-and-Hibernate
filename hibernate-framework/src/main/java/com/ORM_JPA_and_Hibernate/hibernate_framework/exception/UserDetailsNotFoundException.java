package com.ORM_JPA_and_Hibernate.hibernate_framework.exception;

public class UserDetailsNotFoundException extends RuntimeException {
    public UserDetailsNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "UserDetailsNotFoundException: " + getMessage();
    }
}
