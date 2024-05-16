package com.example.askme.api.service.oauth;

import com.example.askme.common.constant.LoginType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {

    private static Map<String, SocialLoginApiService> socialLoginApiServiceMap;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServiceMap) {
        SocialLoginApiServiceFactory.socialLoginApiServiceMap = socialLoginApiServiceMap;
    }

    public static SocialLoginApiService getSocialLoginApiService(LoginType loginType) {
        String socialLoginApiServiceBeanName = "";

        if(LoginType.KAKAO.equals(loginType)) {
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        }

        return socialLoginApiServiceMap.get(socialLoginApiServiceBeanName);
    }
}
