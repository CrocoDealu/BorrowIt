package org.example.borrowit.domain;

import org.example.borrowit.utils.ItemCategory;
import org.example.borrowit.utils.ItemCondition;
import org.example.borrowit.utils.ItemStatus;
import jakarta.persistence.*;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "Items")
public class Item extends Entity<Integer> {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ItemCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private ItemCondition condition;

    @Column(name = "rent_price", nullable = false)
    private float rentPrice;

    @ElementCollection
    @CollectionTable(name = "ItemImages", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "image_path")
    private List<String> images;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Item(Integer integer, String title, String description, ItemCategory category, ItemCondition condition, float rentPrice, List<String> images, String location, ItemStatus status, User owner) {
        super(integer);
        this.title = title;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.rentPrice = rentPrice;
        this.images = images;
        this.location = location;
        this.status = status;
        this.owner = owner;
    }

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }

    public float getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(float rentPrice) {
        this.rentPrice = rentPrice;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
