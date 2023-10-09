package com.bonestew.popmate.chart.presentation;


import com.bonestew.popmate.chart.application.ChartService;
import com.bonestew.popmate.chart.presentation.dto.ChartResponse;
import com.bonestew.popmate.utils.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@WithMockCustomUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ChartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChartService chartService;

    @Test
    @DisplayName("차트 데이터 조회")
    public void chart() throws Exception {

        //given
        ChartResponse response = new ChartResponse(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        given(chartService.getStatistics()).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/chart"));

        //then
        result.andExpect(status().isOk()).andDo(customDocument(responseFields(fieldWithPath("code").description("응답 코드"), fieldWithPath("message").description("응답 메시지"), fieldWithPath("data.popupStoreRanks").description("팝업스토어 순위"), fieldWithPath("data.reservationCounts").description("일별 예약 횟수"), fieldWithPath("data.storeRevenues").description("스토어별 매출"))));
    }
}
