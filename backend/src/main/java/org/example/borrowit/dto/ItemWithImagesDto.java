package org.example.borrowit.dto;

import org.example.borrowit.domain.Entity;
import org.example.borrowit.domain.Item;

import java.util.List;

public class ItemWithImagesDto extends Entity<Integer> {
    private String title;
    private String description;
    private String category;
    private String condition;
    private float rentPrice;
    private List<String> images;
    private String location;
    private String status;
    private String owner;

    public ItemWithImagesDto(Item item, List<String> images) {
        super(item.getId());
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.category = item.getCategory().toString();
        this.condition = item.getCondition().toString();
        this.rentPrice = item.getRentPrice();
        this.images = images;
        this.location = item.getLocation();
        this.status = item.getStatus().toString();
        this.owner = item.getOwner().getName();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ItemWithImagesDto{" +
                "id=" + getId() +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", condition='" + condition + '\'' +
                ", rentPrice=" + rentPrice +
                ", images=" + images +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
