package com.example.askme.api.service.article;

import com.example.askme.api.service.article.request.ArticleServiceRequest;
import com.example.askme.api.service.article.response.ArticleServiceResponse;
import com.example.askme.common.error.ErrorCode;
import com.example.askme.common.error.exception.BusinessException;
import com.example.askme.dao.account.Account;
import com.example.askme.dao.account.AccountRepository;
import com.example.askme.dao.article.Article;
import com.example.askme.dao.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String POST_VIEW_COUNT_KEY = "post:view:count:";

    @Transactional
    public ArticleServiceResponse saveArticle(ArticleServiceRequest requestArticle) {
        Account account = accountRepository.findById(requestArticle.getAccountId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "계정 ID " + requestArticle.getAccountId() + "에 해당하는 작성자가 존재하지 않습니다."));
        return ArticleServiceResponse.of(articleRepository.save(requestArticle.toEntity(account)));
    }

    public ArticleServiceResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "존재하지 않는 게시글 입니다."));
        increaseViewCount(id);
        article.setViewCount(getViewCount(id));
        return ArticleServiceResponse.of(article);
    }

    public Page<ArticleServiceResponse> findAllArticles(int page) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlesPage = articleRepository.findAll(pageRequest);

        List<ArticleServiceResponse> articleServiceResponses = articlesPage.stream().map(article -> {
            article.setViewCount(getViewCount(article.getId()));
            return ArticleServiceResponse.of(article);
        }).collect(Collectors.toList());

        return new PageImpl<>(articleServiceResponses, pageRequest, articlesPage.getTotalElements());
    }

    @Transactional
    public ArticleServiceResponse updateArticle(Long id, ArticleServiceRequest serviceRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "수정할 게시글을 찾을 수 없습니다."));

        article.update(serviceRequest.getTitle(), serviceRequest.getContent());
        return ArticleServiceResponse.of(article);
    }

    @Transactional
    public ArticleServiceResponse deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "삭제할 게시글을 찾을 수 없습니다."));

        articleRepository.delete(article);
        redisTemplate.delete(POST_VIEW_COUNT_KEY + id);
        return ArticleServiceResponse.of(article);
    }

    @Transactional
    public ArticleServiceResponse likeArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "존재하지 않는 게시글 입니다"));

        article.increaseLikeCount();
        return ArticleServiceResponse.of(article);
    }

    @Transactional
    public ArticleServiceResponse dislikeArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "존재하지 않는 게시글 입니다"));

        article.decreaseLikeCount();
        return ArticleServiceResponse.of(article);
    }

    private void increaseViewCount(Long id) {
        String key = POST_VIEW_COUNT_KEY + id;
        redisTemplate.opsForValue().increment(key);
    }

    public Long getViewCount(Long postId) {
        String key = POST_VIEW_COUNT_KEY + postId;
        String countStr = redisTemplate.opsForValue().get(key);

        if (countStr == null) {
            Article article = articleRepository.findById(postId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, "존재하지 않는 게시글 입니다."));
            Long viewCount = article.getViewCount();
            redisTemplate.opsForValue().set(key, viewCount.toString());
            return viewCount;
        }

        return Long.parseLong(countStr);
    }
}

