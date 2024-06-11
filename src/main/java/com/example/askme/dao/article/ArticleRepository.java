package com.example.askme.dao.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.viewCount = :viewCount WHERE a.id = :postId")
    void updateViewCount(@Param("postId") Long postId, @Param("viewCount") Long viewCount);
}
