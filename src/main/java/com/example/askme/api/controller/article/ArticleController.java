package com.example.askme.api.controller.article;

import com.example.askme.api.controller.article.request.ArticleCreateRequest;
import com.example.askme.api.service.article.ArticleService;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/save")
    public ResponseEntity<ArticleServiceResponse> createArticle(@Valid @RequestBody ArticleCreateRequest createRequestArticle) {
        articleService.saveArticle(createRequestArticle.toServiceRequest());
        return ResponseEntity.ok().build();
    }

}
