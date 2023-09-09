package com.bonestew.popmate.order.persistence;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    List<PopupStoreItem> getItems(@Param("popupStoreId") Long popupStoreId);

    void insertOrderTable(@Param("orderId") Long orderId,
                          @Param("userId") Long userId,
                          @Param("storeId") Long storeId,
                          @Param("totalAmount") int totalAmount);


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
}
