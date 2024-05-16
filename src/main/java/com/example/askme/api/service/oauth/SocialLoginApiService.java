package com.example.askme.api.service.oauth;

import com.example.askme.api.service.account.request.AccountServiceRequest;

public interface SocialLoginApiService {

    AccountServiceRequest getAccountInfo(String accessToken);
}
