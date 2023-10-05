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

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.Category;
import com.bonestew.popmate.popupstore.domain.CategoryType;
import com.bonestew.popmate.user.domain.Role;
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

import javax.swing.Popup;
import org.junit.jupiter.api.BeforeEach;
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

    private PopupStore popupStore;
    private List<PopupStore> popupStoreList;
    private List<Banner> bannerList;
    private User user;
    private PopmateUser popmateUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setNickname("test");
        user.setRole(Role.ROLE_USER);
        popmateUser = PopmateUser.from(user);
        popupStore =  new PopupStore(
                1L,
                new User(),
                new Department(1L, "department", "placeDescription", 12.1, 12.1, LocalDateTime.of(2023, 10, 4, 9, 0),
                               LocalDateTime.of(2024, 1, 1, 17, 0), LocalDateTime.now() ),
                new ChatRoom("1", "chatroom", LocalDateTime.now()),
                new Category(1L, CategoryType.POPUP_STORE),
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
                LocalDateTime.of(2023, 10, 4, 10, 0),
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2023, 10, 4, 9, 0),
                LocalDateTime.of(2024, 1, 1, 17, 0),
                0L,
                LocalDateTime.now(),
                0);
        bannerList = List.of(
                new Banner(1L, "img_url", popupStore, LocalDateTime.now())
        );
        popupStoreList = List.of( popupStore);

    }

//    @Disabled
    @Test
    void 팝업스토어목록을_조회한다() throws Exception {
        // Given
        PopupStoreSearchRequest searchRequest =
                new PopupStoreSearchRequest(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );


        // When
        given(popupStoreService.getPopupStores(null,
                                               null,
                                               null,
                                               null,
                                               null,
                                               null)).willReturn(popupStoreList);

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
                                fieldWithPath("isOpeningSoon").description("오픈예정"),
                                fieldWithPath("startDate").description("시작날짜"),
                                fieldWithPath("endDate").description("종료날짜"),
                                fieldWithPath("keyword").description("검색 키워드"),
                                fieldWithPath("offSetRows").description("페이징 오프셋"),
                                fieldWithPath("rowsToGet").description("반환할 데이터 수")
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
                                fieldWithPath("data.popupStores[].organizer").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.popupStores[].departmentName").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.popupStores[].categoryName").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("data.popupStores[].createdAt").type(JsonFieldType.STRING).description("생성날짜"),
                                fieldWithPath("data.popupStores[].total").type(JsonFieldType.NUMBER).description("조회수")




                        )
                ));
    }

    @Test
    void 팝업스토어_홈을_조회한다() throws Exception {
        given(popupStoreService.getBanners()).willReturn(bannerList);
        given(popupStoreService.getPopupStoresVisitedBy(user.getUserId())).willReturn(popupStoreList);
        given(popupStoreService.getPopupStoresRecommend()).willReturn(popupStoreList);
        given(popupStoreService.getPopupStoresEndingSoon()).willReturn(popupStoreList);


        ResultActions result = mockMvc.perform(
                get("/api/v1/popup-stores/home")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(popmateUser))
        );
        result
                .andExpect(status().isOk())
                .andDo(customDocument(
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.banners[].bannerId").type(JsonFieldType.NUMBER).description("예약 가능 여부"),
                                fieldWithPath("data.banners[].imgUrl").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.banners[].popupStoreId").type(JsonFieldType.NUMBER).description("예약 가능 여부"),


                                fieldWithPath("data.popupStoresVisitedBy[].popupStoreId").type(JsonFieldType.NUMBER).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresVisitedBy[].title").type(JsonFieldType.STRING).description("팝업스토어 이름"),
                                fieldWithPath("data.popupStoresVisitedBy[].bannerImgUrl").type(JsonFieldType.STRING).description("시작 시간"),

                                fieldWithPath("data.popupStoresRecommend[].popupStoreId").type(JsonFieldType.NUMBER).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].title").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].categoryName").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].openDate").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].closeDate").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].placeDetail").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].bannerImgUrl").type(JsonFieldType.STRING).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoresRecommend[].organizer").type(JsonFieldType.STRING).description("예약 가능 여부"),

                                fieldWithPath("data.popupStoresEndingSoon[].popupStoreId").type(JsonFieldType.NUMBER).description("종료 시간"),
                                fieldWithPath("data.popupStoresEndingSoon[].title").type(JsonFieldType.STRING).description("상세 주소"),
                                fieldWithPath("data.popupStoresEndingSoon[].categoryName").type(JsonFieldType.STRING).description("배너 이미지 URL"),
                                fieldWithPath("data.popupStoresEndingSoon[].openDate").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.popupStoresEndingSoon[].closeDate").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.popupStoresEndingSoon[].placeDetail").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("data.popupStoresEndingSoon[].bannerImgUrl").type(JsonFieldType.STRING).description("생성날짜"),
                                fieldWithPath("data.popupStoresEndingSoon[].organizer").type(JsonFieldType.STRING).description("조회수")

                        )
                ));

    }


    @Disabled
    @Test
    void 팝업스토어를_조회한다() throws Exception {
        // given
        Long popupStoreId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 8, 30, 9, 0);

        PopupStore popupStore = new PopupStore(1L, new User(), new Department(), new ChatRoom(), new Category(), "테스트 팝업 스토어", "주최자 이름",
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
