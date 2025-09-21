package com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    //JPQL QUERIES
    @Query("SELECT u FROM User u WHERE SIZE(u.posts) > :postCount")
    List<User> findUsersWithMoreThanXPostsJPQL(@Param("postCount") int postCount);

    @Query("SELECT u FROM User u WHERE SIZE(u.posts) = 0")
    List<User> findUsersWithNoPostsJPQL();

    //NATIVE SQL QUERIES
    @Query(value = """
        SELECT u.* FROM users u 
        WHERE (SELECT COUNT(*) FROM posts p WHERE p.user_id = u.user_id) > :postCount
        """, nativeQuery = true)
    List<User> findUsersWithMoreThanXPostsNative(@Param("postCount") int postCount);

    @Query
    (value = """
        SELECT u.* FROM users u 
        WHERE (SELECT COUNT(*) FROM posts p WHERE p.user_id = u.user_id) = 0
        """, nativeQuery = true)
    List<User> findUsersWithNoPostsNative();
}
