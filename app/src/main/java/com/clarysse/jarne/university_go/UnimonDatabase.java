package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Event.class,Move.class,Unimon.class}, version=16,exportSchema =false)
public abstract class UnimonDatabase extends RoomDatabase {
    public abstract DaoAcces daoAcces();
}
