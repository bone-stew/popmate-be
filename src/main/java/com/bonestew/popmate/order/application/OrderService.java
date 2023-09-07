package com.bonestew.popmate.order.application;

import com.bonestew.popmate.order.persistence.OrderDao;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;

    // 전체 상품 정보 가져오는 곳
    public List<PopupStoreItem> getItems(Long popupStoreId) {
        return orderDao.getItems(popupStoreId);
    }
}
