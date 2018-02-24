//package com.example.coltonjacobson.fblamobapp;
//
//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
//import java.util.ArrayList;
//
///**
// * Created by 2149292 on 2/24/2018.
// */
//
//@Entity
//public class BookEntity {
//
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @ColumnInfo(name = "book_title")
//    private String title;
//
//    @ColumnInfo(name = "book_pageCount")
//    private int pageCount;
//
//    @ColumnInfo(name = "book_base64Encoded")
//    //Founds inside the cover object
//    private String base64Encoded;
//
//    @ColumnInfo(name = "book_authors")
//    //Could be more than one author
//    private ArrayList<String> authors;
//
//    @ColumnInfo(name = "book_isbn")
//    private String isbn;
//
//    @ColumnInfo(name = "book_deweyDecimal")
//    private String deweyDecimal;
//
//    @ColumnInfo(name = "book_reserved")
//    private boolean reserved;
//
//    @ColumnInfo(name = "book_checkedOut")
//    private boolean checkedOut;
//
//    @ColumnInfo(name = "book_ficID")
//    private String ficID;
//
//}
