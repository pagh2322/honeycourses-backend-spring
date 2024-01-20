package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_category")
    private PostCategory category;

    @Column(name = "post_title", length = 45, nullable = false)
    private String title;

    @Column(name = "post_content", length = 2000)
    private String content;

    @Column(name = "post_author", length = 1000)
    private String author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    @Column(name = "post_time")
    @CreatedDate
    private LocalDateTime createdAt;


    @Builder
    public Post(Long id, PostCategory category, String title, String content, String author, List<Comment> comments, List<PostLike> postLikes) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.comments = comments;
        this.postLikes = postLikes;
    }

    public boolean isSameAuthor(String author) {
        return this.author.equals(author);
    }

    public void validateAuthor(String author) {
        if (!this.author.equals(author)) throw new AuthorizationException();
    }

    public void addPostLike(PostLike postLike) {
        postLikes.add(postLike);
    }

    public void deletePostLike(PostLike postLike) {
        postLikes.remove(postLike);
        postLike.delete();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
