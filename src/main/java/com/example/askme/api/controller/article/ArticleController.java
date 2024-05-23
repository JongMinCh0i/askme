package com.example.askme.api.controller.article;

import com.example.askme.api.controller.article.request.ArticleCreateRequest;
import com.example.askme.api.service.article.ArticleService;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import com.example.askme.common.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/create")
    public ResultResponse<ArticleServiceResponse> createArticle(@Valid @RequestBody ArticleCreateRequest createRequestArticle) {
        return ResultResponse.success(articleService.saveArticle(createRequestArticle.toServiceRequest()));
    }

    @GetMapping("/{id}")
    public ResultResponse<ArticleServiceResponse> getArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.findById(id);
        return ResultResponse.success(response);
    }

    @GetMapping("/list")
    public ResultResponse<Page<ArticleServiceResponse>> getAllArticles(
            @RequestParam(defaultValue = "0") int page) {
        return ResultResponse.success(articleService.findAllArticles(page));
    }

    @PutMapping("/{id}")
    public ResultResponse<ArticleServiceResponse> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleCreateRequest updateRequestArticle) {
        ArticleServiceResponse response = articleService.updateArticle(id, updateRequestArticle.toServiceRequest());
        return ResultResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ResultResponse<ArticleServiceResponse> deleteArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.deleteArticle(id);
        return ResultResponse.success(response);
    }

    @PostMapping("/{id}/like")
    public ResultResponse<ArticleServiceResponse> likeArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.likeArticle(id);
        return ResultResponse.success(response);
    }

    @PostMapping("/{id}/dislike")
    public ResultResponse<ArticleServiceResponse> dislikeArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.dislikeArticle(id);
        return ResultResponse.success(response);
    }
}
