package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by colto on 1/15/2018.
 */

@Entity
public class Book {
    //Variables are ordered based on their order in the database
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "book_title")
    private String title;

    @ColumnInfo(name = "book_pageCount")
    private int pageCount;

    @ColumnInfo(name = "book_base64Encoded")
    //Founds inside the cover object
    private String base64Encoded;

    @ColumnInfo(name = "book_authors")
    //Could be more than one author
    private ArrayList<String> authors;

    @ColumnInfo(name = "book_isbn")
    private String isbn;

    @ColumnInfo(name = "book_deweyDecimal")
    private String deweyDecimal;

    @ColumnInfo(name = "book_reserved")
    private boolean reserved;

    @ColumnInfo(name = "book_checkedOut")
    private boolean checkedOut;

    @ColumnInfo(name = "book_ficID")
    private String ficID;





    public Book() {
        this(null,0,null,null,null,false, false, null,null);
    }

    public Book(String title, int pageCount, String base64Encoded, ArrayList<String> authors, String isbn, boolean reserved,
                boolean checkedOut, String deweyDecimal, String ficID) {

        //Filler values to use for empty parameters
        String titleB = "no title";
        int pageCountB = 200;
        String base64EncodedB = "no base64 string";
        ArrayList<String> authorsB =new ArrayList<String>();
        authorsB.add("no author");
        String isbnB = "000-0000000000";
        String deweyDecimalB = "no dewey";
        String ficIDB = "no ficID";

        //Replaces empty or null parameters with a filler value
        if(title== null || title.equals("")) {
            this.title = titleB;
        } else {
            this.title = title;
        }

        if(pageCount == 0) {
            this.pageCount = pageCountB;
        } else {
            this.pageCount = pageCount;
        }

        if(authors == null ||authors.equals(new ArrayList<String>())) {
            this.authors = authorsB;
        } else {
            this.authors = authors;
        }

        if(isbn == null || isbn.equals("")) {
            this.isbn = isbnB;
        } else {
            this.isbn = isbn;
        }

         if(base64Encoded == null || base64Encoded.equals("")) {
            this.base64Encoded = base64EncodedB;
        } else {
            this.base64Encoded = base64Encoded;
        }

        if(deweyDecimal == null || deweyDecimal.equals("")) {
            this.deweyDecimal = deweyDecimalB;
        } else {
            this.deweyDecimal = deweyDecimal;
        }

        if(ficID == null || ficID.equals("")) {
            this.ficID = ficIDB;
        } else {
            this.ficID = ficID;
        }

        this.reserved = reserved;
        this.checkedOut = checkedOut;






    }



    public static Bitmap toBitmap(Context c, String s) {
        byte[] rawString = Base64.decode(s, Base64.DEFAULT);
        Bitmap imgDecoded = BitmapFactory.decodeByteArray(rawString,0,rawString.length);
        return imgDecoded;
    }

    public static ArrayList<String> makeAuthorList(JSONArray jArray) throws JSONException {

        ArrayList<String> arrayList = new ArrayList<String>();

        if (jArray.equals(new JSONArray())) {
            return new ArrayList<String>();
        }

        for (int i = 0; i < jArray.length();i++)  {
            arrayList.add(jArray.getString(i));
        }

        return arrayList;
    }

    public String toString() {
        return    "\nTitle: " + title
                + "\nPageCount: " + pageCount
                + "\nAuthors " + authors
                + "\nisbn: " + isbn
                + "\nReserved: " + reserved
                + "\nCheckedout: " + checkedOut
                + "\nB64Encoded: " + base64Encoded
                + "\nficID: " + ficID;
    }

    public static ArrayList<Book> makeBookArrayList(Context context, JSONArray jsonArray, ArrayList<Book> bookList) throws JSONException {

        JSONObject jsonObject;
        Book book;
        bookList = new ArrayList<Book>();


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                book = new Book(jsonObject.getString("title"),
                        jsonObject.getInt("pageCount"),
                        jsonObject.getJSONObject("cover").getString("base64Encoded"),
                        Book.makeAuthorList(jsonObject.getJSONArray("authors")),
                        jsonObject.getString("isbn"),
                        false,
                        false,
                        jsonObject.getString("deweyDecimal"),
                        jsonObject.getString("ficID"));
                bookList.add(book);
            } catch (JSONException e) {

                e.printStackTrace();

            }

        }

        return bookList;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setBase64Encoded(String base64Encoded) {
        this.base64Encoded = base64Encoded;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDeweyDecimal(String deweyDecimal) {
        this.deweyDecimal = deweyDecimal;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public void setFicID(String ficID) {
        this.ficID = ficID;
    }

    public String getTitle() {
        return title;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getBase64Encoded() {
        return base64Encoded;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDeweyDecimal() {
        return deweyDecimal;
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public String getFicID() {
        return ficID;
    }
}
