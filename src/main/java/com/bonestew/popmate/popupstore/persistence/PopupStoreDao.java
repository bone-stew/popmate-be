package com.bonestew.popmate.popupstore.persistence;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.persistence.dto.*;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreQueryRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

@Mapper
public interface PopupStoreDao {

    Optional<PopupStore> findById(@Param("popupStoreId") Long popupStoreId);

    List<PopupStore> selectPopupStores(@RequestBody PopupStoreSearchRequest popupStoreSearchRequest);

    List<Banner> selectBanners();

    List<PopupStore> selectPopupStoresVisitedBy(Long userId);

    List<PopupStore> selectPopupStoresToRecommend();

    List<PopupStore> selectPopupStoresEndingSoon();

    List<PopupStoreDetailDto> findPopupStoreDetailById(PopupStoreQueryDto popupStoreQueryDto);

    Optional<UserReservationStatus> findUserReservationById(PopupStoreQueryDto popupStoreQueryDto);

    int batchUpdatePopupStoreViews(List<PopupStoreUpdateDto> updates);

    List<PopupStore> selectPopupStoresNearBy(Long popupStoreId);

    List<PopupStore> selectPopupStoresByQuery(PopupStorePageDto dto);

    List<PopupStore> selectPopupStoreByAuth(AuthDto dto);
}
