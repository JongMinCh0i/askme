package com.example.askme.api.service.article.request;

import com.example.askme.domain.account.Account;
import com.example.askme.domain.article.Article;
import com.example.askme.domain.constant.ContentStatus;
import com.example.askme.domain.constant.SolveState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleServiceRequest {

    private Long accountId;
    private String title;
    private String content;
    private SolveState state;
    private ContentStatus status;
    private String imageUrl;
    private long viewCount;
    private long likeCount;

    public Article toEntity(Account account) {
        return Article.createArticle(
                account,
                this.title,
                this.content,
                this.state,
                this.status,
                this.imageUrl,
                this.viewCount,
                this.likeCount
        );
    }
}
