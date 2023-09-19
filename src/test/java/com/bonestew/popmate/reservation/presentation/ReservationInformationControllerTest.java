package com.bonestew.popmate.reservation.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private Long popupStoreId;
    private PopupStore popupStore;
    private LocalDateTime dateTime;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        popupStoreId = 1L;
        dateTime = LocalDateTime.of(2023, 10, 1, 10, 0);
        date = LocalDate.of(2023, 10, 1);
        popupStore = new PopupStore(1L, new User(), new Department(), new ChatRoom(), "Your Title", "Your Organizer",
            "Your Place Detail", "Your Description", "Your Event Description", "Your Banner Image URL", 100, 200, true,
            15, 5, 10, dateTime, dateTime.plusDays(14), dateTime, dateTime.plusHours(8), 0L, dateTime);
    }

    @Test
    void 진행_중인_예약을_조회한다() throws Exception {
        // given
        Reservation reservation = new Reservation(1L, popupStore, 10, 5, 5, ReservationStatus.IN_PROGRESS,
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
    void 일일_예약_목록_조회한다() throws Exception {
        // given
        List<Reservation> reservations = List.of(
            new Reservation(1L, popupStore, 10, 5, 5, ReservationStatus.IN_PROGRESS,
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
    void 나의_예약_정보을_조회한다() throws Exception {
        // given
        Long reservationId = 1L;
        Long userId = 1L;
        Reservation reservation = new Reservation(1L, popupStore, 10, 5, 5, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15), dateTime.plusMinutes(30), dateTime.plusMinutes(45), dateTime);
        UserReservation userReservation = new UserReservation(1L, new User(), reservation, 2, "qrImgUrl",
            UserReservationStatus.RESERVED, dateTime);

        // when
        given(reservationInformationService.getMyReservation(reservationId, userId)).willReturn(userReservation);

        ResultActions result = mockMvc.perform(
            get("/api/v1/members/me/reservations/{reservationId}", reservationId));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("조회할 예약 id")
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
}
