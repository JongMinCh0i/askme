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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

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
    @DisplayName("게시글 전체 조회 테스트")
    void getAllArticles() throws Exception {

        //given
        ArticleServiceResponse expectedResponse = ArticleServiceResponse.builder()
                .articleId(1L)
                .accountId(2L)
                .title("Sample Title")
                .content("Sample Content")
                .state(SolveState.SOLVED)
                .status(ContentStatus.PUBLISH)
                .imageUrl("sample.jpg")
                .viewCount(10)
                .likeCount(5)
                .build();

        when(articleService.findAllArticles()).thenReturn(Collections.singletonList(expectedResponse));

        //when & then
        mockMvc.perform(get("/api/v1/article/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].articleId").value(expectedResponse.getArticleId()))
                .andExpect(jsonPath("$.data[0].accountId").value(expectedResponse.getAccountId()))
                .andExpect(jsonPath("$.data[0].title").value(expectedResponse.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(expectedResponse.getContent()))
                .andExpect(jsonPath("$.data[0].state").value(expectedResponse.getState().toString()))
                .andExpect(jsonPath("$.data[0].status").value(expectedResponse.getStatus().toString()))
                .andExpect(jsonPath("$.data[0].imageUrl").value(expectedResponse.getImageUrl()))
                .andExpect(jsonPath("$.data[0].viewCount").value(expectedResponse.getViewCount()))
                .andExpect(jsonPath("$.data[0].likeCount").value(expectedResponse.getLikeCount()));

        verify(articleService, times(1)).findAllArticles();
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
