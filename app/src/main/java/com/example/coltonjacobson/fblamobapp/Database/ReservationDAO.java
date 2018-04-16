package com.example.coltonjacobson.fblamobapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReservationDAO {

    @Query("SELECT * FROM Reservation")
    List<Reservation> getAllReservations();

    @Query("SELECT * FROM Reservation where bookID = :bookID")
    Reservation getByBookID(int bookID);

    @Query("SELECT * FROM Reservation where userID = :userID")
    List<Reservation> getByUserID(int userID);

    @Insert
    void insertAll(Reservation... reservations);


    @Insert
    void insertReservation(Reservation reservation);


    @Query("DELETE FROM Reservation")
    void deleteAll();

    @Query("DELETE FROM Reservation WHERE rID = :rID")
    void delete(int rID);

}
