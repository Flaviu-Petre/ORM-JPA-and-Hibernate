package com.ORM_JPA_and_Hibernate.supporting_query_language.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    //region fields
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    //endregion

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user=" + (user != null ? user.getUserId() : null) +
                '}';
    }
}
