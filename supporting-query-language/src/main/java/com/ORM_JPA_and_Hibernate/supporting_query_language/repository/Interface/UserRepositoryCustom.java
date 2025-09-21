package com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepositoryCustom {

    // Add dummy @Query annotations to prevent Spring from trying to derive queries
    @Query("SELECT u FROM User u WHERE 1=0") // This query will never be executed
    List<User> findUsersWithMoreThanXPostsCriteria(long postCount);

    @Query("SELECT u FROM User u WHERE 1=0") // This query will never be executed
    List<User> findUsersWithNoPostsCriteria();
}