package com.example.askme.api.controller.account;

import com.example.askme.api.controller.account.request.AccountCreateRequest;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.api.service.account.request.AccountServiceRequest;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.common.constant.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignUp() throws Exception {

        AccountCreateRequest request = AccountCreateRequest.builder()
                .nickname("nickname")
                .userId("userId")
                .email("email@example.com")
                .password("password")
                .build();

        AccountServiceResponse response = AccountServiceResponse.builder()
                .id(1L)
                .userId(request.getUserId())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .questionCount(0)
                .imageUrl(null)
                .role(Role.QUESTIONER)
                .build();

        given(accountService.signUp(any(AccountServiceRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/account/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value("userId"))
                .andExpect(jsonPath("$.data.nickname").value("nickname"))
                .andExpect(jsonPath("$.data.email").value("email@example.com"))
                .andExpect(jsonPath("$.data.questionCount").value(0))
                .andExpect(jsonPath("$.data.role").value("QUESTIONER"))
                .andExpect(jsonPath("$.data.imageUrl").isEmpty());
    }
}
