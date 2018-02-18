package com.example.coltonjacobson.fblamobapp.bookData;

import java.util.ArrayList;

/**
 * Created by colto on 1/15/2018.
 */

public class Book {
    private String title;
    private ArrayList<String> authors;
    private int ISBN;
    private boolean reserved;
    private boolean checkedOut;
    private int pageCount;
    private String base64Encoded;
    private String ficID;
    private String specialCollection;


    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }



    public Book(String title, int pageCount, String base64Encoded, ArrayList<String> authors, int ISBN, boolean reserved,
                boolean checkedOut, String ficID, String specialCollection) {
        this.title = title;
        this.pageCount = pageCount;
        this.authors = null;
        this.ISBN = ISBN;
        this.reserved = reserved;
        this.checkedOut = checkedOut;
        this.base64Encoded = base64Encoded;
        this.ficID = ficID;
        this.specialCollection = specialCollection;
    }

    public String getName() {
        return title;
    }

    public void setName(String t) {
        this.title = t;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String a) {
        this.author = a;
    }

    public int getImage() {
        return 2;
    }

}
