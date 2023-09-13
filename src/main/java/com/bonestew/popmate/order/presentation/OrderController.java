package com.bonestew.popmate.order.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.order.application.OrderService;
import com.bonestew.popmate.order.domain.AndroidOrderItem;
import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderPlaceDetail;
import com.bonestew.popmate.order.domain.StockCheckItem;
import com.bonestew.popmate.order.presentation.dto.OrderItemRequest;
import com.bonestew.popmate.order.presentation.dto.OrderListItemsResponse;
import com.bonestew.popmate.order.presentation.dto.OrderPlaceDetailResponse;
import com.bonestew.popmate.order.presentation.dto.OrderResponse;
import com.bonestew.popmate.order.presentation.dto.PopupStoreItemsResponse;
import com.bonestew.popmate.order.presentation.dto.StockCheckItemsResponse;
import com.bonestew.popmate.order.presentation.dto.StockCheckRequest;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private static final Long USER_ID = 1L;

    // 굿즈 상품들 불러오는 곳
    @GetMapping("/popup-stores/{popupStoreId}/items")
    public ApiResponse<PopupStoreItemsResponse> getPopupStoreItems(@PathVariable("popupStoreId") Long popupStoreId){
        List<PopupStoreItem> popupStoreItem = orderService.getItems(popupStoreId);

        return ApiResponse.success(
            PopupStoreItemsResponse.from(popupStoreItem)
        );
    }

    @PostMapping("/orders/new")
    public ApiResponse<OrderResponse> orderPopupStoreItems(@RequestBody OrderItemRequest popupStore, @AuthenticationPrincipal PopmateUser popmateUser){
        List<AndroidOrderItem> orderItems = popupStore.getPopupStore();
        String insertCheck = orderService.insertItems(orderItems, popmateUser.getUserId(), popupStore.getOrderId(), popupStore.getCardType(),popupStore.getUrl(),popupStore.getEasyPay(),popupStore.getMethod());
        return ApiResponse.success(
            OrderResponse.from(insertCheck)
        );
    }

    @GetMapping("/orders/me")
    public ApiResponse<OrderListItemsResponse> orderLists(@AuthenticationPrincipal PopmateUser popmateUser){
        List<Order> orders = orderService.getOrders(popmateUser.getUserId());

        return ApiResponse.success(
            OrderListItemsResponse.from(orders)
        );
    }

    @PostMapping("/orders/stockCheck")
    public ApiResponse<StockCheckItemsResponse> checkItemStock(@RequestBody List<StockCheckRequest> stockCheckRequest){
        List<StockCheckItem> checkItems = orderService.getCheckItems(stockCheckRequest);

        return ApiResponse.success(
            StockCheckItemsResponse.from(checkItems)
        );
    }

    @GetMapping("/orders/placedetails/{popupStoreId}")
    public ApiResponse<OrderPlaceDetailResponse> getPopupStorePlaceDetails(@PathVariable("popupStoreId") Long popupStoreId){
        OrderPlaceDetail orderPlaceDetail = orderService.getPlaceDetails(popupStoreId);
       return ApiResponse.success(
           OrderPlaceDetailResponse.from(orderPlaceDetail)
       );
    }

}