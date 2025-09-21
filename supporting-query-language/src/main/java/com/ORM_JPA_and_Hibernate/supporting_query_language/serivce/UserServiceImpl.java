package com.ORM_JPA_and_Hibernate.supporting_query_language.serivce;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.UserRepository;
import com.ORM_JPA_and_Hibernate.supporting_query_language.serivce.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> findUsersWithMoreThanXPostsJPQL(int postCount) {
        return userRepository.findUsersWithMoreThanXPostsJPQL(postCount);
    }

    @Override
    public List<User> findUsersWithMoreThanXPostsNative(int postCount) {
        return userRepository.findUsersWithMoreThanXPostsNative(postCount);
    }

    @Override
    public List<User> findUsersWithMoreThanXPostsCriteria(long postCount) {
        return userRepository.findUsersWithMoreThanXPostsCriteria(postCount);
    }

    @Override
    public List<User> findUsersWithNoPostsJPQL() {
        return  userRepository.findUsersWithNoPostsJPQL();
    }

    @Override
    public List<User> findUsersWithNoPostsNative() {
        return userRepository.findUsersWithNoPostsNative();
    }

    @Override
    public List<User> findUsersWithNoPostsCriteria() {
        return userRepository.findUsersWithNoPostsCriteria();
    }
}
