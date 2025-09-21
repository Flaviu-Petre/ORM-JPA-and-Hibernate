package com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepositoryCustom {

    // Add dummy @Query annotation to prevent Spring from trying to derive queries
    @Query("SELECT p FROM Post p WHERE 1=0") // This query will never be executed
    List<Post> findPostsByUserIdCriteria(Integer userId);
}