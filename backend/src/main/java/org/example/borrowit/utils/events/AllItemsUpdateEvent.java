package org.example.borrowit.utils.events;

public class AllItemsUpdateEvent extends ItemUpdateEvent{
    public AllItemsUpdateEvent() {
        super(UpdateEvent.ALL_ITEMS);
    }
}
