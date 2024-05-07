package com.example.askme.api.controller.article.request;

import com.example.askme.api.service.article.request.ArticleServiceRequest;
import com.example.askme.common.constant.ContentStatus;
import com.example.askme.common.constant.SolveState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ArticleCreateRequest {
    @NotNull
    private Long accountId;

    @NotEmpty(message = "제목은 필수 입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 입니다.")
    private String content;

    @Builder.Default
    private SolveState state = SolveState.NOT_SOLVED_YET;

    @Builder.Default
    private ContentStatus status = ContentStatus.PUBLISH;

    private String imageUrl;

    @Builder.Default
    private long viewCount = 0L;

    @Builder.Default
    private long likeCount = 0L;

    private ArticleCreateRequest(Long accountId, String title, String content, SolveState state, ContentStatus status, String imageUrl, long viewCount, long likeCount) {
        this.accountId = accountId;
        this.title = title;
        this.content = content;
        this.state = state;
        this.status = status;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public ArticleServiceRequest toServiceRequest() {
        return ArticleServiceRequest.builder()
                .accountId(accountId)
                .title(title)
                .content(content)
                .state(state)
                .status(status)
                .imageUrl(imageUrl)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .build();
    }
}
