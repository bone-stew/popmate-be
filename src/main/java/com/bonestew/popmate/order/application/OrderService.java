package com.bonestew.popmate.order.application;

import static com.bonestew.popmate.popupstore.config.FolderType.ORDERS;
import com.bonestew.popmate.order.domain.AndroidOrderItem;
import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderItem;
import com.bonestew.popmate.order.domain.OrderPlaceDetail;
import com.bonestew.popmate.order.domain.StockCheckItem;
import com.bonestew.popmate.order.exception.StockNotFoundException;
import com.bonestew.popmate.order.persistence.OrderDao;
import com.bonestew.popmate.order.presentation.dto.StockCheckRequest;
import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.reservation.application.QrService;
import java.io.InputStream;
import java.util.ArrayList;
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
    private final QrService qrService;
    private final FileService fileService;

    // 전체 상품 정보 가져오는 곳
    public List<PopupStoreItem> getItems(Long popupStoreId) {
        return orderDao.getItems(popupStoreId);
    }


    @Transactional
    public Long insertItems(List<AndroidOrderItem> orderItems, Long userId, String orderTossId, String cardType, String url, String easyPay,
                              String method) {
        Long storeId = orderItems.get(0).getPopupStoreId();
        int totalAmount = 0;
        try {
            for (AndroidOrderItem androidOrderItem : orderItems) {
                totalAmount += androidOrderItem.getAmount() * androidOrderItem.getTotalQuantity();
            }
            Long orderId = orderDao.selectSequence();
            // 주문 테이블에 insert하는 곳
            String orderQrCode = gnerateOrderQrCode(userId,orderId);
            orderDao.insertOrderTable(orderId, userId, storeId, totalAmount, orderTossId, cardType, url,easyPay, method, orderQrCode);
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
            return orderId;
        } catch (StockNotFoundException e) {
            throw e;
        }
    }

    private String gnerateOrderQrCode(Long userId, Long orderId) {
        InputStream inputStream = qrService.generateOrderQRCode(userId,orderId);
        String directory = String.format(ORDERS.getFolderName(),
            userId, orderId);

        return fileService.uploadInputStream(inputStream, directory);
    }

    public List<Order> getOrders(Long userId) {
        // 처음에 유저가 주문한 적이 있는지 알아야 하기 때문에 가져오는 로직
        List<Order> orderList = orderDao.getOrders(userId);
        if(orderList == null) return null;

        // 여기는 백화점 상세정보 가져와서 세팅해주는 곳
        for(Order order : orderList){
            Order requestOrder = orderDao.getRequestOrders(order.getPopupStore().getPopupStoreId());
            order.setPopupStore(requestOrder.getPopupStore().getTitle(), requestOrder.getPopupStore().getPlaceDetail(), requestOrder.getPopupStore().getBannerImgUrl());
            List<OrderItem> orderItems = orderDao.getOrderItems(order.getOrderId());
            for(OrderItem orderItem : orderItems){
                PopupStoreItem popupStoreItem =  orderDao.getItemInfo(orderItem.getStoreItemId(), order.getPopupStore().getPopupStoreId());
                orderItem.setPopupStoreItem(popupStoreItem);
            }
            order.setOrderItemList(orderItems);
        }

        return orderList;
    }


    public List<StockCheckItem> getCheckItems(List<StockCheckRequest> orderItems) {
        List<StockCheckItem> stockCheckItems = new ArrayList<>();
        for(StockCheckRequest stockCheckRequest : orderItems){
            StockCheckItem stockCheckItem = new StockCheckItem();
            boolean check = true;
            //itemId를 넘겨서 재고 확인해주고 맞으면
            PopupStoreItem popupStoreItem = orderDao.getItemInfo(stockCheckRequest.getItemId(), stockCheckRequest.getPopupStoreId());

            if(popupStoreItem.getStock() < stockCheckRequest.getTotalQuantity()){
                stockCheckItem.setCheck(false);
                stockCheckItem.setItemId(popupStoreItem.getPopupStoreItemId());
            }else{
                stockCheckItem.setCheck(true);
                stockCheckItem.setItemId(popupStoreItem.getPopupStoreItemId());
            }
            assert false;
            stockCheckItems.add(stockCheckItem);
        }
        return stockCheckItems;
    }

    public OrderPlaceDetail getPlaceDetails(Long popupStoreId) {
        return orderDao.getPlaceDetails(popupStoreId);
    }

    public List<Order> getBackOfficeOrderLists(Long popupStoreId) {
        List<Order> orderList = orderDao.getBackOfficeOrders(popupStoreId);
        for(Order order : orderList){
              List<OrderItem> orderItems = orderDao.getBackOfficeOrderItems(order.getOrderId());
              order.setOrderItemList(orderItems);
        }
        return orderList;
    }

    public List<Order> getTodayOrders(Long popupStoreId, String sort) {
        return orderDao.getTodayOrders(popupStoreId, sort);
    }


    public Order getOrderDetails(Long orderId) {
        Order order = orderDao.getOrderDetails(orderId);
        List<OrderItem> orderItems = orderDao.getOrderItems(order.getOrderId());
        for(OrderItem orderItem : orderItems){
            PopupStoreItem popupStoreItem =  orderDao.getItemInfo(orderItem.getStoreItemId(), order.getPopupStore().getPopupStoreId());
            orderItem.setPopupStoreItem(popupStoreItem);
        }
        order.setOrderItemList(orderItems);
        return order;
    }

    public String getChangeStatus(Long orderId, Long userId) {
        orderDao.getChangeStatus(orderId,userId);
        return "픽업완료되었습니다.";
    }
}
