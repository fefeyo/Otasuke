package com.fefeyo.otasuke.items;

/**
 * Created by fefe on 2017/02/21.
 */

public enum EventType {
    RELEASE(0, "リリースイベント"),
    DATE(1, "発売日"),
    EARLY(2, "先行上映会"),
    FANMEETING(3, "ファンミーティング"),
    LIVE(4, "ライブ"),
    OTHERS(5, "その他");

    private final int id;
    private final String name;

    EventType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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
