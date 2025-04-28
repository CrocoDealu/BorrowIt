package org.example.borrowit.utils;

import java.util.ArrayList;
import java.util.List;

public class ItemFilter {
    private ItemStatus status;
    private ItemCategory category;

    public ItemFilter(ItemStatus status, ItemCategory category) {
        this.status = status;
        this.category = category;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public String processFilter() {
        StringBuilder processedFilter = new StringBuilder();
        List<String> values = new ArrayList<>();
        List<String> filters = new ArrayList<>();
        if (status != null) {
            filters.add("status");
            values.add(status.toString());
        }
        if (category != null) {
            filters.add("category");
            values.add(category.toString());
        }

        for (int i = 0; i < filters.size(); i++) {
            if (i != filters.size() - 1) {
                processedFilter.append(filters.get(i)).append("=").append(values.get(i)).append(", ");
            }
        }
        return processedFilter.toString();
    }
}
