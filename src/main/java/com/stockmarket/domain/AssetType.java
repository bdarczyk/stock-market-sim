package com.stockmarket.domain;

public enum AssetType {
    SHARE(0),
    COMMODITY(1),
    CURRENCY(2);

    private final int sortOrder;

    AssetType(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
