package org.example.borrowit.dto;

import org.example.borrowit.domain.Entity;
import org.example.borrowit.domain.Rental;
import org.example.borrowit.utils.HashStringUtility;
import org.example.borrowit.utils.RentalStatus;

import java.time.LocalDateTime;

public class RentalDto extends Entity<Integer> {
    private String userHash;
    private int itemId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private RentalStatus status;

    public RentalDto(int id, String userHash, int itemId, LocalDateTime startDate, LocalDateTime endDate, RentalStatus status) {
        super(id);
        this.userHash = userHash;
        this.itemId = itemId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public RentalDto(Rental rental) {
        super(rental.getId());
        this.userHash = HashStringUtility.hashOwnerIdentifier(rental.getUser().getEmail());
        this.itemId = rental.getItem().getId();
        this.startDate = rental.getStartDate();
        this.endDate = rental.getEndDate();
        this.status = rental.getStatus();
    }
    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
