package com.bonestew.popmate.popupstore.presentation;


import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.utils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@SpringBootTest
@WithMockCustomUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PopupStoreControllerTest {

    @MockBean
    private PopupStoreService popupStoreService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Disabled
    @Test
    void 팝업스토어목록을_조회한다() throws Exception {
        // Given
        PopupStoreSearchRequest searchRequest = new PopupStoreSearchRequest(
                false,
                LocalDate.of(2023, 8, 1),
                LocalDate.of(2023, 12, 23),
                "팝",
                0,
                1
        );
        List<PopupStore> popupStoreList = List.of(
                new PopupStore(
                        1L,
                        new User(),
                        new Department(),
                        new ChatRoom(),
                        "팝업스토어",
                        "주최자",
                        "상세 주소",
                        "설명",
                        "이벤트",
                        "https://example.com/banner.jpg",
                        0,
                        50,
                        true,
                        true,
                        30,
                        5,
                        10,
                        LocalDateTime.of(2023, 8, 23, 10, 0),
                        LocalDateTime.of(2023, 8, 30, 9, 0),
                        LocalDateTime.of(2023, 8, 30, 9, 0),
                        LocalDateTime.of(2023, 8, 30, 17, 0),
                        0L,
                        LocalDateTime.now(),
                        0)

        );


        // When
        given(popupStoreService.getPopupStores(false,
                "2023-08-01",
                "2024-08-01",
                "팝",
                0,
                0)).willReturn(popupStoreList);

        ResultActions result = mockMvc.perform(
                get("/api/v1/popup-stores")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(searchRequest))
        );

        // Then
        result
                .andExpect(status().isOk())
                .andDo(customDocument(
                        requestFields(
                                fieldWithPath("isOpeningSoon").type(JsonFieldType.BOOLEAN).description("오픈예정"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작날짜"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료날짜"),
                                fieldWithPath("keyword").type(JsonFieldType.STRING).description("검색 키워드"),
                                fieldWithPath("offSetRows").type(JsonFieldType.NUMBER).description("페이징 오프셋"),
                                fieldWithPath("rowsToGet").type(JsonFieldType.NUMBER).description("반환할 데이터 수")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.popupStores[].popupStoreId").type(JsonFieldType.NUMBER).description("예약 가능 여부"),
                                fieldWithPath("data.popupStores[].title").type(JsonFieldType.STRING).description("팝업스토어 이름"),
                                fieldWithPath("data.popupStores[].openDate").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("data.popupStores[].closeDate").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("data.popupStores[].placeDetail").type(JsonFieldType.STRING).description("상세 주소"),
                                fieldWithPath("data.popupStores[].bannerImgUrl").type(JsonFieldType.STRING).description("배너 이미지 URL"),
                                fieldWithPath("data.popupStores[].organizer").type(JsonFieldType.STRING).description("주최자")
                        )
                ));
    }


    @Disabled
    @Test
    void 팝업스토어를_조회한다() throws Exception {
        // given
        Long popupStoreId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 8, 30, 9, 0);

        PopupStore popupStore = new PopupStore(1L, new User(), new Department(), new ChatRoom(), "테스트 팝업 스토어", "주최자 이름",
                "장소 상세 정보", "설명", "이벤트 설명", "이미지 URL", 1000, 50, true, true, 30, 5, 10, LocalDateTime.of(2023, 8, 23, 10, 0),
                dateTime, dateTime, dateTime, 0L, dateTime, 0);

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
