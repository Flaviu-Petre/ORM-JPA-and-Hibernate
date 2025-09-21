package com.ORM_JPA_and_Hibernate.supporting_query_language.repository;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersWithMoreThanXPostsCriteria(long postCount) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        // Join with posts
        Join<User, Post> posts = user.join("posts");

        // Group by user and having count > postCount
        cq.select(user)
                .groupBy(user.get("userId"))
                .having(cb.greaterThan(cb.count(posts), postCount));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<User> findUsersWithNoPostsCriteria() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);

        // Left join with posts
        Join<User, Post> posts = user.join("posts", JoinType.LEFT);

        // Where posts is null
        cq.select(user)
                .where(cb.isNull(posts.get("postId")));

        return entityManager.createQuery(cq).getResultList();
    }
}
