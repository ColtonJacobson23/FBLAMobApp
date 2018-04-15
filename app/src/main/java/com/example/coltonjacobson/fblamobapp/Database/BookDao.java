package com.example.coltonjacobson.fblamobapp.Database;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */
@Dao
public interface BookDao {

    /**
     * Gets all books.
     *
     * @return the all books
     */
    @Query("SELECT * FROM Book")
    List<Book> getAllBooks();

    /**
     * Gets book by id.
     *
     * @param bookID the book id
     * @return the book by id
     */
/* Not quite working */
    @Query("SELECT * FROM Book where bookID = :bookID")
    Book getBookByID(int bookID);

    /**
     * Gets book by title.
     *
     * @param title the title
     * @return the book by title
     */
    @Query("SELECT * FROM Book where book_title = :title")
    Book getBookByTitle(String title);

    /**
     * Insert all.
     *
     * @param books the books
     */
    @Insert
    void insertAll(Book... books);

    /**
     * Insert book.
     *
     * @param book the book
     */
    @Insert
    void insertBook(Book book);

    /**
     * Delete all.
     */
    @Query("DELETE FROM Book")
    void deleteAll();

    @Query("UPDATE book SET book_checkedOut = :chkOut WHERE bookID =:ID")
    void setCheckedOut(int ID, boolean chkOut);

    @Query("UPDATE book SET book_reserved = :reserve WHERE bookID =:ID")
    void setReserved(int ID, boolean reserve);  



}
