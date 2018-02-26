package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;


/**
 * Created by 2149292 on 2/24/2018.
 */
@TypeConverters(AuthorConverter.class)
@Database(entities = {Book.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();


}