package com.bonestew.popmate.reservation.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.reservation.application.ReservationWifiService;
import com.bonestew.popmate.reservation.application.dto.WifiInfoRequest;
import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import com.bonestew.popmate.utils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
class ReservationWifiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationWifiService reservationInformationService;

    @Test
    void wifi_목록을_통해_예약_가능한_위치인지_확인한다() throws Exception {
        // given
        Long reservationId = 1L;
        WifiRequest request = new WifiRequest(
                List.of(
                        new WifiInfoRequest("ssid1", "bssid1"),
                        new WifiInfoRequest("ssid2", "bssid2"),
                        new WifiInfoRequest("ssid3", "bssid3")
                )
        );

        given(reservationInformationService.check(reservationId, request)).willReturn(true);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/reservations/{reservationId}/wifi-check", reservationId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result
                .andExpect(status().isOk())
                .andDo(customDocument(
                        requestFields(
                                fieldWithPath("wifiList[].ssid").type(JsonFieldType.STRING).description("와이파이 ssid"),
                                fieldWithPath("wifiList[].bssid").type(JsonFieldType.STRING).description("와이파이 bssid")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.isCheck").type(JsonFieldType.BOOLEAN).description("예약 가능 여부")
                        )
                ));
    }
}
