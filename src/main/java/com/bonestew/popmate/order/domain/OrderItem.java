package com.bonestew.popmate.order.domain;

import com.bonestew.popmate.date.BaseTime;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.time.LocalDateTime;
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
public class OrderItem {

    private Long orderItemId;
    private Order order;
    private int totalQuantity;
    private int totalAmount;
    private LocalDateTime createdAt;
    // 추가한 것
    private Long storeItemId;
    private PopupStoreItem popupStoreItem
}
