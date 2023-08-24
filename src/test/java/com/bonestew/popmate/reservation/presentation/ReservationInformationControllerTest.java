package com.bonestew.popmate.reservation.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.reservation.application.ReservationInformationService;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.ReservationStatus;
import com.bonestew.popmate.user.domain.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReservationInformationControllerTest {

    @MockBean
    private ReservationInformationService reservationInformationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 진행_중인_예약을_조회한다() throws Exception {
        // given
        Long popupStoreId = 1L;
        LocalDateTime dateTime = LocalDateTime.of(2023, 8, 1, 10, 0);
        Reservation reservation = new Reservation(1L, new PopupStore(), 10, 5, 5, ReservationStatus.IN_PROGRESS,
            dateTime, dateTime.plusMinutes(15));

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
                    fieldWithPath("code").description(ResultCode.SUCCESS.name()),
                    fieldWithPath("message").description(ResultCode.SUCCESS.getMessage()),
                    fieldWithPath("data.reservationId").description("예약 id"),
                    fieldWithPath("data.startTime").description(dateTime),
                    fieldWithPath("data.endTime").description(dateTime.plusMinutes(15)),
                    fieldWithPath("data.status").description(ReservationStatus.IN_PROGRESS.getDescription()),
                    fieldWithPath("data.popupStoreTitle").description("팝업스토어 제목"),
                    fieldWithPath("data.popupStoreDescription").description("팝업스토어 설명"),
                    fieldWithPath("data.popupStoreOpenTime").description(dateTime),
                    fieldWithPath("data.popupStoreCloseTime").description(dateTime)
                )
            ));
    }
}
