package org.example.borrowit.utils;

public enum RentalStatus {
    RETURNED,
    OVERDUE,
    RENTED,
    CANCELLED,
    MARKED_AS_RETURNED;

    public static RentalStatus fromString(String status) {
        try {
            return RentalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid rental status: " + status);
        }
    }
}
