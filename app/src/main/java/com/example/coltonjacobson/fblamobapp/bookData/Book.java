package com.example.coltonjacobson.fblamobapp.bookData;

import java.util.ArrayList;

/**
 * Created by colto on 1/15/2018.
 */

public class Book {
    //Variables are ordered based on their order in the database
    private String title;
    private int pageCount;

    //Founds inside the cover object
    private String base64Encoded;

    //Could be more than one author
    private ArrayList<String> authors;

    private int ISBN;
    private int deweyDecimal;
    private boolean reserved;
    private boolean checkedOut;
    private String ficID;
    private String specialCollection;





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


}
