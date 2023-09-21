package com.bonestew.popmate.popupstore.persistence;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStorePageDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfo;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreQueryRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreUpdateRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.List;
import java.util.Optional;
import javax.swing.Popup;
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

    List<PopupStoreInfo> findPopupStoreDetailByIdForAdmin(Long popupStoreId);

    void updatePopupStoreInfo(PopupStore popupStore);

    void insertPopupStore(PopupStore popupStore);
//    PopupStore insertPopupStore(PopupStoreCreateDto popupStoreCreateDto);

    void insertPopupStoreImg(PopupStoreImg popupStoreImg);

    void insertPopupStoreItem(PopupStoreItem popupStoreItemList);

    void insertPopupStoreSns(PopupStoreSns popupStoreSnsList);

    void deleteStoreImageById(Long popupStoreId);

    void deleteStoreItemsById(Long popupStoreId);

    void deleteStoreSnsById(Long popupStoreId);

}
