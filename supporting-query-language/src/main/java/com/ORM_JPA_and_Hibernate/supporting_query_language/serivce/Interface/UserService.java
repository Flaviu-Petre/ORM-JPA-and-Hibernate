package com.ORM_JPA_and_Hibernate.supporting_query_language.serivce.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;

import java.util.List;

public interface UserService {

    List<User> findUsersWithMoreThanXPostsJPQL(int postCount);
    List<User> findUsersWithMoreThanXPostsNative(int postCount);
    List<User> findUsersWithMoreThanXPostsCriteria(long postCount);

    List<User> findUsersWithNoPostsJPQL();
    List<User> findUsersWithNoPostsNative();
    List<User> findUsersWithNoPostsCriteria();

}
