package com.example.askme.api.controller.article;

import com.example.askme.api.controller.article.request.ArticleCreateRequest;
import com.example.askme.api.service.article.ArticleService;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import com.example.askme.common.constant.ContentStatus;
import com.example.askme.common.constant.SolveState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(ArticleController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 생성 테스트")
    void createArticle() throws Exception {

        //given
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(2L)
                .accountId(1L)
                .title("제목 테스트")
                .content("내용 테스트")
                .state(SolveState.NOT_SOLVED_YET)
                .status(ContentStatus.PUBLISH)
                .imageUrl("")
                .viewCount(0)
                .likeCount(0)
                .build();

        when(articleService.saveArticle(any())).thenReturn(expectedResponse);

        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .accountId(1L)
                .title("제목 테스트")
                .content("내용 테스트")
                .state(SolveState.NOT_SOLVED_YET)
                .status(ContentStatus.PUBLISH)
                .imageUrl("")
                .viewCount(0)
                .likeCount(0)
                .build();

        String requestJson = new ObjectMapper().writeValueAsString(request);

        //when & then
        mockMvc.perform(post("/api/v1/article/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void getArticle() throws Exception {

        //given
        long articleId = 1L;
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(articleId)
                .accountId(2L)
                .title("Sample Title")
                .content("Sample Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.findById(articleId)).thenReturn(expectedResponse);

        //when & then
        mockMvc.perform(get("/api/v1/article/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).findById(articleId);
    }

    @Test
    @DisplayName("게시글 전체 조회 시 페이지네이션 조회 여부 테스트")
    void getAllArticlesWithPagination() throws Exception {

        //given
        ArticleServiceResponse expectedResponse1 = ArticleServiceResponse.builder()
                .articleId(1L)
                .accountId(2L)
                .title("Sample Title 1")
                .content("Sample Content 1")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample1.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        ArticleServiceResponse expectedResponse2 = ArticleServiceResponse.builder()
                .articleId(2L)
                .accountId(3L)
                .title("Sample Title 2")
                .content("Sample Content 2")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample2.jpg")
                .viewCount(20)
                .likeCount(15)
                .build();

        ArticleServiceResponse expectedResponse3 = ArticleServiceResponse.builder()
                .articleId(3L)
                .accountId(4L)
                .title("Sample Title 3")
                .content("Sample Content 3")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample3.jpg")
                .viewCount(30)
                .likeCount(25)
                .build();

        // 1번 페이지
        List<ArticleServiceResponse> firstPageArticles = Arrays.asList(expectedResponse1, expectedResponse2);
        Page<ArticleServiceResponse> firstPage = new PageImpl<>(firstPageArticles, PageRequest.of(0, 2), 3);

        // 2번 페이지
        List<ArticleServiceResponse> secondPageArticles = Collections.singletonList(expectedResponse3);
        Page<ArticleServiceResponse> secondPage = new PageImpl<>(secondPageArticles, PageRequest.of(1, 2), 3);

        when(articleService.findAllArticles(0)).thenReturn(firstPage);
        when(articleService.findAllArticles(1)).thenReturn(secondPage);

        //when & then
        mockMvc.perform(get("/api/v1/article/list")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].articleId").value(expectedResponse1.getArticleId()))
                .andExpect(jsonPath("$.data.content[0].accountId").value(expectedResponse1.getAccountId()))
                .andExpect(jsonPath("$.data.content[0].title").value(expectedResponse1.getTitle()))
                .andExpect(jsonPath("$.data.content[0].content").value(expectedResponse1.getContent()))
                .andExpect(jsonPath("$.data.content[0].state").value(expectedResponse1.getState().toString()))
                .andExpect(jsonPath("$.data.content[0].status").value(expectedResponse1.getStatus().toString()))
                .andExpect(jsonPath("$.data.content[0].imageUrl").value(expectedResponse1.getImageUrl()))
                .andExpect(jsonPath("$.data.content[0].viewCount").value(expectedResponse1.getViewCount()))
                .andExpect(jsonPath("$.data.content[0].likeCount").value(expectedResponse1.getLikeCount()))
                .andExpect(jsonPath("$.data.content[1].articleId").value(expectedResponse2.getArticleId()))
                .andExpect(jsonPath("$.data.content[1].accountId").value(expectedResponse2.getAccountId()))
                .andExpect(jsonPath("$.data.content[1].title").value(expectedResponse2.getTitle()))
                .andExpect(jsonPath("$.data.content[1].content").value(expectedResponse2.getContent()))
                .andExpect(jsonPath("$.data.content[1].state").value(expectedResponse2.getState().toString()))
                .andExpect(jsonPath("$.data.content[1].status").value(expectedResponse2.getStatus().toString()))
                .andExpect(jsonPath("$.data.content[1].imageUrl").value(expectedResponse2.getImageUrl()))
                .andExpect(jsonPath("$.data.content[1].viewCount").value(expectedResponse2.getViewCount()))
                .andExpect(jsonPath("$.data.content[1].likeCount").value(expectedResponse2.getLikeCount()));

        mockMvc.perform(get("/api/v1/article/list")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].articleId").value(expectedResponse3.getArticleId()))
                .andExpect(jsonPath("$.data.content[0].accountId").value(expectedResponse3.getAccountId()))
                .andExpect(jsonPath("$.data.content[0].title").value(expectedResponse3.getTitle()))
                .andExpect(jsonPath("$.data.content[0].content").value(expectedResponse3.getContent()))
                .andExpect(jsonPath("$.data.content[0].state").value(expectedResponse3.getState().toString()))
                .andExpect(jsonPath("$.data.content[0].status").value(expectedResponse3.getStatus().toString()))
                .andExpect(jsonPath("$.data.content[0].imageUrl").value(expectedResponse3.getImageUrl()))
                .andExpect(jsonPath("$.data.content[0].viewCount").value(expectedResponse3.getViewCount()))
                .andExpect(jsonPath("$.data.content[0].likeCount").value(expectedResponse3.getLikeCount()));

        verify(articleService, times(1)).findAllArticles(0);
        verify(articleService, times(1)).findAllArticles(1);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updateArticle() throws Exception {
        long articleId = 1L;

        //given
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(articleId)
                .accountId(1L)
                .title("Updated Title")
                .content("Updated Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("updated.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.updateArticle(eq(articleId), any())).thenReturn(expectedResponse);

        ArticleCreateRequest updateRequest = ArticleCreateRequest.builder()
                .accountId(1L)
                .title("Updated Title")
                .content("Updated Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("updated.jpg")
                .build();

        String requestJson = new ObjectMapper().writeValueAsString(updateRequest);

        //when & then
        mockMvc.perform(put("/api/v1/article/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).updateArticle(eq(articleId), any());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteArticle() throws Exception {
        long articleId = 1L;

        //given
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(articleId)
                .accountId(2L)
                .title("Sample Title")
                .content("Sample Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.deleteArticle(articleId)).thenReturn(expectedResponse);

        //when & then
        mockMvc.perform(delete("/api/v1/article/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).deleteArticle(articleId);
    }

    @Test
    @DisplayName("게시글 좋아요 테스트")
    void likeArticle() throws Exception {

        //given
        long articleId = 1L;
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(articleId)
                .accountId(2L)
                .title("Sample Title")
                .content("Sample Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.likeArticle(articleId)).thenReturn(expectedResponse);

        //when & then
        mockMvc.perform(post("/api/v1/article/{id}/like", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).likeArticle(articleId);
    }

    @Test
    @DisplayName("게시글 싫어요 테스트")
    void dislikeArticle() throws Exception {

        //given
        long articleId = 1L;
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(articleId)
                .accountId(2L)
                .title("Sample Title")
                .content("Sample Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.dislikeArticle(articleId)).thenReturn(expectedResponse);

        //when & then
        mockMvc.perform(post("/api/v1/article/{id}/dislike", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data.accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data.title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data.content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data.state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data.status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data.imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data.viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data.likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).dislikeArticle(articleId);
    }
}
