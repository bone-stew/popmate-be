package com.bonestew.popmate.popupstore.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.security.domain.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PopupStoreControllerTest {

    @MockBean
    private PopupStoreService popupStoreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 팝업스토어를_조회한다() throws Exception {
        // given
        Long popupStoreId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 8, 30, 9, 0);

        PopupStore popupStore = new PopupStore(1L, new User(), new Department(), new ChatRoom(), "테스트 팝업 스토어", "주최자 이름",
            "장소 상세 정보", "설명", "이벤트 설명", "이미지 URL", 1000, 50, true, 30, 5, 10, LocalDateTime.of(2023, 8, 23, 10, 0),
            dateTime, dateTime, dateTime);

        // when
        given(popupStoreService.getPopupStore(popupStoreId)).willReturn(popupStore);

        ResultActions result = mockMvc.perform(
            get("/api/v1/popup-stores/{popupStoreId}", popupStoreId));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("popupStoreId").description("조회할 팝업스토어 id")
                ),
                responseFields(
                    fieldWithPath("code").description(ResultCode.SUCCESS.name()),
                    fieldWithPath("message").description(ResultCode.SUCCESS.getMessage()),
                    fieldWithPath("data.id").description(1L),
                    fieldWithPath("data.name").description("Sample Popup Store"),
                    fieldWithPath("data.description").description("Sample Description")
                )
            ));
    }
}
