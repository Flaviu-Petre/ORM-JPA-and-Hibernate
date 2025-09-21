package com.ORM_JPA_and_Hibernate.Entity_Mapping_and_Relationships_in_hibernate.test;

import com.ORM_JPA_and_Hibernate.Entity_Mapping_and_Relationships_in_hibernate.entity.Post;
import com.ORM_JPA_and_Hibernate.Entity_Mapping_and_Relationships_in_hibernate.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(false)
public class UserPostRelationshipTest {

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");

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
    }

    @Test
    void testUserCanHaveMultiplePosts() {
        entityManager.persistAndFlush(testUser);

        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);
        entityManager.persistAndFlush(post3);

        entityManager.clear();

        User savedUser = entityManager.find(User.class, testUser.getUserId());

        assertNotNull(savedUser, "User should be saved successfully");
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("testuser@example.com", savedUser.getEmail());

        List<Post> userPosts = savedUser.getPosts();
        assertNotNull(userPosts, "User should have posts");
        assertEquals(3, userPosts.size(), "User should have exactly 3 posts");

        for (Post post : userPosts) {
            assertEquals(savedUser.getUserId(), post.getUser().getUserId(),
                    "Each post should belong to the correct user");
        }

        List<String> postTitles = userPosts.stream()
                .map(Post::getTitle)
                .sorted()
                .toList();

        assertTrue(postTitles.contains("First Post"));
        assertTrue(postTitles.contains("Second Post"));
        assertTrue(postTitles.contains("Third Post"));
    }

    @Test
    void testCascadeDeleteWhenUserIsDeleted() {
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);
        entityManager.persistAndFlush(post3);

        Integer userId = testUser.getUserId();
        Integer post1Id = post1.getPostId();
        Integer post2Id = post2.getPostId();
        Integer post3Id = post3.getPostId();

        entityManager.clear();

        User userBeforeDelete = entityManager.find(User.class, userId);
        Post post1BeforeDelete = entityManager.find(Post.class, post1Id);
        Post post2BeforeDelete = entityManager.find(Post.class, post2Id);
        Post post3BeforeDelete = entityManager.find(Post.class, post3Id);

        assertNotNull(userBeforeDelete, "User should exist before deletion");
        assertNotNull(post1BeforeDelete, "Post 1 should exist before deletion");
        assertNotNull(post2BeforeDelete, "Post 2 should exist before deletion");
        assertNotNull(post3BeforeDelete, "Post 3 should exist before deletion");

        entityManager.remove(userBeforeDelete);
        entityManager.flush();
        entityManager.clear();

        User deletedUser = entityManager.find(User.class, userId);
        assertNull(deletedUser, "User should be deleted");

        Post deletedPost1 = entityManager.find(Post.class, post1Id);
        Post deletedPost2 = entityManager.find(Post.class, post2Id);
        Post deletedPost3 = entityManager.find(Post.class, post3Id);

        assertNull(deletedPost1, "Post 1 should be deleted when user is deleted");
        assertNull(deletedPost2, "Post 2 should be deleted when user is deleted");
        assertNull(deletedPost3, "Post 3 should be deleted when user is deleted");
    }

    @Test
    void testPostCannotExistWithoutUser() {
        Post orphanPost = new Post();
        orphanPost.setTitle("Orphan Post");
        orphanPost.setContent("This post has no user");

        // This should throw an exception due to nullable = false constraint
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(orphanPost);
        }, "Post without user should throw an exception");
    }

    @Test
    void testBidirectionalRelationshipConsistency() {
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(post1);
        entityManager.persistAndFlush(post2);

        entityManager.clear();

        User loadedUser = entityManager.find(User.class, testUser.getUserId());
        List<Post> posts = loadedUser.getPosts();

        assertEquals(2, posts.size(), "User should have 2 posts");

        for (Post post : posts) {
            assertEquals(loadedUser.getUserId(), post.getUser().getUserId(),
                    "Post's user reference should point back to the correct user");
            assertEquals(loadedUser.getUsername(), post.getUser().getUsername(),
                    "Post's user should have the correct username");
        }
    }
}