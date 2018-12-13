package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Unimon {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int unimonid;
    private String nickname;
    private int exp;
    private int level;
    private int eventid;
    private int ownerid;
    private String real_id;

    public Unimon(){
        exp = 0;
    }

    @NonNull
    public int getUnimonid() {
        return unimonid;
    }

    public void setUnimonid(@NonNull int unimonid) {
        this.unimonid = unimonid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getReal_id() {
        return real_id;
    }

    public void setReal_id(String real_id) {
        this.real_id = real_id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Unimon(@NonNull int unimonid, String nickname, int exp, int level, int eventid, int ownerid, String real_id) {
        this.unimonid = unimonid;
        this.nickname = nickname;
        this.exp = exp;
        this.level = level;
        this.eventid = eventid;
        this.ownerid = ownerid;
        this.real_id = real_id;
    }


}
