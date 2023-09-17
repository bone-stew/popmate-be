package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import com.bonestew.popmate.popupstore.persistence.PopupStoreRepository;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreCreateDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreCreateRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfo;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopupStoreService {

    private final PopupStoreDao popupStoreDao;

    private final PopupStoreRepository popupStoreRepository;

    public PopupStore getPopupStore(Long popupStoreId) {
        return popupStoreDao.findById(popupStoreId).orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
    }

    public List<PopupStore> getPopupStores(
            Boolean isOpeningSoon,
            String startDateText,
            String endDateText,
            String keyword,
            Integer offSetRows,
            Integer rowsToGet
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        if (startDateText != null) {
            startDate = LocalDate.parse(startDateText, formatter);
        }
        if (endDateText != null) {
            endDate = LocalDate.parse(endDateText, formatter);
        }
        isOpeningSoon = Objects.requireNonNullElse(isOpeningSoon, false);
        keyword = Objects.requireNonNullElse(keyword, "");
        offSetRows = Objects.requireNonNullElse(offSetRows, 0);
        rowsToGet = Objects.requireNonNullElse(rowsToGet, 0);

        PopupStoreSearchRequest popupStoreSearchRequest = new PopupStoreSearchRequest(
                isOpeningSoon,
                startDate,
                endDate,
                keyword,
                offSetRows,
                rowsToGet
        );
        return popupStoreDao.selectPopupStores(popupStoreSearchRequest);
    }

    public List<Banner> getBanners() {
        return popupStoreDao.selectBanners();
    }

    public List<PopupStore> getPopupStoresVisitedBy(Long userId) {
        if (userId == null) {
            return new ArrayList<PopupStore>();
        }
        return popupStoreDao.selectPopupStoresVisitedBy(userId);
    }

    public List<PopupStore> getPopupStoresRecommend() {
        return popupStoreDao.selectPopupStoresToRecommend();
    }

    public List<PopupStore> getPopupStoresEndingSoon() {
        return popupStoreDao.selectPopupStoresEndingSoon();
    }

    public List<PopupStoreDetailDto> getPopupStoreDetail(Long popupStoreId, Long userId) {
        PopupStoreQueryDto popupStoreQueryDto = new PopupStoreQueryDto(popupStoreId, userId);
        List<PopupStoreDetailDto> popupStoreDetailDtoList = popupStoreDao.findPopupStoreDetailById(popupStoreQueryDto);
        if(popupStoreDetailDtoList.isEmpty()) {
            throw new PopupStoreNotFoundException(popupStoreId);
        }
        if (userId == null) {
            popupStoreDetailDtoList.get(0).setUserReservationStatus(UserReservationStatus.CANCELED);
        } else {
            Optional<UserReservationStatus> userReservationStatus = popupStoreDao.findUserReservationById(popupStoreQueryDto);
            if (userReservationStatus.isPresent()) {
                popupStoreDetailDtoList.get(0).setUserReservationStatus(userReservationStatus.get());
            } else {
                popupStoreDetailDtoList.get(0).setUserReservationStatus(UserReservationStatus.CANCELED);
            }
        }
        if (userFirstTimeViewingPost(popupStoreId, userId)) {
            popupStoreRepository.createUserViewedKey(popupStoreId, userId);
            popupStoreRepository.incrementPostView(popupStoreId, popupStoreDetailDtoList.get(0).getPopupStore().getViews());
        }
        return popupStoreDetailDtoList;
    }

    private boolean userFirstTimeViewingPost(Long popupStoreId, Long userId) {
        Boolean keyExists = popupStoreRepository.hasKey(popupStoreId, userId);
        if (keyExists && keyExists != null) {
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    public void updateRedisPopupStoreViews() {
        Set<String> redisKeys = popupStoreRepository.getKeys("POST:*");
        List<PopupStoreUpdateDto> updates = new ArrayList<>();
        if (redisKeys != null) {
            for (String key : redisKeys) {
                String[] parts = key.split(":");
                Long popupStoreId = Long.parseLong(parts[1]);
                Long views = popupStoreRepository.getViews(key);
                PopupStoreUpdateDto updateDto = new PopupStoreUpdateDto(popupStoreId, views);
                updates.add(updateDto);
                popupStoreRepository.removeKey(key);
            }
        }
        if (!updates.isEmpty()) {
            popupStoreDao.batchUpdatePopupStoreViews(updates);
        }
    }


    public List<PopupStore> getPopupStoresInDepartment(Long popupStoreId) {
        return popupStoreDao.selectPopupStoresNearBy(popupStoreId);
    }

    public Long postNewPopupStore(PopupStoreCreateRequest popupStoreCreateRequest, List<String> storeImageList,
            List<String> storeItemImageList) {
        popupStoreCreateRequest.getPopupStore().setBannerImgUrl(storeImageList.get(0));
        storeImageList.remove(0);
//        PopupStoreCreateDto popupStoreCreateDto = new PopupStoreCreateDto();
//        popupStoreCreateDto.setPopupStore(popupStoreCreateRequest.getPopupStore());
        popupStoreDao.insertPopupStore(popupStoreCreateRequest.getPopupStore());
        Long storeId = popupStoreCreateRequest.getPopupStore().getPopupStoreId();
        PopupStore popupStore = new PopupStore();
        popupStore.setPopupStoreId(storeId);
        List<PopupStoreImg> popupStoreImgs = new ArrayList<>();
        for(String url : storeImageList) {
            PopupStoreImg storeImg = new PopupStoreImg();
            storeImg.setPopupStore(popupStore);
            storeImg.setImgUrl(url);
            popupStoreImgs.add(storeImg);
        }

        log.info("storeImg {}", popupStoreImgs);
        for (int i = 0; i < popupStoreCreateRequest.getPopupStoreItemList().size(); i++) {
            PopupStoreItem popupStoreItem = popupStoreCreateRequest.getPopupStoreItemList().get(i);
            popupStoreItem.setImgUrl(storeItemImageList.get(i));
            popupStoreItem.setPopupStore(popupStore);
        }
        for (PopupStoreSns popupStoreSns : popupStoreCreateRequest.getPopupStoreSnsList()){
            popupStoreSns.setPopupStore(popupStore);
        }
        popupStoreDao.insertPopupStoreImgs(popupStoreImgs);
        popupStoreDao.insertPopupStoreItems(popupStoreCreateRequest.getPopupStoreItemList());
        popupStoreDao.insertPopupStoreSns(popupStoreCreateRequest.getPopupStoreSnsList());
        return storeId;
    }

    public List<PopupStoreInfo> getPopupStoreDetailForAdmin(Long popupStoreId) {
        List<PopupStoreInfo> popupStoreInfoList = popupStoreDao.findPopupStoreDetailByIdForAdmin(popupStoreId);
        if(popupStoreInfoList.isEmpty()){
           throw new PopupStoreNotFoundException(popupStoreId);
        }
        return popupStoreDao.findPopupStoreDetailByIdForAdmin(popupStoreId);
    }

    public PopupStore updatePopupStore(PopupStoreInfo popupStoreInfo) {
        return popupStoreDao.updsatePopupStoreInfo(popupStoreInfo);
    }

}
