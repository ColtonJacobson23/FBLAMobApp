//package com.example.coltonjacobson.fblamobapp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//import static android.content.ContentValues.TAG;
//
//
///**
// * Used to make Queries to the database
// */
//
//public class QueryMaker extends AsyncTask<String,Void,Object> {
//
//    private Activity activity;
//    private Context context;
//    private AppDatabase database;
//    ArrayList<Book> books;
//    Book book;
//
//    //Used to determine what to cast the return object as in postExecute()
//    private String returnType;
//
//    public QueryMaker(Activity activity, RecyclerViewFragment fragment, AppDatabase database, ArrayList<Book> books, Book book) {
//
//        this.activity = activity;
//        this.context = this.activity.getApplicationContext();
//        this.database = database;
//        this.books = new ArrayList<Book>();
//        this.book = new Book();
//
//    }
//
//    @Override
//    protected Object doInBackground(String... strings) {
//        Object rObject = new Object();
//        Log.d(TAG, "doInBackground: " + strings[0]);
//        //First string passed into the AsyncTask will always be the type of query
//        String queryType = strings[0];
//
//        //Gets all of the books in database
//        if (queryType.equals("GET_ALL")) {
//            books = (ArrayList<Book>) database.bookDao().getAllBooks();
//            returnType = "ArrayList";
//            rObject = books;
//        }
//
//        //If getting one book, second string passed into AsyncTask is the ID number or the title
//        //Gets a single book from database
//        if (queryType.equals("GET_BY_ID")) {
//            String sID = strings[1];
//            //Converts ID number string to int
//            int ID = Integer.parseInt(sID);
//            book = database.bookDao().getBookByID(ID);
//            returnType = "Book";
//            rObject = book;
//        }
//
//        if (queryType.equals("GET_BY_NAME")) {
//            String title = strings[1];
//            book = database.bookDao().getBookByTitle(title);
//            returnType = "Book";
//            rObject = book;
//        }
//
//        if (queryType.equals("DELETE_ALL")) {
//            database.bookDao().deleteAll();
//            returnType = "Null";
//            rObject = null;
//        }
//
//        return rObject;
//
//    }
//
//    @Override
//    protected void onPostExecute(Object o) {
//
//        if (returnType.equals("ArrayList")) {
//
//        }
//
//        if (returnType.equals("Book")) {
//
//        }
//
//        if (returnType.equals("Null")) {
//            return;
//        }
//
//    }
//}
