package com.example.askme.common.resolver.tokenInfo;

import com.example.askme.common.util.AuthorizationHeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(TokenParser.class);
        boolean hasMemberInfoDto = TokenDto.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasMemberInfoDto;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String authorizationHeader = nativeRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorizationHeader(authorizationHeader);
        String token = authorizationHeader.split(" ")[1];

        return TokenDto.builder()
                .token(token)
                .build();
    }
}
