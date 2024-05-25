package com.example.askme.common.config;

import com.example.askme.common.interceptor.AuthenticationInterceptor;
import com.example.askme.common.interceptor.QuestionerAuthorizationInterceptor;
import com.example.askme.common.resolver.accountInfo.AccountArgumentResolver;
import com.example.askme.common.resolver.tokenInfo.TokenArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final QuestionerAuthorizationInterceptor questionerAuthorizationInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;
    private final TokenArgumentResolver tokenArgumentResolver;
    private final AccountArgumentResolver accountArgumentResolver;

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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenArgumentResolver);
        resolvers.add(accountArgumentResolver);
    }
}
