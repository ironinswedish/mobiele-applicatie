package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAcces {

    @Insert
    void insertMultipleMoves(List<Move> moveList);

    @Insert
    void insertMultipleUnimons(List<Unimon> unimonList);

    @Insert
    void insertMultipleEvent(List<Event> eventList);

    @Update
    void updateMoves(List<Move> moveList);

    @Update
    void updateUnimons(List<Unimon> unimonList);

    @Update
    void updateEvents(List<Event> eventList);

    @Delete
    void deleteMoves(List<Move> moveList);

    @Delete
    void deleteUnimons(List<Unimon> unimonList);

    @Delete
    void deleteEvents(List<Event> eventList);

    @Query("SELECT * FROM Event WHERE eventId = :eventId")
    Event getEventById(int eventId);

    @Query("SELECT * FROM Event")
    List<Event> getEvents();

    @Query("SELECT * FROM Unimon WHERE unimonId = :unimonId")
    Unimon getUnimonById(int unimonId);

    @Query("SELECT * FROM Unimon")
    List<Unimon> getUnimons();

    @Query("SELECT COUNT(*) FROM Event")
    int eventRowCount();

    @Query("SELECT COUNT(*) FROM Unimon")
    int unimonRowCount();

    @Query("SELECT COUNT(*) FROM Unimon WHERE eventId = :eventId")
    int caughtAmount(int eventId);

}
