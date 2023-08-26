package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopupStoreService {

    private final PopupStoreDao popupStoreDao;

    public PopupStore getPopupStore(Long popupStoreId) {
        return popupStoreDao.findById(popupStoreId).orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
    }

    public List<PopupStore> getPopupStores(PopupStoreSearchRequest popupStoreSearchRequest) {
        return popupStoreDao.selectPopupStores(popupStoreSearchRequest);
    }

    public List<Banner> getBanners() {
        return popupStoreDao.selectBanners();
    }

    public List<PopupStore> getPopupStoresVisitedBy(long userId) {
        return popupStoreDao.selectPopupStoresVisitedBy(userId);
    }

    public List<PopupStore> getPopupStoresRecommend() {
        return popupStoreDao.selectPopupStoresToRecommend();
    }

    public List<PopupStore> getPopupStoresEndingSoon() {
        return popupStoreDao.selectPopupStoresEndingInOneWeek();
    }

    public PopupStoreDetailDto getPopupStoreDetail(Long popupStoreId, Long userId) {
        return popupStoreDao.findPopupStoreDetailById(new PopupStoreQueryDto(popupStoreId, userId))
                .orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
    }

    public List<PopupStoreSns> getPopupStoreSnss(Long popupStoreId) {
        return popupStoreDao.selectPopupStoreSnss(popupStoreId);
    }

    public List<PopupStoreImg> getPopupStoreImgs(Long popupStoreId) {
        return popupStoreDao.selectPopupStoreImgs(popupStoreId);
    }

    public List<PopupStoreItem> getPopupStoreGoods(Long popupStoreId) {
        return popupStoreDao.selectPopupStoreItems(popupStoreId);
    }
}
