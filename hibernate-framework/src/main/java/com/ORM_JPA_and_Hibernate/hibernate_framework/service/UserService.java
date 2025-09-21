package com.ORM_JPA_and_Hibernate.hibernate_framework.service;

import com.ORM_JPA_and_Hibernate.hibernate_framework.entity.User;
import com.ORM_JPA_and_Hibernate.hibernate_framework.exception.UserDetailsNotFoundException;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();
    void deleteUserById(int id) throws UserDetailsNotFoundException;
    void addUser(User user);
    void updateUser(User user) throws UserDetailsNotFoundException;
}
