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
    @PrimaryKey(autoGenerate = false)
    private int bookID;

    @ColumnInfo(name = "book_title")
    private String title;

    @ColumnInfo(name = "book_pageCount")
    private int pageCount;

    @ColumnInfo(name = "book_imagePath")
    //Founds inside the cover object
    private String imagePath;

    @ColumnInfo(name = "book_authors")
    //Could be more than one author
    private ArrayList<String> authors;

    @ColumnInfo(name = "book_description")
    //Founds inside the cover object
    private String description;

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


    /**
     * Instantiates a new Book.
     *
     * @param bookID       the book id
     * @param title        the title
     * @param pageCount    the page count
     * @param authors      the authors
     * @param isbn         the isbn
     * @param reserved     the reserved
     * @param checkedOut   the checked out
     * @param deweyDecimal the dewey decimal
     * @param ficID        the fic id
     * @param imagePath    the image path
     * @param description  the description
     */
    public Book(int bookID, String title, int pageCount, ArrayList<String> authors, String isbn, boolean reserved,
                boolean checkedOut, String deweyDecimal, String ficID, String imagePath, String description) {

        //Filler values to use for empty parameters
        String titleB = "no title";
        int pageCountB = 200;
        String base64EncodedB = "no base64 string";
        ArrayList<String> authorsB =new ArrayList<String>();
        authorsB.add("no author");
        String isbnB = "000-0000000000";
        String deweyDecimalB = "no dewey";
        String ficIDB = "no ficID";
        this.imagePath = imagePath;
        this.description = description;

        this.bookID = bookID;

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


    /**
     * To bitmap bitmap.
     *
     * @param c the c
     * @param s the s
     * @return the bitmap
     */
    public static Bitmap toBitmap(Context c, String s) {
        byte[] rawString = Base64.decode(s, Base64.DEFAULT);
        Bitmap imgDecoded = BitmapFactory.decodeByteArray(rawString,0,rawString.length);
        return imgDecoded;
    }

    /**
     * Make author list array list.
     *
     * @param jArray the j array
     * @return the array list
     * @throws JSONException the json exception
     */
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
                + "\nImagePath: " + imagePath
                + "\nficID: " + ficID
                + "\nDescription: " + description;
    }

    /**
     * Make book array list array list.
     *
     * @param context   the context
     * @param jsonArray the json array
     * @param bookList  the book list
     * @return the array list
     * @throws JSONException the json exception
     */
    public static ArrayList<Book> makeBookArrayList(Context context, JSONArray jsonArray, ArrayList<Book> bookList) throws JSONException {

        JSONObject jsonObject;
        Book book;
        bookList = new ArrayList<Book>();


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                book = new Book(jsonObject.getInt("bookID"),jsonObject.getString("title"),
                        jsonObject.getInt("pageCount"),
                        Book.makeAuthorList(jsonObject.getJSONArray("authors")),
                        jsonObject.getString("isbn"),
                        false,
                        false,
                        jsonObject.getString("deweyDecimal"),
                        jsonObject.getString("ficID"),
                        jsonObject.getString("imagePath"),
                        jsonObject.getString("description"));
                bookList.add(book);
            } catch (JSONException e) {

                e.printStackTrace();

            }

        }

        return bookList;

    }

    /**
     * Gets book id.
     *
     * @return the book id
     */
    public int getBookID() {
        return bookID;
    }

    /**
     * Sets id.
     *
     * @param bookID the book id
     */
    public void setId(int bookID) {
        this.bookID = bookID;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets page count.
     *
     * @param pageCount the page count
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * Sets authors.
     *
     * @param authors the authors
     */
    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets dewey decimal.
     *
     * @param deweyDecimal the dewey decimal
     */
    public void setDeweyDecimal(String deweyDecimal) {
        this.deweyDecimal = deweyDecimal;
    }

    /**
     * Sets reserved.
     *
     * @param reserved the reserved
     */
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    /**
     * Sets checked out.
     *
     * @param checkedOut the checked out
     */
    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    /**
     * Sets fic id.
     *
     * @param ficID the fic id
     */
    public void setFicID(String ficID) {
        this.ficID = ficID;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets page count.
     *
     * @return the page count
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Gets authors.
     *
     * @return the authors
     */
    public ArrayList<String> getAuthors() {
        return authors;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets dewey decimal.
     *
     * @return the dewey decimal
     */
    public String getDeweyDecimal() {
        return deweyDecimal;
    }

    /**
     * Is reserved boolean.
     *
     * @return the boolean
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Is checked out boolean.
     *
     * @return the boolean
     */
    public boolean isCheckedOut() {
        return checkedOut;
    }

    /**
     * Gets fic id.
     *
     * @return the fic id
     */
    public String getFicID() {
        return ficID;
    }

    /**
     * Gets image path.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets image path.
     *
     * @param imagePath the image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
