package com.ORM_JPA_and_Hibernate.hibernate_framework.service;

import com.ORM_JPA_and_Hibernate.hibernate_framework.dao.UserDao;
import com.ORM_JPA_and_Hibernate.hibernate_framework.entity.User;
import com.ORM_JPA_and_Hibernate.hibernate_framework.exception.UserDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.getAll();
    }

    @Override
    public void deleteUserById(int id) throws UserDetailsNotFoundException {
        userDao.delete(id);
    }


    @Override
    public void addUser(User user) {
        userDao.insert(user);
    }

    @Override
    public void updateUser(User user) throws UserDetailsNotFoundException {
        userDao.update(user);
    }
}
