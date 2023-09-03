package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.exception.PopupStoreUpdateFailedException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.ArrayList;
import java.util.List;
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

    private final RedisTemplate<String, Object> redisTemplate;

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
        PopupStoreQueryDto popupStoreQueryDto = new PopupStoreQueryDto(popupStoreId, userId);
        PopupStoreDetailDto popupStoreDetailDto = popupStoreDao.findPopupStoreDetailById(popupStoreQueryDto)
                .orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
        Optional<UserReservationStatus> userReservationStatus = popupStoreDao.findUserReservationById(popupStoreQueryDto);
        if(userReservationStatus.isPresent()){
            popupStoreDetailDto.setUserReservationStatus(userReservationStatus.get());
        }else {
            popupStoreDetailDto.setUserReservationStatus(UserReservationStatus.CANCELED);
        }
        if (userFirstTimeViewingPost(popupStoreId, userId)) {
            String userViewKey = "USER-"+ userId + "POST-" + popupStoreId ;
            redisTemplate.opsForValue().set(userViewKey, true);
            redisTemplate.expire(userViewKey, 1, TimeUnit.DAYS);
            incrementPostView(popupStoreId, popupStoreDetailDto.getPopupStore().getViews());
        }
        return popupStoreDetailDto;
    }

    private boolean userFirstTimeViewingPost(Long popupStoreId, Long userId) {
        String userKey = "USER-" + userId + "POST-" + popupStoreId ;
        Boolean keyExists = redisTemplate.hasKey(userKey);
        if (keyExists && keyExists != null) {
            return false;
        }
        return true;
    }

    private void incrementPostView(Long popupStoreId, Long views) {
        String postKey = "POST-" + popupStoreId;
        Boolean keyExists = redisTemplate.hasKey(postKey);
        if (keyExists && keyExists != null) {
            redisTemplate.opsForValue().increment(postKey);
        } else {
            redisTemplate.opsForValue().set(postKey, views+1L);
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    public void updateRedisPopupStoreViews(){
        Set<String> redisKeys = redisTemplate.keys("POST-*");
        List<PopupStoreUpdateDto> updates = new ArrayList<>();
        if (redisKeys != null) {
            for (String key : redisKeys) {
                String[] parts = key.split("-");
                Long popupStoreId = Long.parseLong(parts[1]);
                Long views = Long.parseLong((String) redisTemplate.opsForValue().get(key));
                PopupStoreUpdateDto updateDto = new PopupStoreUpdateDto(popupStoreId, views);
                updates.add(updateDto);

                redisTemplate.delete(key);
            }
        }
        if (!updates.isEmpty()) {
            int rows = popupStoreDao.batchUpdatePopupStoreViews(updates);
            if (rows != updates.size()){
                throw new PopupStoreUpdateFailedException();
            }
        }
        Set<String> userFirstTimeViewingKeys = redisTemplate.keys("USER-*POST-*");
        if (userFirstTimeViewingKeys != null) {
            for (String key : userFirstTimeViewingKeys) {
                redisTemplate.delete(key);
            }
        }
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
