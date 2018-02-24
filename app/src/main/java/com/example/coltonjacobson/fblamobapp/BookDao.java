package com.example.coltonjacobson.fblamobapp;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */
@Dao
public interface BookDao {

    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    /* Not quite working */
    @Query("SELECT * FROM Book where id = :id")
    Book getBookByID(int id);

    @Query("SELECT * FROM Book where book_title = :title")
    Book getBookByTitle(String title);

    @Insert
    void insertAll(Book... books);

    @Insert
    void insertBook(Book book);

    @Query("DELETE FROM Book")
    void deleteAll();




}
