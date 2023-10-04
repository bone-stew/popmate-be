package com.bonestew.popmate.popupstore.presentation.dto;

import org.springframework.data.domain.Pageable;

public record PopupStoreQueryRequest(
        int type,
        String query
) {
}
