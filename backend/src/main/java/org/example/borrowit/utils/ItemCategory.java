package org.example.borrowit.utils;

public enum ItemCategory {
    ELECTRONICS,
    BOOKS,
    TOOLS,
    KITCHEN,
    SPORTS,
    OUTDOOR,
    CLOTHING,
    FURNITURE,
    GAMES,
    OTHER;

    public static ItemCategory fromString(String string) {
        for (ItemCategory itemCategory : ItemCategory.values()) {
            if (itemCategory.name().equalsIgnoreCase(string)) {
                return itemCategory;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + string);
    }
}
