package com.bonestew.popmate.order.domain;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.user.domain.User;
import java.time.LocalDateTime;
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
public class Order {

    private Long orderId;
    private User user;
    private PopupStore popupStore;
    private int totalAmount;
    private int status;
    private LocalDateTime createdAt;

    // 이건 토스페이먼츠 주문번호
    private String orderTossId;
    private String url;
    private String cardType;
    private String easyPay;
    private String method;
    private String qrImgUrl;

    private List<OrderItem> orderItemList;
    public void setPopupStore(String title, String placeDetail, String bannerImgUrl) {
        if (this.popupStore == null) {
            this.popupStore = new PopupStore();
        }
        this.popupStore.setTitle(title);
        this.popupStore.setPlaceDetail(placeDetail);
        this.popupStore.setBannerImgUrl(bannerImgUrl);
    }
}
