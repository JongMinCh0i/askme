package com.example.askme.common.config;

import com.example.askme.common.interceptor.AuthenticationInterceptor;
import com.example.askme.common.interceptor.QuestionerAuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    private final QuestionerAuthorizationInterceptor questionerAuthorizationInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/oauth/login",
                        "/api/access-token/issue",
                        "/api/logout");


        registry.addInterceptor(questionerAuthorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/question/**");

    }
}
