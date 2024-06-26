package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.report.domain.PostReport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_category")
    private PostCategory category;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    @OneToMany(mappedBy = "post")
    private List<PostReport> reports = new ArrayList<>();

    @Column(name = "report_count")
    @ColumnDefault("0")
    private int reportCount;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private int viewCount;

    @ColumnDefault("false")
    private boolean updated;

    @Column(name = "post_time")
    @CreatedDate
    private LocalDateTime createdAt;


    @Builder
    public Post(Long id, PostCategory category, String title, String content,
                Member member, List<Comment> comments, List<PostLike> postLikes,
                List<PostReport> reports, boolean updated) {
        this.id = id;
        this.category = category;
        this.title = new Title(title);
        this.content = new Content(content);
        this.member = member;
        this.comments = comments;
        this.postLikes = postLikes;
        this.reports = reports;
        this.updated = updated;
    }

    public void validateAuthor(Member member) {
        this.member.verify(member);
    }

    public boolean sameAuthor(Member member) {
        return this.member.isSame(member);
    }

    public void addPostLike(PostLike postLike) {
        postLikes.add(postLike);
    }

    public void deletePostLike(PostLike postLike) {
        postLikes.remove(postLike);
        postLike.delete();
    }

    public void updateTitle(String title) {
        this.title = new Title(title);
        updated = true;
    }

    public void addReport(PostReport report) {
        reports.add(report);
    }

    public void updateContent(String content) {
        this.content = new Content(content);
        updated = true;
    }

    public int getCommentCount() {
        if (comments == null)
            return 0;
        return comments.size();
    }

    public boolean hasReportByMember(Member member) {
        for (PostReport report: reports)
            if (report.isOwner(member))
                return true;
        return false;
    }

    public String getTitle() {
        return isReportedTooMuch() ? null : title.getValue();
    }

    public String getContent() {
        return isReportedTooMuch() ? null : content.getValue();
    }

    public boolean isReportedTooMuch() {
        return reportCount >= 5;
    }

}
