package org.example.borrowit.utils;

public enum ItemCondition {
    NEW,
    LIKE_NEW,
    VERY_GOOD,
    GOOD,
    FAIR,
    POOR;

    public static ItemCondition fromString(String condition) {
        for (ItemCondition itemCondition : ItemCondition.values()) {
            if (itemCondition.name().equalsIgnoreCase(condition)) {
                return itemCondition;
            }
        }
        throw new IllegalArgumentException("Unknown item condition: " + condition);
    }
}
