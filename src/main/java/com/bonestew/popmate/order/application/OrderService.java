package com.bonestew.popmate.order.application;

import com.bonestew.popmate.order.domain.AndroidOrderItem;
import com.bonestew.popmate.order.exception.StockNotFoundException;
import com.bonestew.popmate.order.persistence.OrderDao;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;

    // 전체 상품 정보 가져오는 곳
    public List<PopupStoreItem> getItems(Long popupStoreId) {
        return orderDao.getItems(popupStoreId);
    }


    @Transactional
    public String insertItems(List<AndroidOrderItem> orderItems, Long userId) {
        Long storeId = orderItems.get(0).getPopupStoreId();
        int totalAmount = 0;
        try {
            for (AndroidOrderItem androidOrderItem : orderItems) {
                totalAmount += androidOrderItem.getAmount() * androidOrderItem.getTotalQuantity();
            }
            Long orderId = orderDao.selectSequence();
            // 주문 테이블에 insert하는 곳
            orderDao.insertOrderTable(orderId, userId, storeId, totalAmount);
            //재고 가져와서 스토어 아이템 업데이트
            //주문 아이템 테이블에 insert
            for (AndroidOrderItem androidOrderItem : orderItems) {
                Long itemId = androidOrderItem.getItemId();
                int totalItemAmount = androidOrderItem.getAmount() * androidOrderItem.getTotalQuantity();

                Optional<Integer> opitionalStock = orderDao.findStock(storeId, itemId)
                    .orElseThrow(() -> new StockNotFoundException(storeId, itemId))
                    .describeConstable();
                // 이건 스토어 아이템 테이블의 재고
                int stock = opitionalStock.get();
                // 업데이트할 테이블 재고
                int updateStock = stock - androidOrderItem.getTotalQuantity();
                orderDao.insertOrderItemTable(orderId, itemId, androidOrderItem.getTotalQuantity(), totalItemAmount);
                orderDao.updateStoreItemTable(itemId, storeId, updateStock);
            }
        } catch (StockNotFoundException e) {
            throw e;
        }
        return "주문 성공하였습니다.";
    }
}
