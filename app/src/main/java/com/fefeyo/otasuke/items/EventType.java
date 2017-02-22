package com.fefeyo.otasuke.items;

/**
 * Created by fefe on 2017/02/21.
 */

public enum EventType {
    RELEASE(0),
    DATE(1),
    EARLY(2),
    FANMEETING(3),
    LIVE(4),
    OTHERS(5);

    private final int id;

    EventType(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static EventType getType(final int id) {
        EventType[] types = EventType.values();
        for (EventType type : types) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

}
