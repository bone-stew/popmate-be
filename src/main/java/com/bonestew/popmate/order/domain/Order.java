package com.bonestew.popmate.order.domain;

import com.bonestew.popmate.date.BaseTime;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.auth.domain.User;
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
public class Order extends BaseTime {

    private Long orderId;
    private User user;
    private PopupStore popupStore;
    private int totalAmount;
    private int status;
    // 이건 토스페이먼츠 주문번호
    private String orderTossId;
    private String url;
    private String cardType;
    private String easyPay;
    private String method;
}
