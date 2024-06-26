package org.wooriverygood.api.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;


public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    Page<Post> findAllByCategoryOrderByIdDesc(PostCategory category, Pageable pageable);

    Page<Post> findByMemberOrderByIdDesc(Member member, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseLikeCount(Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count - 1 WHERE post_id = :postId", nativeQuery = true)
    void decreaseLikeCount(Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET report_count = report_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseReportCount(Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET view_count = view_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseViewCount(Long postId);

}
