package com.bonestew.popmate.popupstore.domain;

public enum CategoryType {

    POPUP_STORE("팝업스토어"),
    MALL("매장");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
