package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = {"naam"}, unique = true)})
public class Move {

    @NonNull
    @PrimaryKey
    private int moveid;
    private String naam;
    private String beschrijving;
    private String tags;
    private int base_damage;
    private String special_effect;

    public Move(){

    }

    public Move(@NonNull int moveid, String naam, String beschrijving, String tags, int base_damage, String special_effect) {
        this.moveid = moveid;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.tags = tags;
        this.base_damage = base_damage;
        this.special_effect = special_effect;
    }

    public int getMoveid() {
        return moveid;
    }

    public void setMoveid(int moveid) {
        this.moveid = moveid;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getBase_damage() {
        return base_damage;
    }

    public void setBase_damage(int base_damage) {
        this.base_damage = base_damage;
    }

    public String getSpecial_effect() {
        return special_effect;
    }

    public void setSpecial_effect(String special_effect) {
        this.special_effect = special_effect;
    }


}
