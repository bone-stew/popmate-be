package com.bonestew.popmate.order.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.order.application.OrderService;
import com.bonestew.popmate.order.domain.AndroidOrderItem;
import com.bonestew.popmate.order.presentation.dto.OrderItemRequest;
import com.bonestew.popmate.order.presentation.dto.OrderResponse;
import com.bonestew.popmate.order.presentation.dto.PopupStoreItemsResponse;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import javax.swing.text.StyledEditorKit.BoldAction;
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
        String insertCheck = orderService.insertItems(orderItems, USER_ID);
        return ApiResponse.success(
            OrderResponse.from(insertCheck)
        );
    }

}
