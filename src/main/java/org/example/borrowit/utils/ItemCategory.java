package org.example.borrowit.utils;

public enum ItemCategory {
    ELECTRONICS,
    FURNITURE;

    public static ItemCategory fromString(String string) {
        for (ItemCategory itemCategory : ItemCategory.values()) {
            if (itemCategory.name().equalsIgnoreCase(string)) {
                return itemCategory;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + string);
    }
}
