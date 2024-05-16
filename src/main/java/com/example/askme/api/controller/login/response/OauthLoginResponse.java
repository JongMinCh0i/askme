package com.example.askme.api.controller.login.response;

import com.example.askme.common.jwt.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginResponse {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date accessTokenExpiresIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date refreshTokenExpiresIn;


    public static OauthLoginResponse of(JwtTokenDto jwtTokenDto) {
        return OauthLoginResponse.builder()
                .grantType(jwtTokenDto.getGrantType())
                .accessToken(jwtTokenDto.getAccessToken())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .accessTokenExpiresIn(jwtTokenDto.getAccessTokenExpiresTime())
                .refreshTokenExpiresIn(jwtTokenDto.getRefreshTokenExpiresTime())
                .build();
    }
}
