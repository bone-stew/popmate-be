package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import com.bonestew.popmate.popupstore.persistence.PopupStoreRepository;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStorePageDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreQueryDto;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreUpdateDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreCreateRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfo;
import com.bonestew.popmate.popupstore.persistence.dto.*;
import com.bonestew.popmate.popupstore.presentation.dto.MyStoreResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreQueryRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreUpdateRequest;
import com.bonestew.popmate.reservation.application.ReservationEventService;
import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.user.persistence.UserDao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.bonestew.popmate.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class PopupStoreService {

    private final FileService awsFileService;
    private final ReservationEventService reservationEventService;
    private final PopupStoreDao popupStoreDao;
    private final UserDao userDao;
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

    public Boolean checkReservationPossibleToday(Long popupStoreId) {
        Integer canReserveToday = popupStoreDao.existsReservationToday(popupStoreId);
        if (canReserveToday > 0){
            return true;
        }
        return false;
    }

    public List<PopupStoreDetailDto> getPopupStoreDetail(Long popupStoreId, Long userId) {
        PopupStoreQueryDto popupStoreQueryDto = new PopupStoreQueryDto(popupStoreId, userId);
        List<PopupStoreDetailDto> popupStoreDetailDtoList = popupStoreDao.findPopupStoreDetailById(popupStoreQueryDto);
        if (popupStoreDetailDtoList.isEmpty()) {
            throw new PopupStoreNotFoundException(popupStoreId);
        }
        if (userId == null) {
            popupStoreDetailDtoList.get(0).setUserReservationStatus(UserReservationStatus.CANCELED);
        } else {
            Boolean userReservationExists = popupStoreDao.userReservationExistsById(popupStoreQueryDto);
            if (userReservationExists) {
                popupStoreDetailDtoList.get(0).setUserReservationStatus(UserReservationStatus.VISITED);
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

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void updateRedisPopupStoreViews() {
        log.info("스케줄러 - 팝업스토어별 조회수 업데이트");
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

    public List<PopupStore> getPopupStoresByQuery(PopupStoreQueryRequest request, Pageable pageable) {
        log.info("pageable: {}", pageable);
        PopupStorePageDto dto = new PopupStorePageDto(pageable, request);
        log.info("sorted: {}", pageable.getSort());
        return popupStoreDao.selectPopupStoresByQuery(dto);
    }

    public List<MyStoreResponse> getPopupStoresByAuth(PopmateUser user) {
        AuthDto authDto = new AuthDto(user.getUserId(), null);
        if (user.getAuthorities().contains(Role.ROLE_MANAGER)) {
            authDto = new AuthDto(user.getUserId(), Role.ROLE_MANAGER);
        } else if (user.getAuthorities().contains(Role.ROLE_STAFF)) {
            authDto = new AuthDto(user.getUserId(), Role.ROLE_STAFF);
        }
        return popupStoreDao.selectPopupStoreByAuth(authDto).stream().map(MyStoreResponse::from).collect(Collectors.toList());
    }

    public void updatePopupStore(PopupStoreUpdateRequest popupStoreUpdateRequest) {
        PopupStore popupStore = popupStoreUpdateRequest.getPopupStore();
        Long popupStoreId = popupStore.getPopupStoreId();
        if (popupStoreUpdateRequest.getStoreImageList() != null) {
            if (!popupStoreUpdateRequest.getStoreImageList().isEmpty()) {
                List<String> storeImgList = popupStoreUpdateRequest.getStoreImageList();
                popupStoreDao.deleteStoreImagesExcludingIds(
                        new PopupStoreImageDeleteRequest(popupStoreUpdateRequest.getPopupStore().getPopupStoreId(),
                                                         popupStoreUpdateRequest.getStoreImagesNotToDelete()));
                for (String url : storeImgList) {
                    PopupStoreImg storeImg = new PopupStoreImg();
                    storeImg.setPopupStore(popupStore);
                    storeImg.setImgUrl(url);
                    popupStoreDao.insertPopupStoreImg(storeImg);
                }
            }
        }
        if (!popupStoreUpdateRequest.getPopupStoreItemList().isEmpty()) {
            List<PopupStoreItem> originalItemsThatNeedUpdating = new ArrayList<>();
            for (PopupStoreItem popupStoreItem : popupStoreUpdateRequest.getPopupStoreItemList()) {
                if (popupStoreItem.getPopupStoreItemId() == null) {
                    popupStoreItem.setPopupStore(popupStore);
                    popupStoreDao.insertPopupStoreItem(popupStoreItem);
                } else {
                    originalItemsThatNeedUpdating.add(popupStoreItem);
                }
            }
            if (!originalItemsThatNeedUpdating.isEmpty()) {
                popupStoreDao.updatePopupStoreItem(originalItemsThatNeedUpdating);
            }
        }
        if (popupStoreUpdateRequest.getPopupStoreItemsToDelete() != null &&
                !popupStoreUpdateRequest.getPopupStoreItemsToDelete().isEmpty()) {
            popupStoreDao.updatePopupStoreItemSalesStatus(popupStoreUpdateRequest.getPopupStoreItemsToDelete());
        }

        if (!popupStoreUpdateRequest.getPopupStoreSnsList().isEmpty()) {
            for (PopupStoreSns popupStoreSns : popupStoreUpdateRequest.getPopupStoreSnsList()) {
                popupStoreSns.setPopupStore(popupStore);

                popupStoreDao.upsertPopupStoreSns(popupStoreSns);
            }
        }
        popupStoreDao.updatePopupStoreInfo(popupStoreUpdateRequest.getPopupStore());

        // 예약 정보가 변경되었는지 확인
        PopupStore updatePopupStore = popupStoreUpdateRequest.getPopupStore();
        popupStoreDao.findById(updatePopupStore.getPopupStoreId())
            .ifPresent(previousPopupStore -> {
                // 예약이 취소되었을 때
                if (!updatePopupStore.getReservationEnabled()) {
                    reservationEventService.deleteReservationFromTomorrow(popupStoreId);
                    log.info("Reservation deleted. popupStoreId: {}", popupStoreId);
                    // 예약이 변경되었을 때
                } else if (previousPopupStore.getReservationEnabled() && updatePopupStore.getReservationEnabled()) {
                    reservationEventService.deleteReservationFromTomorrow(popupStoreId);
                    reservationEventService.createReservation(
                        CreateReservationDto.fromUpdate(
                            popupStoreUpdateRequest.getPopupStore()
                        )
                    );
                    log.info("Reservation updated. popupStoreId: {}", popupStoreId);
                    // 예약이 생성되었을 때
                } else if (!previousPopupStore.getReservationEnabled() && updatePopupStore.getReservationEnabled()) {
                    reservationEventService.createReservation(
                        CreateReservationDto.fromUpdate(
                            popupStoreUpdateRequest.getPopupStore()
                        )
                    );
                    log.info("Reservation created. popupStoreId: {}", popupStoreId);
                }
            });
    }

    public Long postNewPopupStore(List<MultipartFile> storeImageFiles, List<MultipartFile> storeItemImageFiles,
                                  PopupStoreCreateRequest popupStoreCreateRequest
    ) {
        PopupStore popupStore = popupStoreCreateRequest.getPopupStore();
        LocalDate oldOpenDate = popupStore.getOpenDate().toLocalDate();
        LocalDate oldCloseDate = popupStore.getCloseDate().toLocalDate();
        LocalDateTime openTime = popupStore.getOpenTime();
        LocalDateTime closeTime = popupStore.getCloseTime();
        popupStore.setOpenTime(LocalDateTime.of(oldOpenDate, openTime.toLocalTime()));
        popupStore.setCloseTime(LocalDateTime.of(oldCloseDate, closeTime.toLocalTime()));

        List<String> storeImageList = awsFileService.uploadFiles(storeImageFiles, FolderType.STORES);
        List<String> storeItemImageList = new ArrayList<>();
        if (storeItemImageFiles != null) {
            storeItemImageList = awsFileService.uploadFiles(storeItemImageFiles, FolderType.ITEMS);
        }
        popupStoreCreateRequest.getPopupStore().setBannerImgUrl(storeImageList.get(0));
        storeImageList.remove(0);
        popupStoreDao.insertPopupStore(popupStoreCreateRequest.getPopupStore());
        Long storeId = popupStoreCreateRequest.getPopupStore().getPopupStoreId();
        popupStore.setPopupStoreId(storeId);
        if (!storeImageList.isEmpty()) {
            for (String url : storeImageList) {
                PopupStoreImg storeImg = new PopupStoreImg();
                storeImg.setPopupStore(popupStore);
                storeImg.setImgUrl(url);
                popupStoreDao.insertPopupStoreImg(storeImg);
            }
        }
        if (!popupStoreCreateRequest.getPopupStoreItemList().isEmpty()) {
            for (int i = 0; i < popupStoreCreateRequest.getPopupStoreItemList().size(); i++) {
                PopupStoreItem popupStoreItem = popupStoreCreateRequest.getPopupStoreItemList().get(i);
                popupStoreItem.setImgUrl(storeItemImageList.get(i));
                popupStoreItem.setPopupStore(popupStore);
                popupStoreDao.insertPopupStoreItem(popupStoreItem);
            }
        }
        if (!popupStoreCreateRequest.getPopupStoreSnsList().isEmpty()) {
            for (PopupStoreSns popupStoreSns : popupStoreCreateRequest.getPopupStoreSnsList()) {
                popupStoreSns.setPopupStore(popupStore);
                popupStoreDao.insertPopupStoreSns(popupStoreSns);
            }
        }
        if (popupStoreCreateRequest.getPopupStore().getReservationEnabled()) {
            reservationEventService.createReservation(
                CreateReservationDto.from(popupStoreCreateRequest.getPopupStore())
            );
            log.info("Reservation created.");
        }

        log.info("Popup store created. ID: {}", storeId);

        return storeId;
    }

    public List<PopupStoreInfo> getPopupStoreDetailForAdmin(Long popupStoreId) {
        List<PopupStoreInfo> popupStoreInfoList = popupStoreDao.findPopupStoreDetailByIdForAdmin(popupStoreId);
        if (popupStoreInfoList.isEmpty()) {
            throw new PopupStoreNotFoundException(popupStoreId);
        }
        PopupStore popupStore = popupStoreInfoList.get(0).getPopupStore();
        Optional<User> optionalUser = userDao.findById(popupStore.getUser().getUserId());
        User cleanUser = new User();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            cleanUser.setName(user.getName());
            cleanUser.setUserId(user.getUserId());
        }
        popupStoreInfoList.get(0).getPopupStore().setUser(cleanUser);
        return popupStoreInfoList;
    }


}
