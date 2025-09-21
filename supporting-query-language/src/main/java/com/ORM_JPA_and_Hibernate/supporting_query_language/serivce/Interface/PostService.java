package com.ORM_JPA_and_Hibernate.supporting_query_language.serivce.Interface;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> findPostsByUserIdJPQL(Integer userId);
    List<Post> findPostsByUserIdNative(Integer userId);
    List<Post> findPostsByUserIdCriteria(Integer userId);
}
