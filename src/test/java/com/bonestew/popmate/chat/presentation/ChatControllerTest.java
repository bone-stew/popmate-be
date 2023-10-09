package com.bonestew.popmate.chat.presentation;


import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.chat.application.ChatService;
import com.bonestew.popmate.chat.domain.ChatMessage;
import com.bonestew.popmate.chat.domain.ChatReport;
import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.chat.presentation.dto.BanUserRequest;
import com.bonestew.popmate.chat.presentation.dto.EnterResponse;
import com.bonestew.popmate.chat.presentation.dto.MessagesResponse;
import com.bonestew.popmate.utils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@WithMockCustomUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    private final String BASE_URL = "/api/v1/chat/";


    @Test
    @DisplayName("채팅방 입장")
    public void enterChatRoom() throws Exception {

        //given
        String roomId = "1";
        ChatRoom chatRoom = new ChatRoom();
        given(chatService.enterChatRoom(roomId)).willReturn(chatRoom);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "enter/{roomId}", roomId));

        //then
        result.andExpect(status().isOk())
                .andDo(customDocument(
                                pathParameters(
                                        parameterWithName("roomId").description("입장할 채팅방 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.userId").description("유저 ID"),
                                        fieldWithPath("data.nickname").description("유저 닉네임"),
                                        fieldWithPath("data.authorities").description("유저 권한"),
                                        fieldWithPath("data.enabled").description("유저 상태"),
                                        fieldWithPath("data.password").description("유저 비밀번호"),
                                        fieldWithPath("data.username").description("유저 식별자"),
                                        fieldWithPath("data.accountNonLocked").description("유저 정지 유무"),
                                        fieldWithPath("data.credentialsNonExpired").description("유저 권한 만료"),
                                        fieldWithPath("data.accountNonExpired").description("유저 계정 만료")
                                )
                        )
                );

    }

    @Test
    @DisplayName("채팅방 조회")
    public void room() throws Exception {

        //given
        String roomId = "1";
        ChatRoom chatRoom = new ChatRoom();
        given(chatService.findRoomById(roomId)).willReturn(chatRoom);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "room/{roomId}", roomId));

        //then
        result.andExpect(status().isOk())
                .andDo(
                        customDocument(
                                pathParameters(
                                        parameterWithName("roomId").description("입장할 채팅방 Id")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.roomId").description("채팅방 ID"),
                                        fieldWithPath("data.name").description("채팅방 이름"),
                                        fieldWithPath("data.createdAt").description("생성일")
                                )
                        )
                );
    }

    @Test
    @DisplayName("채팅방 메세지 목록")
    public void messages() throws Exception {

        //given
        Long roomId = 1L;
        List<ChatMessage> list = new ArrayList<>();
        MessagesResponse messagesResponse = new MessagesResponse(list);
        given(chatService.loadChatMessagesByRoomId(roomId, Pageable.unpaged())).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "room/messages/{roomId}?page=0&size=0&sort=createdAt,desc", roomId));

        //then
        result.andExpect(status().isOk()).andDo(
                customDocument(
                        pathParameters(
                                parameterWithName("roomId").description("입장할 채팅방 Id")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.messages").description("채팅 메시지 목록")
                        )

                )
        );
    }

    @Test
    @DisplayName("채팅방 썸네일 조회")
    public void thumbnail() throws Exception {

        //given
        Long roomId = 1L;
        List<ChatMessage> list = new ArrayList<>();
        given(chatService.loadChatThumbnail(roomId)).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "thumbnail/{roomId}", roomId));

        //then
        result.andExpect(status().isOk()).andDo(
                customDocument(
                        pathParameters(
                                parameterWithName("roomId").description("입장할 채팅방 Id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.messages").description("채팅 메시지 목록")
                        )

                )
        );
    }

    @Test
    @DisplayName("채팅 신고 내역 조회")
    public void reportList() throws Exception {

        //given
        List<ChatReport> list = new ArrayList<>();
        given(chatService.getReports()).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "report"));

        //then
        result.andExpect(status().isOk()).andDo(
                customDocument(
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("채팅 신고 내역")
                        )

                )
        );
    }

    @Test
    @DisplayName("채팅방 입장 자격 조회")
    public void enterVerify() throws Exception {

        //given
        EnterResponse response = new EnterResponse(false, null);
        given(chatService.isUserBanned(new PopmateUser(1L, "", new ArrayList<>()))).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(get(BASE_URL + "enter-verify"));

        //then
        result.andExpect(status().isOk()).andDo(
                customDocument(
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("채팅방 입장 가능 여부 및 해지일")
                        )
                )
        );
    }

    @Test
    @DisplayName("유저 정지 처리")
    public void banUser() throws Exception {

        //given
        BanUserRequest request = new BanUserRequest(1L, 1);
        List<ChatReport> list = new ArrayList<>();
        given(chatService.banUserAndGetNewReportList(request)).willReturn(list);

        //when
        ResultActions result = mockMvc.perform(post(BASE_URL + "ban-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk()).andDo(
                customDocument(
                        requestFields(
                                fieldWithPath("userId").description("유저 Id"),
                                fieldWithPath("type").description("처분 결과 (0: 반려, 1: 3일, 2: 7일, 3: 1달, 4: 영구)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("채팅 신고 목록")
                        )
                )
        );
    }

}
