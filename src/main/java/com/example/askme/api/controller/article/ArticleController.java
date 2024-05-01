package com.example.askme.api.controller.article;

import com.example.askme.api.controller.article.request.ArticleCreateRequest;
import com.example.askme.api.service.article.ArticleService;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<ArticleServiceResponse> createArticle(@Valid @RequestBody ArticleCreateRequest createRequestArticle) {
        articleService.saveArticle(createRequestArticle.toServiceRequest());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleServiceResponse> getArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ArticleServiceResponse>> getAllArticles() {
        List<ArticleServiceResponse> responses = articleService.findAllArticles();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleServiceResponse> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleCreateRequest updateRequestArticle) {
        ArticleServiceResponse response = articleService.updateArticle(id, updateRequestArticle.toServiceRequest());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleServiceResponse> deleteArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.deleteArticle(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ArticleServiceResponse> likeArticle(@PathVariable Long id) {
        ArticleServiceResponse response = articleService.likeArticle(id);
        return ResponseEntity.ok(response);
    }
}
