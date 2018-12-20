package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DaoAcces {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleMoves(List<Move> moveList);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleUnimons(List<Unimon> unimonList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleEvent(List<Event> eventList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUnimon(Unimon unimon);

    @Update
    void updateMoves(List<Move> moveList);

    @Update
    void updateUnimons(List<Unimon> unimonList);

    @Update
    void updateUnimon(Unimon unimon);

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

    @Query("SELECT * FROM Unimon WHERE real_id = :real_id")
    Unimon getUnimonByRealId(String real_id);

    @Query("SELECT * FROM Event")
    List<Event> getEvents();

    @Query("SELECT * FROM Unimon WHERE unimonId = :unimonId")
    Unimon getUnimonById(int unimonId);

    @Query("SELECT * FROM Unimon")
    List<Unimon> getUnimons();

    @Query("SELECT COUNT(*) FROM Event")
    int eventRowCount();

    @Query("SELECT COUNT(*) FROM Unimon WHERE ownerid = :ownerId")
    int unimonRowCount(int ownerId);

    @Query("SELECT COUNT(*) FROM Unimon WHERE eventId = :eventId AND ownerid = :ownId")
    int caughtAmount(int eventId, int ownId);

    @Query("SELECT * FROM Unimon WHERE ownerid = :ownerId")
    List<Unimon> getOwnUnimons(int ownerId);


    @Query("SELECT * FROM Move")
    List<Move> getMoves();
}
