package org.example.borrowit.utils.events;

public class LentItemsUpdateEvent extends ItemUpdateEvent {
    private final String token;


    public LentItemsUpdateEvent(String token) {
        super(UpdateEvent.LENT_ITEMS);
        this.token = token;
    }

    public String getUserToken() {
        return token;
    }
}
