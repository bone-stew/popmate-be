package com.bonestew.popmate.reservation.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.popupstore.domain.Category;
import com.bonestew.popmate.reservation.application.dto.GuestLimitUpdateRequest;
import com.bonestew.popmate.user.domain.Role;
import com.bonestew.popmate.user.domain.SocialProvider;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.utils.WithMockCustomUser;
import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.reservation.application.ReservationInformationService;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.ReservationStatus;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@WithMockCustomUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReservationInformationControllerTest {

    @MockBean
    private ReservationInformationService reservationInformationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long popupStoreId;
    private PopupStore popupStore;
    private LocalDateTime dateTime;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        popupStoreId = 1L;
        dateTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        date = LocalDate.of(2023, 10, 1);
        popupStore = new PopupStore(1L, new User(), new Department(), new ChatRoom(), new Category(), "Your Title",
            "Your Organizer",
            "Your Place Detail", "Your Description", "Your Event Description", "Your Banner Image URL", 100, 200, true,
            true,
            15, 5, 10, dateTime, dateTime.plusDays(14), dateTime, dateTime.plusHours(8), 0L, dateTime, 0);
    }

    @Test
    void 진행_중인_예약을_조회한다() throws Exception {
        // given
        Reservation reservation = new Reservation(1L, popupStore, 10, 5, 50, 30, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime);

        // when
        given(reservationInformationService.getActiveReservation(popupStoreId)).willReturn(reservation);

        ResultActions result = mockMvc.perform(
            get("/api/v1/popup-stores/{popupStoreId}/current-reservations", popupStoreId));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("popupStoreId").description("조회할 팝업스토어 id")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.reservationId").description("예약 id"),
                    fieldWithPath("data.startTime").description("예약 시작 시간"),
                    fieldWithPath("data.endTime").description("예약 종료 시간"),
                    fieldWithPath("data.status").description("예약 상태"),
                    fieldWithPath("data.popupStoreTitle").description("팝업스토어 제목"),
                    fieldWithPath("data.popupStoreDescription").description("팝업스토어 설명"),
                    fieldWithPath("data.popupStoreOpenTime").description("팝업스토어 오픈 시간"),
                    fieldWithPath("data.popupStoreCloseTime").description("팝업스토어 종료 시간")
                )
            ));
    }

    @Test
    void 나의_예약_목록을_조회한다() throws Exception {
        // given
        Long userId = 1L;
        Reservation reservation = new Reservation(1L, popupStore, 10, 5, 50, 30, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime);
        UserReservation userReservation = new UserReservation(1L, new User(), reservation, 2, "qrImgUrl",
            UserReservationStatus.RESERVED, dateTime);
        UserReservation userReservation2 = new UserReservation(1L, new User(), reservation, 2, "qrImgUrl",
            UserReservationStatus.VISITED, dateTime);
        UserReservation userReservation3 = new UserReservation(1L, new User(), reservation, 2, "qrImgUrl",
            UserReservationStatus.CANCELED, dateTime);

        // when
        given(reservationInformationService.getMyReservations(userId)).willReturn(List.of(userReservation, userReservation2, userReservation3));

        ResultActions result = mockMvc.perform(
            get("/api/v1/members/me/reservations"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.before[].userReservationId").description("예약자 정보 ID"),
                    fieldWithPath("data.before[].reservationId").description("예약 ID"),
                    fieldWithPath("data.before[].reservationStatus").description("예약 상태"),
                    fieldWithPath("data.before[].startTime").description("예약 시작 시간"),
                    fieldWithPath("data.before[].endTime").description("예약 종료 시간"),
                    fieldWithPath("data.before[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.before[].popupStoreTitle").description("팝업스토어 제목"),
                    fieldWithPath("data.before[].bannerImgUrl").description("팝업스토어 배너 이미지 URL"),
                    fieldWithPath("data.after[].userReservationId").description("예약자 정보 ID"),
                    fieldWithPath("data.after[].reservationId").description("예약 ID"),
                    fieldWithPath("data.after[].reservationStatus").description("예약 상태"),
                    fieldWithPath("data.after[].startTime").description("예약 시작 시간"),
                    fieldWithPath("data.after[].endTime").description("예약 종료 시간"),
                    fieldWithPath("data.after[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.after[].popupStoreTitle").description("팝업스토어 제목"),
                    fieldWithPath("data.after[].bannerImgUrl").description("팝업스토어 배너 이미지 URL"),
                    fieldWithPath("data.canceled[].userReservationId").description("예약자 정보 ID"),
                    fieldWithPath("data.canceled[].reservationId").description("예약 ID"),
                    fieldWithPath("data.canceled[].reservationStatus").description("예약 상태"),
                    fieldWithPath("data.canceled[].startTime").description("예약 시작 시간"),
                    fieldWithPath("data.canceled[].endTime").description("예약 종료 시간"),
                    fieldWithPath("data.canceled[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.canceled[].popupStoreTitle").description("팝업스토어 제목"),
                    fieldWithPath("data.canceled[].bannerImgUrl").description("팝업스토어 배너 이미지 URL")
                )
            ));
    }

    @Test
    void 나의_예약_상세를_조회한다() throws Exception {
        // given
        Long userReservationId = 1L;
        Reservation reservation = new Reservation(1L, popupStore, 10, 5, 50, 30, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime);
        UserReservation userReservation = new UserReservation(1L, new User(), reservation, 2, "qrImgUrl",
            UserReservationStatus.RESERVED, dateTime);

        // when
        given(reservationInformationService.getMyReservation(userReservationId)).willReturn(userReservation);

        ResultActions result = mockMvc.perform(
            get("/api/v1/members/me/user-reservations/{userReservationId}", userReservationId));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("userReservationId").description("조회할 사용자 예약 id")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.popupStoreTitle").description("팝업 스토어 제목"),
                    fieldWithPath("data.popupStoreImageUrl").description("팝업 스토어 이미지 URL"),
                    fieldWithPath("data.popupStorePlaceDetail").description("팝업 스토어 장소 상세 정보"),
                    fieldWithPath("data.reservationQrImageUrl").description("예약 QR 코드 이미지 URL"),
                    fieldWithPath("data.guestCount").description("예약 인원 수"),
                    fieldWithPath("data.visitStartTime").description("입장 시작 시간"),
                    fieldWithPath("data.visitEndTime").description("입장 종료 시간"),
                    fieldWithPath("data.reservationStatus").description("예약 상태 (예: PENDING)")
                )
            ));
    }

    @Test
    void 일일_예약_목록_조회한다() throws Exception {
        // given
        List<Reservation> reservations = List.of(
            new Reservation(1L, popupStore, 10, 5, 50, 30, ReservationStatus.IN_PROGRESS,
                dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime)
        );

        // when
        given(reservationInformationService.getDailyReservations(popupStoreId, date)).willReturn(reservations);

        ResultActions result = mockMvc.perform(
            get("/api/v1/popup-stores/{popupStoreId}/reservations", popupStoreId)
                .param("date", "2023-10-01"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("popupStoreId").description("조회할 팝업스토어 id")
                ),
                queryParameters(
                    parameterWithName("date").description("조회할 날짜 (예: 2023-10-01)")
                ),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data[].reservationId").type(JsonFieldType.NUMBER).description("예약 id"),
                    fieldWithPath("data[].startTime").type(JsonFieldType.STRING).description("입장 시작 시간"),
                    fieldWithPath("data[].endTime").type(JsonFieldType.STRING).description("입장 종료 시간"),
                    fieldWithPath("data[].visitStartTime").type(JsonFieldType.STRING).description(" 시작 시간"),
                    fieldWithPath("data[].visitEndTime").type(JsonFieldType.STRING).description("예약 종료 시간"),
                    fieldWithPath("data[].guestLimit").type(JsonFieldType.NUMBER).description("예약 최대 인원 수"),
                    fieldWithPath("data[].currentGuestCount").type(JsonFieldType.NUMBER).description("현재 예약 인원 수"),
                    fieldWithPath("data[].status").type(JsonFieldType.STRING).description("예약 상태(예: IN_PROGRESS)")
                )
            ));

    }

    @Test
    void 오늘의_예약_목록을_조회한다() throws Exception {
        // given
        Reservation activeReservation = new Reservation(1L, popupStore, 10, 5, 50, 30, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime);
        Reservation reservation1 = new Reservation(2L, popupStore, 8, 3, 40, 25, ReservationStatus.CLOSED,
            dateTime.plusHours(1), dateTime.plusHours(1).plusMinutes(15), dateTime.plusHours(1).plusMinutes(30),
            dateTime.plusHours(1).plusMinutes(45), dateTime);
        Reservation reservation2 = new Reservation(3L, popupStore, 12, 6, 60, 35, ReservationStatus.IN_PROGRESS,
            dateTime.plusHours(2), dateTime.plusHours(2).plusMinutes(15), dateTime.plusHours(2).plusMinutes(30),
            dateTime.plusHours(2).plusMinutes(45), dateTime);
        List<Reservation> reservations = List.of(reservation1, reservation2);

        given(reservationInformationService.getCurrentlyEnteredReservation(anyLong())).willReturn(activeReservation);
        given(reservationInformationService.getTodayReservations(anyLong())).willReturn(reservations);

        // when
        ResultActions result = mockMvc.perform(
            get("/api/v1/popup-stores/{popupStoreId}/reservations/today", popupStoreId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("popupStoreId").description("팝업 스토어 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("message").description("응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.popupStoreId").description("팝업 스토어 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data.popupStoreName").description("팝업 스토어 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.currentReservationStartTime").description("현재 예약 시작 시간")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.currentReservationEndTime").description("현재 예약 종료 시간")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.reservedGuestCount").description("예약된 손님 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data.entryGuestCount").description("입장한 손님 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data.isEntering").description("입장 진행 여부")
                        .type(JsonFieldType.BOOLEAN),
                    fieldWithPath("data.upComingReservations").description("다가오는 예약 목록")
                        .type(JsonFieldType.ARRAY),
                    fieldWithPath("data.upComingReservations[].reservationId").description("예약 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data.upComingReservations[].visitStartTime").description("예약 시작 시간")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.upComingReservations[].visitEndTime").description("예약 종료 시간")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data.upComingReservations[].currentGuestCount").description("현재 손님 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data.upComingReservations[].status").description("예약 상태")
                        .type(JsonFieldType.STRING)
                )
            ));
    }

    @Test
    void 예약_인원_수를_변경한다() throws Exception {
        // given
        Long reservationId = 1L;
        int guestLimit = 10;
        GuestLimitUpdateRequest guestLimitUpdateRequest = new GuestLimitUpdateRequest(guestLimit);

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/reservations/{reservationId}/guest-limit", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guestLimitUpdateRequest)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                requestFields(
                    fieldWithPath("guestLimit").description("예약 인원 수")
                        .type(JsonFieldType.NUMBER)
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("message").description("응답 메시지")
                        .type(null),
                    fieldWithPath("data").description("응답 데이터")
                        .type(null)
                )
            ));
    }

    @Test
    void 예약을_중단한다() throws Exception {
        // given
        Long reservationId = 1L;

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/reservations/{reservationId}/cancellation", reservationId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("message").description("응답 메시지")
                        .type(null),
                    fieldWithPath("data").description("응답 데이터")
                        .type(null)
                )
            ));
    }

    @Test
    void 예약을_재개한다() throws Exception {
        // given
        Long reservationId = 1L;

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/reservations/{reservationId}/resume", reservationId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("message").description("응답 메시지")
                        .type(null),
                    fieldWithPath("data").description("응답 데이터")
                        .type(null)
                )
            ));

    }

    @Test
    void 예약자_입장_정보를_조회한다() throws Exception {
        // given
        Long reservationId = 1L;
        User user = new User(1L, "popmate@example.com", "1234", SocialProvider.GOOGLE, Role.ROLE_USER, "서명현",
            "testNickname", LocalDateTime.now());
        UserReservation userReservation = new UserReservation(1L, user, new Reservation(), 2, "qrImgUrl",
            UserReservationStatus.RESERVED, dateTime);

        given(reservationInformationService.getEntranceInfo(anyLong())).willReturn(List.of(userReservation));

        // when
        ResultActions result = mockMvc.perform(
            get("/api/v1/reservations/{reservationId}/entrance-info", reservationId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("message").description("응답 메시지")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data[].userReservationId").description("예약자 정보 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data[].userId").description("예약자 ID")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data[].userName").description("예약자 이름")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data[].email").description("예약자 이메일")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data[].guestCount").description("예약 인원 수")
                        .type(JsonFieldType.NUMBER),
                    fieldWithPath("data[].status").description("예약 상태")
                        .type(JsonFieldType.STRING),
                    fieldWithPath("data[].reservationTime").description("예약 시간")
                        .type(JsonFieldType.STRING)
                )
            ));
    }
}
