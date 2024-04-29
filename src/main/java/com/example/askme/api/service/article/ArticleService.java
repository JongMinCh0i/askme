package com.example.askme.api.service.article;

import com.example.askme.api.service.article.request.ArticleServiceRequest;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import com.example.askme.domain.account.Account;
import com.example.askme.domain.account.AccountRepository;
import com.example.askme.domain.article.Article;
import com.example.askme.domain.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void saveArticle(ArticleServiceRequest requestArticle) {
        Account account = accountRepository.findById(requestArticle.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계정이 존재하지 않습니다."));

        Article article = requestArticle.toEntity(account);
        articleRepository.save(article);
    }

    public ArticleServiceResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return ArticleServiceResponse.of(article);
    }

    public List<ArticleServiceResponse> findAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleServiceResponse::of)
                .collect(Collectors.toList());
    }
}

