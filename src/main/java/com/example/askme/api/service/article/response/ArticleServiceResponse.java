package com.example.askme.api.service.article.response;

import com.example.askme.domain.article.Article;
import com.example.askme.domain.constant.ContentStatus;
import com.example.askme.domain.constant.SolveState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleServiceResponse {

    private Long articleId;
    private Long accountId;
    private String title;
    private String content;
    private SolveState state;
    private ContentStatus status;
    private String imageUrl;
    private long viewCount;
    private long likeCount;


    @Builder
    private ArticleServiceResponse(Long articleId,
                                   Long accountId,
                                   String title,
                                   String content,
                                   SolveState state,
                                   ContentStatus status,
                                   String imageUrl,
                                   long viewCount,
                                   long likeCount
    ) {

        this.articleId = articleId;
        this.accountId = accountId;
        this.title = title;
        this.content = content;
        this.state = state;
        this.status = status;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static ArticleServiceResponse of(Article article) {
        return ArticleServiceResponse.builder()
                .articleId(article.getId())
                .accountId(article.getAccount().getId())
                .title(article.getTitle())
                .content(article.getContent())
                .state(article.getState())
                .status(article.getStatus())
                .imageUrl(article.getImageUrl())
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .build();
    }
}
