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
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfo;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfoResponse;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
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
import net.bytebuddy.asm.Advice.Local;
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

    @Test
    void 팝업스토어_상세정보를_조회한다() throws Exception {
        PopupStoreDetailDto popupStoreDetailDto = new PopupStoreDetailDto(
                popupStore,
                new Department(1L, "department", "placeDescription", 12.1, 12.1, LocalDateTime.of(2023, 10, 4, 9, 0),
                               LocalDateTime.of(2024, 1, 1, 17, 0), LocalDateTime.now() ),
                UserReservationStatus.RESERVED,
                new PopupStoreSns(popupStore, 1L, "instagram", "url", LocalDateTime.now()),
                new PopupStoreImg(popupStore, 1L, "imgurl", LocalDateTime.now())
        );
        List<PopupStoreDetailDto> popupStoreDetailDtoList = List.of(popupStoreDetailDto);

        given(popupStoreService.getPopupStoreDetail(popupStore.getPopupStoreId(), user.getUserId())).willReturn(popupStoreDetailDtoList);
        given(popupStoreService.getPopupStoresInDepartment(popupStore.getPopupStoreId())).willReturn(popupStoreList);

        ResultActions result = mockMvc.perform(
                get("/api/v1/popup-stores/{popupStoreId}", popupStore.getPopupStoreId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(popmateUser))
        );
        result
                .andExpect(status().isOk())
                .andDo(customDocument(
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.popupStoreId").type(JsonFieldType.NUMBER).description("팝업스토어 아이디"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("팝업스토어 제목"),
                                fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("팝업스토어 카테고리명"),
                                fieldWithPath("data.organizer").type(JsonFieldType.STRING).description("팝업스토어 주최사"),
                                fieldWithPath("data.placeDetail").type(JsonFieldType.STRING).description("팝업스토어 상세위치"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("팝업스토어 상세정보"),
                                fieldWithPath("data.eventDescription").type(JsonFieldType.STRING).description("팝업스토어 이벤트"),
                                fieldWithPath("data.bannerImgUrl").type(JsonFieldType.STRING).description("팝업스토어 배너 이미지"),
                                fieldWithPath("data.openDate").type(JsonFieldType.STRING).description("팝업스토어 시작일"),
                                fieldWithPath("data.closeDate").type(JsonFieldType.STRING).description("팝업스토어 종료일"),
                                fieldWithPath("data.openTime").type(JsonFieldType.STRING).description("팝업스토어 시작시간"),
                                fieldWithPath("data.closeTime").type(JsonFieldType.STRING).description("팝업스토어 종료시간"),
                                fieldWithPath("data.status").type(JsonFieldType.NUMBER).description("팝업스토어 상태"),
                                fieldWithPath("data.entryFee").type(JsonFieldType.NUMBER).description("팝업스토어 입장료"),
                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("팝업스토어 조회수"),
                                fieldWithPath("data.reservationEnabled").type(JsonFieldType.BOOLEAN).description("팝업스토어 예약시스템"),
                                fieldWithPath("data.department.departmentId").type(JsonFieldType.NUMBER).description("백화점 아이디"),
                                fieldWithPath("data.department.name").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.department.placeDescription").type(JsonFieldType.STRING).description("백화점 위치"),
                                fieldWithPath("data.department.latitude").type(JsonFieldType.NUMBER).description("백화점 위도"),
                                fieldWithPath("data.department.longitude").type(JsonFieldType.NUMBER).description("백화점 경도"),
                                fieldWithPath("data.department.openTime").type(JsonFieldType.STRING).description("백화점 시작시간"),
                                fieldWithPath("data.department.closeTime").type(JsonFieldType.STRING).description("백화점 종료시간"),
                                fieldWithPath("data.department.createdAt").type(JsonFieldType.STRING).description("백화점 생성시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.popupStoreId").type(JsonFieldType.NUMBER).description("팝업스토어 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.userId").type(JsonFieldType.NULL).description("사용자 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.email").type(JsonFieldType.NULL).description("사용자 이메일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.password").type(JsonFieldType.NULL).description("사용자 비밀번호"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.provider").type(JsonFieldType.NULL).description("사용자 제공 업체"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.role").type(JsonFieldType.NULL).description("사용자 역할"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.name").type(JsonFieldType.NULL).description("사용자 이름"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.nickname").type(JsonFieldType.NULL).description("사용자 닉네임"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.user.createdAt").type(JsonFieldType.NULL).description("사용자 생성일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.departmentId").type(JsonFieldType.NUMBER).description("백화점 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.name").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.placeDescription").type(JsonFieldType.STRING).description("백화점 위치"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.latitude").type(JsonFieldType.NUMBER).description("백화점 위도"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.longitude").type(JsonFieldType.NUMBER).description("백화점 경도"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.openTime").type(JsonFieldType.STRING).description("백화점 시작시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.closeTime").type(JsonFieldType.STRING).description("백화점 종료시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.department.createdAt").type(JsonFieldType.STRING).description("백화점 생성시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.chatRoomId.roomId").type(JsonFieldType.STRING).description("채팅 룸 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.chatRoomId.name").type(JsonFieldType.STRING).description("채팅 룸 이름"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.chatRoomId.createdAt").type(JsonFieldType.STRING).description("채팅 룸 생성일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.category.categoryId").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.category.categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.organizer").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.placeDetail").type(JsonFieldType.STRING).description("상세 위치"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.description").type(JsonFieldType.STRING).description("상세 정보"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.eventDescription").type(JsonFieldType.STRING).description("이벤트"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.bannerImgUrl").type(JsonFieldType.STRING).description("배너 이미지 URL"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.entryFee").type(JsonFieldType.NUMBER).description("입장료"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.maxCapacity").type(JsonFieldType.NUMBER).description("최대 수용 인원"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.reservationEnabled").type(JsonFieldType.BOOLEAN).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.salesEnabled").type(JsonFieldType.BOOLEAN).description("판매 가능 여부"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.reservationInterval").type(JsonFieldType.NUMBER).description("예약 간격"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.intervalCapacity").type(JsonFieldType.NUMBER).description("간격당 수용 인원"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.teamSizeLimit").type(JsonFieldType.NUMBER).description("팀 크기 제한"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.openDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.closeDate").type(JsonFieldType.STRING).description("종료일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.openTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.closeTime").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.views").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("data.popupStoreSnsResponses[].popupStore.total").type(JsonFieldType.NUMBER).description("총합"),
                                fieldWithPath("data.popupStoreSnsResponses[].snsId").type(JsonFieldType.NUMBER).description("SNS 아이디"),
                                fieldWithPath("data.popupStoreSnsResponses[].platform").type(JsonFieldType.STRING).description("플랫폼"),
                                fieldWithPath("data.popupStoreSnsResponses[].url").type(JsonFieldType.STRING).description("URL"),
                                fieldWithPath("data.popupStoreSnsResponses[].createdAt").type(JsonFieldType.STRING).description("SNS 생성일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.popupStoreId").type(JsonFieldType.NUMBER).description("팝업스토어 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.userId").type(JsonFieldType.NULL).description("사용자 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.email").type(JsonFieldType.NULL).description("사용자 이메일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.password").type(JsonFieldType.NULL).description("사용자 비밀번호"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.provider").type(JsonFieldType.NULL).description("사용자 제공 업체"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.role").type(JsonFieldType.NULL).description("사용자 역할"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.name").type(JsonFieldType.NULL).description("사용자 이름"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.nickname").type(JsonFieldType.NULL).description("사용자 닉네임"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.user.createdAt").type(JsonFieldType.NULL).description("사용자 생성일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.departmentId").type(JsonFieldType.NUMBER).description("백화점 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.name").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.placeDescription").type(JsonFieldType.STRING).description("백화점 위치"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.latitude").type(JsonFieldType.NUMBER).description("백화점 위도"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.longitude").type(JsonFieldType.NUMBER).description("백화점 경도"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.openTime").type(JsonFieldType.STRING).description("백화점 시작시간"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.closeTime").type(JsonFieldType.STRING).description("백화점 종료시간"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.department.createdAt").type(JsonFieldType.STRING).description("백화점 생성일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.chatRoomId.roomId").type(JsonFieldType.STRING).description("채팅 룸 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.chatRoomId.name").type(JsonFieldType.STRING).description("채팅 룸 이름"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.chatRoomId.createdAt").type(JsonFieldType.STRING).description("채팅 룸 생성일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.category.categoryId").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.category.categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.organizer").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.placeDetail").type(JsonFieldType.STRING).description("상세 위치"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.description").type(JsonFieldType.STRING).description("상세 정보"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.eventDescription").type(JsonFieldType.STRING).description("이벤트"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.bannerImgUrl").type(JsonFieldType.STRING).description("배너 이미지 URL"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.entryFee").type(JsonFieldType.NUMBER).description("입장료"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.maxCapacity").type(JsonFieldType.NUMBER).description("최대 수용 인원"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.reservationEnabled").type(JsonFieldType.BOOLEAN).description("예약 가능 여부"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.salesEnabled").type(JsonFieldType.BOOLEAN).description("판매 가능 여부"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.reservationInterval").type(JsonFieldType.NUMBER).description("예약 간격"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.intervalCapacity").type(JsonFieldType.NUMBER).description("간격당 수용 인원"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.teamSizeLimit").type(JsonFieldType.NUMBER).description("팀 크기 제한"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.openDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.closeDate").type(JsonFieldType.STRING).description("종료일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.openTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.closeTime").type(JsonFieldType.STRING).description("종료 시간"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.views").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStore.total").type(JsonFieldType.NUMBER).description("총합"),
                                fieldWithPath("data.popupStoreImgResponses[].popupStoreImgId").type(JsonFieldType.NUMBER).description("팝업스토어 이미지 아이디"),
                                fieldWithPath("data.popupStoreImgResponses[].imgUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("data.popupStoreImgResponses[].createdAt").type(JsonFieldType.STRING).description("이미지 생성일"),
                                fieldWithPath("data.popupStoresNearBy[].popupStoreId").type(JsonFieldType.NUMBER).description("팝업스토어 아이디"),
                                fieldWithPath("data.popupStoresNearBy[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.popupStoresNearBy[].departmentName").type(JsonFieldType.STRING).description("백화점 이름"),
                                fieldWithPath("data.popupStoresNearBy[].categoryName").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("data.popupStoresNearBy[].openDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("data.popupStoresNearBy[].closeDate").type(JsonFieldType.STRING).description("종료일"),
                                fieldWithPath("data.popupStoresNearBy[].placeDetail").type(JsonFieldType.STRING).description("상세 위치"),
                                fieldWithPath("data.popupStoresNearBy[].bannerImgUrl").type(JsonFieldType.STRING).description("배너 이미지 URL"),
                                fieldWithPath("data.popupStoresNearBy[].organizer").type(JsonFieldType.STRING).description("주최자"),
                                fieldWithPath("data.popupStoresNearBy[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("data.popupStoresNearBy[].total").type(JsonFieldType.NUMBER).description("총합")


                        )
                ));

    }

    @Test
    void 관리자용_팝업스토어_상세정보를_조회한다() throws Exception {
        PopupStoreInfo popupStoreInfo = new PopupStoreInfo(
                popupStore,
                new PopupStoreSns(popupStore, 1L, "instagram", "url", LocalDateTime.now()),
                new PopupStoreImg(popupStore, 1L, "imgurl", LocalDateTime.now()),
                new PopupStoreItem(1L, popupStore, "name", "imgUrl", true, 1000, 100, 1, LocalDateTime.now())
        );

        List<PopupStoreInfo> popupStoreInfoList = List.of(popupStoreInfo);

        given(popupStoreService.getPopupStoreDetailForAdmin(popupStore.getPopupStoreId())).willReturn(popupStoreInfoList);

        ResultActions result = mockMvc.perform(
                get("/api/v1/popup-stores/{popupStoreId}/edit", popupStore.getPopupStoreId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(popmateUser))
        );
        result
                .andExpect(status().isOk())
                .andDo(customDocument());
    }
}
