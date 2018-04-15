package com.example.coltonjacobson.fblamobapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;


/**
 * Created by 2149292 on 2/24/2018.
 */
@TypeConverters({AuthorConverter.class,DateConverter.class})
@Database(entities = {
        Book.class,
        Checkout.class,
        Reservation.class
        }, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Book dao book dao.
     *
     * @return the book dao
     */
    public abstract BookDao bookDao();
    public abstract CheckoutDAO checkoutDAO();
    public abstract ReservationDAO reservationDAO();




}