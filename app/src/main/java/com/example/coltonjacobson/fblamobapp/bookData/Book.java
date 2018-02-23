package com.example.coltonjacobson.fblamobapp.bookData;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.coltonjacobson.fblamobapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
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

    private String isbn;
    private String deweyDecimal;
    private boolean reserved;
    private boolean checkedOut;
    private String ficID;





    public Book(String title, int pageCount, String base64Encoded, ArrayList<String> authors, String isbn, boolean reserved,
                boolean checkedOut, String deweyDecimal, String ficID) {
        this.title = title;
        this.pageCount = pageCount;
        this.authors = authors;
        this.isbn = isbn;
        this.reserved = reserved;
        this.checkedOut = checkedOut;
        this.base64Encoded = base64Encoded;
        this.deweyDecimal = deweyDecimal;
        this.ficID = ficID;
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
        return title + pageCount + authors + isbn + reserved + checkedOut + base64Encoded + ficID;
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
