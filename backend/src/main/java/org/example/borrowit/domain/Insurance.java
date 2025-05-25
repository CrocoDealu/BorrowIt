package org.example.borrowit.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "Insurances")
public class Insurance extends Entity<Integer> {
    @Column(name = "covered_amount", nullable = false)
    private double coveredAmount;

    @ManyToMany
    @JoinTable(
            name = "insured_items", // Join table name
            joinColumns = @JoinColumn(name = "insurance_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();


    public Insurance() {
    }

    public Insurance(Integer integer, List<Item> items, double coveredAmount) {
        super(integer);
        this.items = items;
        this.coveredAmount = coveredAmount;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getCoveredAmount() {
        return coveredAmount;
    }

    public void setCoveredAmount(double coveredAmount) {
        this.coveredAmount = coveredAmount;
    }
}
