package org.example.borrowit.utils.events;

import java.time.LocalDateTime;

public abstract class ItemUpdateEvent {
    private final LocalDateTime timestamp;
    private final UpdateEvent updateType;

    public ItemUpdateEvent(UpdateEvent updateType) {
        this.updateType = updateType;
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UpdateEvent getUpdateType() {
        return updateType;
    }
}
