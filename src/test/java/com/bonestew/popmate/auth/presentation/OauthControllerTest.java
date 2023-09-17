package com.bonestew.popmate.auth.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.auth.application.OauthService;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class OauthControllerTest {
    @MockBean
    private OauthService oauthService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 카카오_로그인을_한다() throws Exception {
        String code = "dsdsddw";
        given(oauthService.loginKakao(any())).willReturn("jwtAuthenticationResponse");

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);

        ResultActions result = mockMvc.perform(
            post("/api/v1/oauth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBody))
        );

        result.andExpect(status().isOk())
            .andDo(customDocument(
                requestFields(
                    fieldWithPath("code").description("인가코드")
                ),
                responseFields(
                    fieldWithPath("code").description(ResultCode.SUCCESS.name()),
                    fieldWithPath("message").description(ResultCode.SUCCESS.getMessage()),
                    fieldWithPath("data.token").description("JWT토큰")
                )
            ));
    }


    @Test
    void 구글_로그인을_한다() throws Exception{
        User user = new User();
        user.setEmail("frogs6225@naver.com");
        user.setName("조재룡");

        // ObjectMapper를 이용하여 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String oauthUserJson = objectMapper.writeValueAsString(user);

        given(oauthService.loginGoogle(any())).willReturn("jwtAuthenticationResponse");

        ResultActions result = mockMvc.perform(
            post("/api/v1/oauth/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(oauthUserJson)
            );

        result.andExpect(status().isOk())
            .andDo(customDocument(
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("name").description("이름"),
                    fieldWithPath("nickname").description("닉네임"),
                    fieldWithPath("provider").description("제공자"),
                    fieldWithPath("createdAt").description("생성일").optional(), // optional 필드로 처리
                    fieldWithPath("userId").description("사용자 ID").optional(), // optional 필드로 처리
                    fieldWithPath("password").description("비밀번호").optional(), // optional 필드로 처리
                    fieldWithPath("role").description("역할").optional() // optional 필드로 처리
                ),
                responseFields(
                    fieldWithPath("code").description(ResultCode.SUCCESS.name()),
                    fieldWithPath("message").description(ResultCode.SUCCESS.getMessage()),
                    fieldWithPath("data.token").description("JWT토큰")
                )
            ));
    }


}
