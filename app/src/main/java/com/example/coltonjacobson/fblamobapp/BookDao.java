package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.coltonjacobson.fblamobapp.bookData.Book;

import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */
@Dao
public interface BookDao {

    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    @Insert
    void insertAll(Book... books);



}
