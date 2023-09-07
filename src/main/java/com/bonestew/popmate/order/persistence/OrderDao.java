package com.bonestew.popmate.order.persistence;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {
    List<PopupStoreItem> getItems(@Param("popupStoreId") Long popupStoreId);

}
