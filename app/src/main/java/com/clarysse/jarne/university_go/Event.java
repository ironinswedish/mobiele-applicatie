package com.clarysse.jarne.university_go;
import com.google.android.gms.maps.model.LatLng;


public class Event {

    private LatLng location;
    private String eventName;
    private int level;

    public Event(LatLng location, String eventName, int level) {
        this.level = level;
        this.eventName = eventName;
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
