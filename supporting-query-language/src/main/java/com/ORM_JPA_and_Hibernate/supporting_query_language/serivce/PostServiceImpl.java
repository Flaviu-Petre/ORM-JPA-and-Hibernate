package com.ORM_JPA_and_Hibernate.supporting_query_language.serivce;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.PostRepository;
import com.ORM_JPA_and_Hibernate.supporting_query_language.serivce.Interface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;


    @Override
    public List<Post> findPostsByUserIdJPQL(Integer userId) {
        return postRepository.findPostsByUserIdJPQL(userId);
    }

    @Override
    public List<Post> findPostsByUserIdNative(Integer userId) {
        return postRepository.findPostsByUserIdNative(userId);
    }

    @Override
    public List<Post> findPostsByUserIdCriteria(Integer userId) {
        return postRepository.findPostsByUserIdCriteria(userId);
    }
}
