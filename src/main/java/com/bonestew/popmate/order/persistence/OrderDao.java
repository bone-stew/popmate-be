package com.bonestew.popmate.order.persistence;

import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderItem;
import com.bonestew.popmate.order.domain.OrderPlaceDetail;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    List<PopupStoreItem> getItems(@Param("popupStoreId") Long popupStoreId);

    void insertOrderTable(@Param("orderId") Long orderId,
                          @Param("userId") Long userId,
                          @Param("storeId") Long storeId,
                          @Param("totalAmount") int totalAmount,
                          @Param("orderTossId") String orderTossId,
                          @Param("cardType") String orderCardType,
                          @Param("url") String url,
                          @Param("easyPay") String easyPay,
                          @Param("method") String method);

    Optional<Integer> findStock(@Param("storeId") Long storeId,
                                @Param("itemId") Long itemId);

    void insertOrderItemTable(@Param("orderId") Long orderId,
                              @Param("itemId") Long itemId,
                              @Param("totalQuantity") int totalQuantity,
                              @Param("totalItemAmount") int totalItemAmount);

    Long selectSequence();

    void updateStoreItemTable(@Param("itemId") Long itemId,
                              @Param("storeId") Long storeId,
                              @Param("updateStock") int updateStock);

    List<Order> getOrders(@Param("userId") Long userId);

    Order getRequestOrders(@Param("popupStoreId") Long popupStoreId);

    List<OrderItem> getOrderItems(@Param("orderId") Long orderId);

    PopupStoreItem getItemInfo(@Param("storeItemId") Long storeItemId,
                               @Param("popupStoreId") Long popupStoreId);

    OrderPlaceDetail getPlaceDetails(@Param("popupStoreId") Long popupStoreId);

}
