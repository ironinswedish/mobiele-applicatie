package com.clarysse.jarne.university_go;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

@Entity(indices = {@Index(value = {"naam"}, unique = true)})
public class Event {

    @NonNull
    @PrimaryKey
    private int eventid;
    private String naam;
    private String type;
    private String tags;
    private String beschrijving;
    private int sprite;
    private String moveset;
    private int spawn_rate;
    private int base_health;


    @NonNull
    public int getEventid() {
        return eventid;
    }

    public void setEventid(@NonNull int eventid) {
        this.eventid = eventid;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public int getSprite() {
        return sprite;
    }

    public void setSprite(int sprite) {
        sprite = sprite;
    }

    public String getMoveset() {
        return moveset;
    }

    public void setMoveset(String moveset) {
        this.moveset = moveset;
    }

    public int getSpawn_rate() {
        return spawn_rate;
    }

    public void setSpawn_rate(int spawn_rate) {
        this.spawn_rate = spawn_rate;
    }

    public int getBase_health() {
        return base_health;
    }

    public void setBase_health(int base_health) {
        this.base_health = base_health;
    }

    public Event(@NonNull int eventid, String naam, String type, String tags, String beschrijving, int sprite, String moveset, int spawn_rate, int base_health) {
        this.eventid = eventid;
        this.naam = naam;
        this.type = type;
        this.tags = tags;
        this.beschrijving = beschrijving;
        this.sprite = sprite;
        this.moveset = moveset;
        this.spawn_rate = spawn_rate;
        this.base_health = base_health;
    }

    public Event() {

    }
}
