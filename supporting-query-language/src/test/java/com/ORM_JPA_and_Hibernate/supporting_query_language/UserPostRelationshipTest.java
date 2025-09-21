package com.ORM_JPA_and_Hibernate.supporting_query_language;

import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.Post;
import com.ORM_JPA_and_Hibernate.supporting_query_language.entity.User;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.PostRepository;
import com.ORM_JPA_and_Hibernate.supporting_query_language.repository.Interface.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class UserPostRelationshipTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User testUser;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setPosts(new ArrayList<>());

        post1 = new Post();
        post1.setTitle("First Post");
        post1.setContent("This is the content of the first post");
        post1.setUser(testUser);

        post2 = new Post();
        post2.setTitle("Second Post");
        post2.setContent("This is the content of the second post");
        post2.setUser(testUser);

        post3 = new Post();
        post3.setTitle("Third Post");
        post3.setContent("This is the content of the third post");
        post3.setUser(testUser);

        testUser.getPosts().add(post1);
        testUser.getPosts().add(post2);
        testUser.getPosts().add(post3);
    }

    @Test
    void testUserCanHaveMultiplePosts() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");

        assertThat(savedUser.getPosts()).hasSize(3);
        assertThat(savedUser.getPosts()).extracting(Post::getTitle)
                .containsExactlyInAnyOrder("First Post", "Second Post", "Third Post");

        List<Post> allPosts = postRepository.findAll();
        assertThat(allPosts).hasSize(3);

        allPosts.forEach(post -> {
            assertThat(post.getUser()).isNotNull();
            assertThat(post.getUser().getUserId()).isEqualTo(savedUser.getUserId());
            assertThat(post.getUser().getUsername()).isEqualTo("testuser");
        });
    }

    @Test
    void testBidirectionalRelationshipConsistency() {
        User savedUser = userRepository.save(testUser);

        Optional<User> fetchedUserOpt = userRepository.findById(savedUser.getUserId());

        assertTrue(fetchedUserOpt.isPresent());
        User fetchedUser = fetchedUserOpt.get();

        assertThat(fetchedUser.getPosts()).hasSize(3);

        fetchedUser.getPosts().forEach(post -> {
            assertThat(post.getUser()).isNotNull();
            assertThat(post.getUser().getUserId()).isEqualTo(fetchedUser.getUserId());
        });
    }

    @Test
    void testCascadeDeleteWhenUserIsDeleted() {
        User savedUser = userRepository.save(testUser);
        Integer userId = savedUser.getUserId();

        assertThat(userRepository.findById(userId)).isPresent();
        List<Post> initialPosts = postRepository.findAll();
        assertThat(initialPosts).hasSize(3);

        List<Integer> postIds = initialPosts.stream()
                .map(Post::getPostId)
                .toList();

        userRepository.deleteById(userId);
        userRepository.flush(); // Force immediate execution

        Optional<User> deletedUser = userRepository.findById(userId);
        assertThat(deletedUser).isEmpty();

        List<Post> remainingPosts = postRepository.findAll();
        assertThat(remainingPosts).isEmpty();

        postIds.forEach(postId -> {
            Optional<Post> deletedPost = postRepository.findById((long) postId);
            assertThat(deletedPost).isEmpty();
        });
    }

    @Test
    void testCascadeDeleteWithMultipleUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPosts(new ArrayList<>());

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPosts(new ArrayList<>());

        Post user1Post1 = new Post();
        user1Post1.setTitle("User1 Post1");
        user1Post1.setContent("Content for user1 post1");
        user1Post1.setUser(user1);
        user1.getPosts().add(user1Post1);

        Post user1Post2 = new Post();
        user1Post2.setTitle("User1 Post2");
        user1Post2.setContent("Content for user1 post2");
        user1Post2.setUser(user1);
        user1.getPosts().add(user1Post2);

        Post user2Post1 = new Post();
        user2Post1.setTitle("User2 Post1");
        user2Post1.setContent("Content for user2 post1");
        user2Post1.setUser(user2);
        user2.getPosts().add(user2Post1);

        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        assertThat(userRepository.findAll()).hasSize(2);
        assertThat(postRepository.findAll()).hasSize(3);

        userRepository.deleteById(savedUser1.getUserId());
        userRepository.flush();

        assertThat(userRepository.findById(savedUser1.getUserId())).isEmpty();
        assertThat(userRepository.findById(savedUser2.getUserId())).isPresent();

        List<Post> remainingPosts = postRepository.findAll();
        assertThat(remainingPosts).hasSize(1);
        assertThat(remainingPosts.get(0).getTitle()).isEqualTo("User2 Post1");
        assertThat(remainingPosts.get(0).getUser().getUserId()).isEqualTo(savedUser2.getUserId());
    }

    @Test
    void testOrphanPostsHandling() {
        User savedUser = userRepository.save(testUser);

        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(3);

        Post postToDelete = posts.get(0);
        Integer postIdToDelete = postToDelete.getPostId();

        postRepository.deleteById((long) postIdToDelete);
        postRepository.flush();

        assertThat(postRepository.findById((long) postIdToDelete)).isEmpty();
        assertThat(postRepository.findAll()).hasSize(2);

        assertThat(userRepository.findById(savedUser.getUserId())).isPresent();
    }

    @Test
    void testAddPostToExistingUser() {
        User savedUser = userRepository.save(testUser);
        assertThat(postRepository.findAll()).hasSize(3);

        Post newPost = new Post();
        newPost.setTitle("Fourth Post");
        newPost.setContent("This is a new post added later");
        newPost.setUser(savedUser);

        Post savedPost = postRepository.save(newPost);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getPostId()).isNotNull();
        assertThat(postRepository.findAll()).hasSize(4);

        assertThat(savedPost.getUser().getUserId()).isEqualTo(savedUser.getUserId());
    }
}