package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import com.bonestew.popmate.popupstore.persistence.PopupStoreRepository;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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

    public PopupStoreDetailDto getPopupStoreDetail(Long popupStoreId, Long userId) {
        PopupStoreQueryDto popupStoreQueryDto = new PopupStoreQueryDto(popupStoreId, userId);
        PopupStoreDetailDto popupStoreDetailDto = popupStoreDao.findPopupStoreDetailById(popupStoreQueryDto)
                .orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
        if (userId == null) {
            popupStoreDetailDto.setUserReservationStatus(UserReservationStatus.CANCELED);
        } else {
            Optional<UserReservationStatus> userReservationStatus = popupStoreDao.findUserReservationById(popupStoreQueryDto);
            if (userReservationStatus.isPresent()) {
                popupStoreDetailDto.setUserReservationStatus(userReservationStatus.get());
            } else {
                popupStoreDetailDto.setUserReservationStatus(UserReservationStatus.CANCELED);
            }
        }
        if (userFirstTimeViewingPost(popupStoreId, userId)) {
            popupStoreRepository.createUserViewedKey(popupStoreId, userId);
            popupStoreRepository.incrementPostView(popupStoreId, popupStoreDetailDto.getPopupStore().getViews());
        }
        return popupStoreDetailDto;
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


    public List<PopupStoreSns> getPopupStoreSnss(Long popupStoreId) {
        return popupStoreDao.selectPopupStoreSnss(popupStoreId);
    }

    public List<PopupStoreImg> getPopupStoreImgs(Long popupStoreId) {
        return popupStoreDao.selectPopupStoreImgs(popupStoreId);
    }


    public List<PopupStore> getPopupStoresInDepartment(Long popupStoreId) {
        return popupStoreDao.selectPopupStoresNearBy(popupStoreId);

    }
}
