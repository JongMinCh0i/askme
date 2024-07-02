package com.example.askme.api.service.article;

import com.example.askme.dao.article.ArticleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final ArticleRepository articleRepository;
    private static final String POST_VIEW_COUNT_KEY = "post:view:count:";

    @PostConstruct
    public void init() {
        log.info("조회수 동기화 스케쥴러 시작");
        syncViewCounts();
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *") // 매 1분마다 실행
    public void syncViewCounts() {
        Set<String> keys = redisTemplate.keys(POST_VIEW_COUNT_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                try {
                    Long postId = Long.parseLong(key.replace(POST_VIEW_COUNT_KEY, ""));
                    String viewCountStr = redisTemplate.opsForValue().get(key);
                    if (viewCountStr != null) {
                        Long viewCount = Long.parseLong(viewCountStr);
                        articleRepository.updateViewCount(postId, viewCount);
                        log.info("포스트 ID {}, 조회수 {} 업데이트 완료.", postId, viewCount);
                    } else {
                        log.warn("키에 대한 조회수 값이 null입니다: {}", key);
                    }
                } catch (Exception e) {
                    log.error("조회수 동기화 중 오류가 발생했습니다: {}", key, e);
                }
            }
        }
    }
}
