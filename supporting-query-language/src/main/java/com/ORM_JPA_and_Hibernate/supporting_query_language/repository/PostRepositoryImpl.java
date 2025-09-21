package com.ORM_JPA_and_Hibernate.supporting_query_language.repository;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.PostRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Post> findPostsByUserIdCriteria(Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> post = cq.from(Post.class);

        // Add where clause to filter by userId
        cq.select(post)
                .where(cb.equal(post.get("user").get("userId"), userId));

        return entityManager.createQuery(cq).getResultList();
    }
}
