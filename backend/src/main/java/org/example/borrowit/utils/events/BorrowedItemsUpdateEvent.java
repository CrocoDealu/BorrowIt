package org.example.borrowit.utils.events;

public class BorrowedItemsUpdateEvent extends ItemUpdateEvent{
    private int userId;

    public BorrowedItemsUpdateEvent(int userId) {
        super(UpdateEvent.BORROWED_ITEMS);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
