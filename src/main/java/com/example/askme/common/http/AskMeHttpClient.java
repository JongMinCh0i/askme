package com.example.askme.common.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AskMeHttpClient {
    private final RestTemplate restTemplate;

    public AskMeHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getData(String uri, HttpMethod httpMethod, HttpHeaders headers) {
        return restTemplate.exchange(
                uri,
                httpMethod,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<String>() {
                }
        ).getBody();
    }

    public <T> T sendRequest(String uri, HttpMethod httpMethod, HttpHeaders headers, Object body, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(
                uri,
                httpMethod,
                entity,
                responseType
        ).getBody();
    }
}
