package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponse {

    private final Long postId;

    private final String postTitle;

    private final String postCategory;

    private final int postComments;

    private final int postLikes;

    private final LocalDateTime postTime;

    private final boolean liked;

    private final boolean updated;

    private final boolean reported;

    private final int viewCount;


    @Builder
    public PostResponse(Long postId, String postTitle, String postCategory, int postComments, int postLikes, LocalDateTime postTime, boolean liked, boolean updated, boolean reported, int viewCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postCategory = postCategory;
        this.postComments = postComments;
        this.postLikes = postLikes;
        this.postTime = postTime;
        this.liked = liked;
        this.updated = updated;
        this.reported = reported;
        this.viewCount = viewCount;
    }

    public static PostResponse of(Post post, boolean liked) {
        return PostResponse.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postCategory(post.getCategory().getValue())
                .postComments(post.getCommentCount())
                .postLikes(post.getLikeCount())
                .postTime(post.getCreatedAt())
                .liked(liked)
                .updated(post.isUpdated())
                .reported(post.isReportedTooMuch())
                .viewCount(post.getViewCount())
                .build();
    }

}
