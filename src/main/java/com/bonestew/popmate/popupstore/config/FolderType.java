package com.bonestew.popmate.popupstore.config;

public enum FolderType {
    ITEMS("item_imgs"),
    STORES("store_imgs"),
    BANNERS("banner_imgs"),
    RESERVATIONS("popup-stores/%s/reservations/%s"),
    ORDERS("popup-stores/%s/orders/%s");
    private final String folderName;

    FolderType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
