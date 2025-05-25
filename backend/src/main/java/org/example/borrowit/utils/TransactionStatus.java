package org.example.borrowit.utils;

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED,
    IN_PROGRESS,
    DECLINED,
    APPROVED;

    public static TransactionStatus fromString(String status) {
        try {
            return TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
    }
}
