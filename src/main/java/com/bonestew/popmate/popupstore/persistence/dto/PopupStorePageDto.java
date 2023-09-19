package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreQueryRequest;
import org.springframework.data.domain.Pageable;

public record PopupStorePageDto(
        Pageable pageable,
        PopupStoreQueryRequest request
) {
}
