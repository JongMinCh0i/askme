package com.example.askme.common.resolver.accountInfo;

import com.example.askme.common.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountDto {

    private Long accountId;
    private Role role;

}
