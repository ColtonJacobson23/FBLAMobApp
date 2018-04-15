package com.example.coltonjacobson.fblamobapp.Database;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface CheckoutDAO {

    @Query("SELECT * FROM Checkout ORDER BY dueDate ASC")
    List<Checkout> getAllCheckouts();

    @Query("SELECT * FROM Checkout where bookID = :bookID")
    Checkout getByBookID(int bookID);

    @Query("SELECT * FROM Checkout where userID = :userID")
    List<Checkout> getByUserID(int userID);

    @Insert
    void insertAll(Checkout... checkouts);

    @Insert
    void insertCheckout(Checkout checkout);

    @Query("DELETE FROM Checkout")
    void deleteAll();

    @Query("DELETE FROM Checkout WHERE bookID = :bookID")
    void delete(int bookID);

}
