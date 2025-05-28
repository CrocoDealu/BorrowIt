package org.example.borrowit.dto;

import org.example.borrowit.domain.Rental;

public class RentalDatesDto {
    private String startDate;
    private String endDate;

    public RentalDatesDto(Rental rental) {
        this.startDate = rental.getStartDate().toString(); // Assumes Rental has LocalDateTime fields
        this.endDate = rental.getEndDate().toString();
    }

    // Getters and setters
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
