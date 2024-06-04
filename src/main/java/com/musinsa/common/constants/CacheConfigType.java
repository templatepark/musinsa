package com.musinsa.common.constants;

public enum CacheConfigType {
    CATEGORY_LOWEST_PRICES(CacheNames.CATEGORY_LOWEST_PRICES, 600, 10),
    BRAND_TOTAL_LOWEST_PRICE(CacheNames.BRAND_TOTAL_LOWEST_PRICE, 600, 10),
    CATEGORY_LOWEST_HIGHEST_PRICES(CacheNames.CATEGORY_LOWEST_HIGHEST_PRICES, 600, 500);

    private final String cacheName;
    private final int ttlSeconds;
    private final int maxSize;

    CacheConfigType(String cacheName, int ttlSeconds, int maxSize) {
        this.cacheName = cacheName;
        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    public String getCacheName() {
        return cacheName;
    }

    public int getTtlSeconds() {
        return ttlSeconds;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
