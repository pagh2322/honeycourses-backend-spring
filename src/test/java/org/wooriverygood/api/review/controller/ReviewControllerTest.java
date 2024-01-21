package org.wooriverygood.api.review.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewLikeResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.util.ControllerTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class ReviewControllerTest extends ControllerTest {
    List<ReviewResponse> responses = new ArrayList<>();

    Courses course= Courses.builder()
            .id(1L)
            .course_name("Gaoshu")
            .course_category("Zhuanye")
            .course_credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .build();

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    @BeforeEach
    void setUp() {
        for(int i = 1; i <= 2; i++) {
            responses.add(ReviewResponse.builder()
                    .review_id((long)i)
                    .course_id(1L)
                    .review_content("Test Review " + i)
                    .review_title("Title" + i)
                    .instructor_name("Jiaoshou")
                    .taken_semyr("22-23")
                    .grade("60")
                    .review_time(LocalDateTime.now())
                    .isMine(false)
                    .build());
        }
    }

    @Test
    @DisplayName("특정 강의의 리뷰 조회 요청을 받으면 리뷰들을 반환한다.")
    void findAllReviewsByCourseId() {
        Mockito.when(reviewService.findAllReviewsByCourseId(any(), any(AuthInfo.class)))
                .thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses/1/reviews")
                .then().log().all()
                .apply(document("reviews/find/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 강의의 리뷰를 작성한다.")
    void addReview() {
        NewReviewRequest request = NewReviewRequest.builder()
                .review_title("Test Review from TestCode")
                .instructor_name("Jiaoshou")
                .taken_semyr("1stSem")
                .review_content("Good!")
                .grade("100")
                .build();

        NewReviewResponse response = NewReviewResponse.builder()
                .review_id(50L)
                .author_email(authInfo.getUsername())
                .review_title("Test Review from TestCode")
                .instructor_name("Jiaoshou")
                .taken_semyr("1stSem")
                .review_content("Good!")
                .grade("100")
                .build();

        Mockito.when(reviewService.addReview(any(AuthInfo.class), any(Long.class), any(NewReviewRequest.class)))
                .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/courses/50/reviews")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 올리거나 내린다.")
    void likeReview() {
        Mockito.when(reviewService.likeReview(any(Long.class), any(AuthInfo.class)))
                .thenReturn(ReviewLikeResponse.builder()
                        .like_count(5)
                        .liked(false)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/courses/1/reviews/1")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/like/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자 본인이 작성한 리뷰들을 불러온다.")
    void findMyReviews() {
        List<ReviewResponse> responses = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            responses.add(ReviewResponse.builder()
                    .review_id((long)i)
                    .course_id(1L)
                    .review_content("Test Review " + i)
                    .review_title("Title" + i)
                    .instructor_name("Jiaoshou")
                    .taken_semyr("22-23")
                    .grade("60")
                    .review_time(LocalDateTime.now())
                    .isMine(true)
                    .build());
        }

        Mockito.when(reviewService.findMyReviews(any(AuthInfo.class)))
                .thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses/reviews/me")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/find/me/success"))
                .statusCode(HttpStatus.OK.value());
    }

}
