package com.bonestew.popmate.order.domain;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.auth.domain.User;
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
public class Order {

    private Long orderId;
    private User user;
    private PopupStore popupStore;
    private int totalAmount;
    private int status;
    private LocalDateTime createdAt;
}
