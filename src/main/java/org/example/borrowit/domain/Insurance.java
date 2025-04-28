package org.example.borrowit.domain;


import javax.persistence.*;


@javax.persistence.Entity
@Table(name = "Insurances")
public class Insurance extends Entity<Integer> {
    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(name = "covered_amount", nullable = false)
    private double coveredAmount;

    public Insurance() {
    }

    public Insurance(Integer integer, Rental rental, double coveredAmount) {
        super(integer);
        this.rental = rental;
        this.coveredAmount = coveredAmount;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public double getCoveredAmount() {
        return coveredAmount;
    }

    public void setCoveredAmount(double coveredAmount) {
        this.coveredAmount = coveredAmount;
    }
}
