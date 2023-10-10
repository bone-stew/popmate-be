package com.bonestew.popmate.order.presentation;

import static com.bonestew.popmate.helper.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.order.application.OrderService;
import com.bonestew.popmate.order.domain.AndroidOrderItem;
import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderItem;
import com.bonestew.popmate.order.domain.OrderPlaceDetail;
import com.bonestew.popmate.order.domain.StockCheckItem;
import com.bonestew.popmate.order.presentation.dto.OrderItemRequest;
import com.bonestew.popmate.order.presentation.dto.StockCheckItemsResponse;
import com.bonestew.popmate.order.presentation.dto.StockCheckRequest;
import com.bonestew.popmate.popupstore.domain.Category;
import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.user.domain.Role;
import com.bonestew.popmate.user.domain.SocialProvider;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.utils.WithMockCustomUser;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@SpringBootTest
@WithMockCustomUser
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long popupStoreId;
    private PopupStore popupStore;
    private User user;
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

        user = new User(1L,"frogs0127@naver.com","", SocialProvider.KAKAO, Role.ROLE_USER,"이주시","코딩하는 거북이",dateTime);

    }

    @Test
    void 팝업스토어_상품을_주문한다() throws Exception{
        //given
        Long userId = 1L;
        AndroidOrderItem androidOrderItem = new AndroidOrderItem(1L,"무럭 무럭 잔",1L,"https//img1.url",40000,3,2,1);
        OrderItemRequest orderItemRequest = new OrderItemRequest(List.of(androidOrderItem),"2L","https//img1.url","신용",null,"카드");

        // when
        given(orderService.insertItems(orderItemRequest.getPopupStore(),1L,orderItemRequest.getOrderId(),orderItemRequest.getCardType(),orderItemRequest.getUrl(),orderItemRequest.getEasyPay(),orderItemRequest.getMethod())).willReturn(12345L);

        ResultActions result = mockMvc.perform(
            post("/api/v1/orders/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderItemRequest))
        );

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.orderId").description("주문 ID 출력")
                )
            ));

    }

    @Test
    void 팝업스토어_상품의_재고를_체크한다() throws Exception{
        //given
        List<StockCheckRequest> stockCheckRequest = List.of(new StockCheckRequest(1L,"무럭 무럭 잔",1L,"https//img1.url",40000,3,2,1));
        List<StockCheckItem> stockCheckItemResponse = List.of(new StockCheckItem(1L,true));

        // when
        given(orderService.getCheckItems(stockCheckRequest)).willReturn(stockCheckItemResponse);

        ResultActions result = mockMvc.perform(
            post("/api/v1/orders/stockCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(stockCheckRequest))
        );

        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.stockCheckItemResponse").description("재고 확인 아이템 목록").optional()
                )
            ));

    }

    @Test
    void 팝업스토어_상품을_불러온다() throws Exception{
        //given
        Long popupStoreId = 1L;
        List<PopupStoreItem> popupStoreItem = List.of(
           new PopupStoreItem(1L,popupStore,"무럭무럭 잔","https//img1.url",true,30000,2,1,dateTime),
            new PopupStoreItem(2L,popupStore,"막걸리 잔","https//img2.url",true,60000,4,2,dateTime)
        );
        // when
        given(orderService.getItems(popupStoreId)).willReturn(popupStoreItem);

        ResultActions result = mockMvc.perform(
            get("/api/v1/popup-stores/{popupStoreId}/items",popupStoreId));
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
                    fieldWithPath("data.popupStoreItemResponse").description("팝업스토어 상품 목록"),
                    fieldWithPath("data.popupStoreItemResponse[].itemId").description("상품 ID"),
                    fieldWithPath("data.popupStoreItemResponse[].name").description("상품 이름"),
                    fieldWithPath("data.popupStoreItemResponse[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.popupStoreItemResponse[].imgUrl").description("상품 이미지 URL"),
                    fieldWithPath("data.popupStoreItemResponse[].amount").description("상품 가격"),
                    fieldWithPath("data.popupStoreItemResponse[].stock").description("상품 재고"),
                    fieldWithPath("data.popupStoreItemResponse[].orderLimit").description("주문 제한 수량")
                )
            ));
    }

    @Test
    void 나의_주문내역_불러온다() throws Exception {
        Long userId = 1L;
        PopupStore popupStore1 = new PopupStore(1L,null,null,null,null,"Your Title",
            "Your Organizer",
            "Your Place Detail", "Your Description", "Your Event Description", "Your Banner Image URL", 100, 200, true,
            true,
            15, 5, 10, dateTime, dateTime.plusDays(14), dateTime, dateTime.plusHours(8), 0L, dateTime, 0);

        PopupStoreItem popupStoreItem = new PopupStoreItem(207L,popupStore1,"무럭 무럭 잔","https//img1.url",true,40000,1,1,dateTime);
        List<OrderItem> orderItemList = List.of(
            new OrderItem(1L,null,1,40000,dateTime,207L,popupStoreItem)
        );

        Order order = new Order(21L, user, popupStore, 40000, 0, dateTime, "AfiefeA23FF",
            "https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20231005154215tQIF7&ref=PX",
            "신용", null, "간편결제",
            "https://popmate-bucket.s3.ap-northeast-2.amazonaws.com/popup-stores/81/orders/189/9a18c59e5fc84886ae9952855dcbb74e.png",
            orderItemList);

        List<Order> orders = List.of(
            order
        );

        // when
        given(orderService.getOrders(userId)).willReturn(orders);

        ResultActions result = mockMvc.perform(
            get("/api/v1/orders/me"));

        // then
        result
            .andDo(customDocument(
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.orderListItemResponses[].createdAt").description("주문 생성 일시"),
                    fieldWithPath("data.orderListItemResponses[].orderId").description("주문 ID"),
                    fieldWithPath("data.orderListItemResponses[].userId").description("유저 ID"),
                    fieldWithPath("data.orderListItemResponses[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListItemResponses[].title").description("팝업스토어 제목"),
                    fieldWithPath("data.orderListItemResponses[].placeDetail").description("팝업스토어 장소 상세 정보"),
                    fieldWithPath("data.orderListItemResponses[].bannerImgUrl").description("팝업스토어 배너 이미지 URL"),
                    fieldWithPath("data.orderListItemResponses[].total_amount").description("주문 총 금액"),
                    fieldWithPath("data.orderListItemResponses[].status").description("주문 상태"),
                    fieldWithPath("data.orderListItemResponses[].orderTossId").description("주문 Toss ID"),
                    fieldWithPath("data.orderListItemResponses[].url").description("주문 URL"),
                    fieldWithPath("data.orderListItemResponses[].cardType").description("카드 종류"),
                    fieldWithPath("data.orderListItemResponses[].easyPay").description("간편 결제 여부"),
                    fieldWithPath("data.orderListItemResponses[].method").description("결제 방법"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].orderItemId").description("주문 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].order").description("주문"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].totalQuantity").description("주문 아이템 총 수량"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].totalAmount").description("주문 아이템 총 합계"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].createdAt").description("생성 날짜"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].storeItemId").description("상품 아이템 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem").description("팝업스토어 정보"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStoreItemId").description("팝업스토어 상품 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.user").description("사용자"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.department").description("부서"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.chatRoomId").description("채팅 룸 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.category").description("카테고리"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.title").description("제목"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.organizer").description("주최자"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.placeDetail").description("장소 상세 정보"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.description").description("설명"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.eventDescription").description("이벤트 설명"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.bannerImgUrl").description("배너 이미지 URL"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.entryFee").description("참가비"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.maxCapacity").description("최대 수용 인원"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.reservationEnabled").description("예약 가능 여부"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.salesEnabled").description("판매 가능 여부"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.reservationInterval").description("예약 간격"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.intervalCapacity").description("간격 별 수용 인원"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.teamSizeLimit").description("팀 크기 제한"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.openDate").description("오픈 날짜"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.closeDate").description("마감 날짜"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.openTime").description("오픈 시간"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.closeTime").description("마감 시간"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.views").description("조회수"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.createdAt").description("생성일"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.popupStore.total").description("총 합계"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.name").description("팝업스토어 상품 이름"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.imgUrl").description("팝업스토어 상품 이미지 URL"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.isOnSale").description("상품 판매 여부"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.amount").description("팝업스토어 상품 가격"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.stock").description("팝업스토어 상품 재고"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.orderLimit").description("팝업스토어 상품 주문 제한 수량"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[].popupStoreItem.createdAt").description("팝업스토어 상품 생성 일시")

                )
            ));
    }

    @Test
    void 팝업스토어_위치를_불러온다() throws Exception{
        //given
        Long popupStoreId = 1L;
        OrderPlaceDetail orderPlaceDetail = new OrderPlaceDetail("빵빵이 팝업스토어","더현대 서울 4층","서울특별시","https:img1.url");
        // when
        given(orderService.getPlaceDetails(popupStoreId)).willReturn(orderPlaceDetail);

        ResultActions result = mockMvc.perform(
            get("/api/v1/orders/placedetails/{popupStoreId}",popupStoreId));
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
                    fieldWithPath("data.title").description("팝업스토어 제목"),
                    fieldWithPath("data.placeDetail").description("팝업스토어 상세 정보"),
                    fieldWithPath("data.placeDescription").description("팝업스토어 설명"),
                    fieldWithPath("data.bannerImgUrl").description("배너 이미지 Url")
                )
            ));
    }

    @Test
    void 백오피스_팝업스토어_주문목록을_불러온다() throws Exception{
        //given
        Long popupStoreId = 1L;
        PopupStoreItem popupStoreItem = new PopupStoreItem(207L,null,"무럭 무럭 잔","https//img1.url",true,40000,1,1,dateTime);
        List<OrderItem> orderItemList = List.of(
            new OrderItem(1L,null,1,40000,dateTime,207L,popupStoreItem)
        );
        List<Order> orders = List.of(
            new Order(21L, user, popupStore, 40000, 0, dateTime, "AfiefeA23FF",
            "https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20231005154215tQIF7&ref=PX",
            "신용", null, "간편결제",
            "https://popmate-bucket.s3.ap-northeast-2.amazonaws.com/popup-stores/81/orders/189/9a18c59e5fc84886ae9952855dcbb74e.png",
            orderItemList)
        );
        // when
        given(orderService.getBackOfficeOrderLists(popupStoreId)).willReturn(orders);

        ResultActions result = mockMvc.perform(
            get("/api/v1/orders/backoffice/orderList/{popupStoreId}",popupStoreId));
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
                    fieldWithPath("data.orderListItemResponses[].createdAt").description("주문 생성일시"),
                    fieldWithPath("data.orderListItemResponses[].orderId").description("주문 ID"),
                    fieldWithPath("data.orderListItemResponses[].userId").description("사용자 ID"),
                    fieldWithPath("data.orderListItemResponses[].name").description("사용자 이름"),
                    fieldWithPath("data.orderListItemResponses[].popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListItemResponses[].title").description("팝업스토어 제목"),
                    fieldWithPath("data.orderListItemResponses[].placeDetail").description("팝업스토어 장소 세부 정보"),
                    fieldWithPath("data.orderListItemResponses[].bannerImgUrl").description("팝업스토어 배너 이미지 URL"),
                    fieldWithPath("data.orderListItemResponses[].total_amount").description("주문 총액"),
                    fieldWithPath("data.orderListItemResponses[].status").description("주문 상태"),
                    fieldWithPath("data.orderListItemResponses[].orderTossId").description("주문 Toss ID"),
                    fieldWithPath("data.orderListItemResponses[].url").description("주문 URL"),
                    fieldWithPath("data.orderListItemResponses[].cardType").description("카드 종류"),
                    fieldWithPath("data.orderListItemResponses[].easyPay").description("간편 결제 여부"),
                    fieldWithPath("data.orderListItemResponses[].method").description("결제 방법"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].orderItemId").description("주문 상품 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].order").description("주문"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].totalQuantity").description("주문 상품 수량"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].totalAmount").description("주문 상품 총액"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].createdAt").description("주문 상품 생성일시"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].storeItemId").description("주문 상품 상점 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.popupStoreItemId").description("상품 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.popupStore").description("상품 이름"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.name").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.imgUrl").description("상품 이미지 URL"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.isOnSale").description("상품 판매 여부"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.amount").description("상품 가격"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.stock").description("상품 수량"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.orderLimit").description("주문 제한 수량"),
                    fieldWithPath("data.orderListItemResponses[].orderItemList[0].popupStoreItem.createdAt").description("아이템 생성 일시")
                )

            ));
    }

    @Test
    void 팝업스토어_주문_상세정보를_불러온다() throws Exception{
        //given
        Long orderId = 1L;
        PopupStoreItem popupStoreItem = new PopupStoreItem(207L,null,"무럭 무럭 잔","https//img1.url",true,40000,1,1,dateTime);
        List<OrderItem> orderItemList = List.of(
            new OrderItem(1L,null,1,40000,dateTime,207L,popupStoreItem)
        );
        Order order = new Order(21L, user, popupStore, 40000, 0, dateTime, "AfiefeA23FF",
                "https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20231005154215tQIF7&ref=PX",
                "신용", null, "간편결제",
                "https://popmate-bucket.s3.ap-northeast-2.amazonaws.com/popup-stores/81/orders/189/9a18c59e5fc84886ae9952855dcbb74e.png",
                orderItemList);
        // when
        given(orderService.getOrderDetails(orderId)).willReturn(order);

        ResultActions result = mockMvc.perform(
            get("/api/v1/orders/details/{orderId}",orderId));
        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("orderId").description("주문 id")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.orderListDetailResponse.createdAt").description("주문 생성일시"),
                    fieldWithPath("data.orderListDetailResponse.orderId").description("주문 ID"),
                    fieldWithPath("data.orderListDetailResponse.userId").description("사용자 ID"),
                    fieldWithPath("data.orderListDetailResponse.popupStoreId").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListDetailResponse.title").description("팝업스토어 제목"),
                    fieldWithPath("data.orderListDetailResponse.placeDetail").description("팝업스토어 장소 세부 정보"),
                    fieldWithPath("data.orderListDetailResponse.bannerImgUrl").description("팝업스토어 배너 이미지 URL"),
                    fieldWithPath("data.orderListDetailResponse.total_amount").description("주문 총액"),
                    fieldWithPath("data.orderListDetailResponse.status").description("주문 상태"),
                    fieldWithPath("data.orderListDetailResponse.orderTossId").description("주문 Toss ID"),
                    fieldWithPath("data.orderListDetailResponse.url").description("주문 URL"),
                    fieldWithPath("data.orderListDetailResponse.cardType").description("카드 종류"),
                    fieldWithPath("data.orderListDetailResponse.easyPay").description("간편 결제 여부"),
                    fieldWithPath("data.orderListDetailResponse.method").description("결제 방법"),
                    fieldWithPath("data.orderListDetailResponse.qrImgUrl").description("QR 이미지"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].orderItemId").description("주문 상품 ID"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].order").description("주문"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].totalQuantity").description("주문 상품 수량"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].totalAmount").description("주문 상품 총액"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].createdAt").description("주문 상품 생성일시"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].storeItemId").description("주문 상품 상점 ID"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.popupStoreItemId").description("상품 ID"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.popupStore").description("상품 이름"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.name").description("팝업스토어 ID"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.imgUrl").description("상품 이미지 URL"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.isOnSale").description("상품 판매 여부"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.amount").description("상품 가격"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.stock").description("상품 수량"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.orderLimit").description("주문 제한 수량"),
                    fieldWithPath("data.orderListDetailResponse.orderItemList[0].popupStoreItem.createdAt").description("아이템 생성 일시")
                )

            ));
    }

    @Test
    void 백오피스_팝업스토어_픽업() throws Exception{
        //given
        Long orderId = 1L;
        Long userId = 1L;
        Long popupStoreId = 1L;
        String message = "픽업완료 되었습니다.";
        // when
        given(orderService.getChangeStatus(orderId,userId,popupStoreId)).willReturn(message);

        ResultActions result = mockMvc.perform(
            get("/api/v1/orders/qrcode/{orderId}/{userId}/{popupStoreId}",orderId,userId,popupStoreId));
        // then
        result
            .andExpect(status().isOk())
            .andDo(customDocument(
                pathParameters(
                    parameterWithName("orderId").description("주문 id"),
                    parameterWithName("userId").description("유저 id"),
                    parameterWithName("popupStoreId").description("팝업스토어 id")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 코드"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data").description("상품 픽업 성공여부")
                )

            ));
    }


}
