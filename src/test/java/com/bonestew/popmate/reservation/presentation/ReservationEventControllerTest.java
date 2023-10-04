package com.bonestew.popmate.reservation.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.reservation.application.ReservationEventService;
import com.bonestew.popmate.reservation.application.dto.ProcessEntranceRequest;
import com.bonestew.popmate.utils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ReservationEventControllerTest {

    @MockBean
    private ReservationEventService reservationEventService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 예약자를_입장_처리한다() throws Exception {
        // given
        Long reservationId = 1L;
        Long reservationUserId = 1L;
        ProcessEntranceRequest processEntranceRequest = new ProcessEntranceRequest(reservationUserId);

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/reservations/{reservationId}/entrance", reservationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(processEntranceRequest)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("reservationId").description("예약 ID")
                ),
                requestFields(
                    fieldWithPath("reservationUserId").description("예약자 ID")
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
    void 예약을_취소한다() throws Exception {
        // given
        Long reservationId = 1L;

        // when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/reservations/{reservationId}/cancel", reservationId)
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
}
