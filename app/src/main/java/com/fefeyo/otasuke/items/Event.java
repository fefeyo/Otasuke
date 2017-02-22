package com.fefeyo.otasuke.items;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by fefe on 2017/02/21.
 */

public class Event extends RealmObject {
    private String eventName;
    private RealmList<EventDetail> eventDetails;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public RealmList<EventDetail> getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(RealmList<EventDetail> eventDetails) {
        this.eventDetails = eventDetails;
    }
}
