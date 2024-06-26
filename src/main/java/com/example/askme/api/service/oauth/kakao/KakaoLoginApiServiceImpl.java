package com.example.askme.api.service.oauth.kakao;

import com.example.askme.api.service.oauth.kakao.dto.KakaoUserInfoResponseDto;
import com.example.askme.api.service.oauth.SocialLoginApiService;
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
        String email = kakaoAccount.getEmail();
        String nickname = kakaoAccount.getProfile().getNickname();
        String imageUrl = kakaoAccount.getProfile().getThumbnailImageUrl();

        return AccountServiceRequest.builder()
                .email(StringUtils.hasText(email) ? email : "unknownEmail")
                .nickname(StringUtils.hasText(nickname) ? nickname : "unknownName")
                .imageUrl(imageUrl)
                .loginType(LoginType.KAKAO)
                .build();
    }
}
