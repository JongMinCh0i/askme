package com.example.askme.api.service.account.oauth.service;

import com.example.askme.api.service.account.request.AccountServiceRequest;

public interface SocialLoginApiService {

    AccountServiceRequest getAccountInfo(String accessToken);
}
