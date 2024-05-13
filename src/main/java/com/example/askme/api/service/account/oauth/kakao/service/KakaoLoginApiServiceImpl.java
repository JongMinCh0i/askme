package com.example.askme.api.service.account.oauth.kakao.service;

import com.example.askme.api.service.account.oauth.kakao.dto.KakaoUserInfoResponseDto;
import com.example.askme.api.service.account.oauth.service.SocialLoginApiService;
import com.example.askme.api.service.account.request.AccountServiceRequest;
import com.example.askme.common.constant.LoginType;
import com.example.askme.common.jwt.constatnt.GrantType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakaoLoginApiServiceImpl implements SocialLoginApiService {

    private final KakaoAccountInfoClient kakaoAccountInfoClient;
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public AccountServiceRequest getAccountInfo(String accessToken) {

        KakaoUserInfoResponseDto accountInfo = kakaoAccountInfoClient.getAccountInfo(
                CONTENT_TYPE, GrantType.BEARER.getType() + " " + accessToken);

        KakaoUserInfoResponseDto.KakaoAccount kakaoAccount = accountInfo.getKakaoAccount();
        String nickname = kakaoAccount.getProfile().getNickname();

        return AccountServiceRequest.builder()
                .email(!StringUtils.hasText(nickname) ? kakaoAccount.getProfile().getNickname() : "unknown")
                .nickname(kakaoAccount.getProfile().getNickname())
                .imageUrl(kakaoAccount.getProfile().getThumbnailImageUrl())
                .loginType(LoginType.KAKAO)
                .build();

    }
}
