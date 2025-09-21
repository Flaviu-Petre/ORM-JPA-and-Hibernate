package com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    //JPQL QUERIES
    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId")
    List<Post> findPostsByUserIdJPQL(@Param("userId") Integer userId);

    //NATIVE SQL QUERIES
    @Query(value = "SELECT * FROM posts p WHERE p.user_id = :userId", nativeQuery = true)
    List<Post> findPostsByUserIdNative(@Param("userId") Integer userId);
}
