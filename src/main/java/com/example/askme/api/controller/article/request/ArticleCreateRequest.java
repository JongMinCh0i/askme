package com.example.askme.api.controller.article.request;

import com.example.askme.api.service.article.request.ArticleServiceRequest;
import com.example.askme.dao.constant.ContentStatus;
import com.example.askme.dao.constant.SolveState;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
