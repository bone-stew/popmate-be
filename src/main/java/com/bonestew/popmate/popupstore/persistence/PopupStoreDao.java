package com.bonestew.popmate.popupstore.persistence;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PopupStoreDao {

    Optional<PopupStore> findById(@Param("popupStoreId") Long popupStoreId);
}
