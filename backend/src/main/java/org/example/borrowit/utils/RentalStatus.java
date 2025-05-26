package org.example.borrowit.utils;

public enum RentalStatus {
    APPROVED,
    REJECTED,
    RETURNED,
    OVERDUE,
    PENDING,
    CANCELLED;

    public static RentalStatus fromString(String status) {
        try {
            return RentalStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid rental status: " + status);
        }
    }
}
