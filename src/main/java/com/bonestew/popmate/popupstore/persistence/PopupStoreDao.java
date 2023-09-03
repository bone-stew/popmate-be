package com.bonestew.popmate.popupstore.persistence;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

@Mapper
public interface PopupStoreDao {

    Optional<PopupStore> findById(@Param("popupStoreId") Long popupStoreId);

    List<PopupStore> selectPopupStores(@RequestBody PopupStoreSearchRequest popupStoreSearchRequest);

    List<Banner> selectBanners();

    List<PopupStore> selectPopupStoresVisitedBy(Long userId);

    List<PopupStore> selectPopupStoresToRecommend();

    List<PopupStore> selectPopupStoresEndingInOneWeek();

    Optional<PopupStoreDetailDto> findPopupStoreDetailById(PopupStoreQueryDto popupStoreQueryDto);

    List<PopupStoreSns> selectPopupStoreSnss(Long popupStoreId);

    List<PopupStoreImg> selectPopupStoreImgs(Long popupStoreId);

    List<PopupStoreItem> selectPopupStoreItems(Long popupStoreId);

    Optional<UserReservationStatus> findUserReservationById(PopupStoreQueryDto popupStoreQueryDto);

    boolean updatePopupStoreViews(PopupStoreUpdateDto popupStoreUpdateDto);

    int batchUpdatePopupStoreViews(List<PopupStoreUpdateDto> updates);
}
