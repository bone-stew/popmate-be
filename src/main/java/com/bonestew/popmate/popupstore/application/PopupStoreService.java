package com.bonestew.popmate.popupstore.application;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.exception.PopupStoreNotFoundException;
import com.bonestew.popmate.popupstore.persistence.PopupStoreDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopupStoreService {

    private final PopupStoreDao popupStoreDao;

    public PopupStore getPopupStore(Long popupStoreId) {
        return popupStoreDao.findById(popupStoreId)
            .orElseThrow(() -> new PopupStoreNotFoundException(popupStoreId));
    }
}
