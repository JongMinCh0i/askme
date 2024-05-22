package com.example.askme.api.service.oauth.kakao;

import com.example.askme.api.service.oauth.kakao.dto.KakaoUserInfoResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class KakaoAccountInfoClient {

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    public KakaoUserInfoResponseDto getAccountInfo(String contentType, String accessToken) {

        URI uri = UriComponentsBuilder.fromUriString(kakaoApiUrl)
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("Content-type", contentType);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<KakaoUserInfoResponseDto> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                KakaoUserInfoResponseDto.class);

        return response.getBody();
    }
}
