package com.clarysse.jarne.university_go;

import com.google.android.gms.maps.model.LatLng;

public class Encounter {
    private LatLng latlng;
    private Event event;
    private Unimon unimon;

    public Encounter(LatLng latlng, Event event, Unimon unimon) {
        this.latlng = latlng;
        this.event = event;
        this.unimon = unimon;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Unimon getUnimon() {
        return unimon;
    }

    public void setUnimon(Unimon unimon) {
        this.unimon = unimon;
    }
}
