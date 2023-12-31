package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.AndroidOrderItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private List<AndroidOrderItem> popupStore;
    // 이건 토스페이먼츠 주문번호
    private String orderId;
    private String url;
    private String cardType;
    private String easyPay;
    private String method;
}
