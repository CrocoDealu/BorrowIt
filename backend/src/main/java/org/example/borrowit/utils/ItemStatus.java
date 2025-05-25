package org.example.borrowit.utils;

public enum ItemStatus {
    AVAILABLE,
    RENTED,
    UNAVAILABLE;

    public static ItemStatus fromString(String status) {
        for (ItemStatus itemStatus : ItemStatus.values()) {
            if (itemStatus.name().equalsIgnoreCase(status)) {
                return itemStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
