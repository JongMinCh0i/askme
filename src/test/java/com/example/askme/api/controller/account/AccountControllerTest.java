package com.example.askme.api.controller.account;

import com.example.askme.api.controller.account.request.AccountCreateRequest;
import com.example.askme.api.service.account.AccountInfoService;
import com.example.askme.api.service.account.AccountService;
import com.example.askme.api.service.account.request.AccountServiceRequest;
import com.example.askme.api.service.account.response.AccountServiceResponse;
import com.example.askme.common.constant.Role;
import com.example.askme.common.interceptor.AuthenticationInterceptor;
import com.example.askme.common.interceptor.QuestionerAuthorizationInterceptor;
import com.example.askme.common.jwt.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenManager tokenManager;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountInfoService accountInfoService;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private QuestionerAuthorizationInterceptor questionerAuthorizationInterceptor;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);
        Mockito.when(questionerAuthorizationInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

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
