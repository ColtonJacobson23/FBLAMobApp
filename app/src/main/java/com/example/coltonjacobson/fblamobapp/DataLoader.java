package com.example.coltonjacobson.fblamobapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Contains all of the Async tasks needed throughout the program
 */

public class DataLoader {

     public static class AllBooksLoader extends AsyncTask<Void,Void,Void> {

         Context context;
         AppDatabase database;
         String getUserInfoURL;
         String getURL;

         //Necessary inside the class
         ArrayList<Book> books;
         ArrayList<Book> bookList;


         public AllBooksLoader(Context context, AppDatabase database, String getURL, String getUserInfoURL) {

             this.context = context;
             this.database = database;
             this.getUserInfoURL = getUserInfoURL;
             this.getURL = getURL;
             this.books = new ArrayList<Book>();


         }


         @Override
         protected Void doInBackground(Void... voids) {

             try {
                 getBookData();
<<<<<<< HEAD
                 Log.d(TAG, "doInBackground: after getbook data" );
                 books = (ArrayList<Book>) database.bookDao().getAllBooks();
                 getUserInformation();
                 Log.d(TAG, "doInBackground: after getuser data" );
=======
                 books = (ArrayList<Book>) database.bookDao().getAllBooks();
                 getUserInformation();
>>>>>>> parent of a790d91... Revert "Good Version"
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             return null;
         }

         @Override
         protected void onPreExecute() {

             Toast.makeText(context, "Loading Data Now...", Toast.LENGTH_LONG).show();

         }

         @Override
         protected void onPostExecute(Void aVoid) {
<<<<<<< HEAD
             Log.d(TAG, "doInBackground: onPostExecute" );
             try {
                 getUserInformation();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
=======
             super.onPostExecute(aVoid);
>>>>>>> parent of a790d91... Revert "Good Version"
             Toast.makeText(context, "Data has been loaded!", Toast.LENGTH_LONG).show();
         }

         //Loads all of the books from the database
         public void getBookData() throws JSONException {


             StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                     //Attempt to get JSON and parse it to a book ArrayList
                     try {

                         JSONArray jsonArray = new JSONArray(response);

                         database.bookDao().deleteAll();
                         bookList = Book.makeBookArrayList(context, jsonArray, bookList);
                         for (Book b : bookList) {
                             database.bookDao().insertBook(b);
                         }


                     } catch (JSONException e) {

                         e.printStackTrace();
                         Toast.makeText(context, "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                     }

                 }
             },
                     new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                             Toast.makeText(context, "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                         }
                     });
             RequestQueue requestQueue = Volley.newRequestQueue(context);
             requestQueue.add(stringRequest);

         }

<<<<<<< HEAD

=======
>>>>>>> parent of a790d91... Revert "Good Version"
         //Gets all of a user's checkouts and reservations, and get user identification info
         public void getUserInformation() throws JSONException {

             StringRequest stringRequest = new StringRequest(Request.Method.GET, getUserInfoURL, new Response.Listener<String>() {

                 @Override
                 public void onResponse(String response) {
                     //Attempt to get JSON objects for books checked out and books reserved
                     try {

                         //Getting checkouts, renewals, and reservations
                         JSONObject jsonObject = new JSONObject(response);
                         JSONArray reservations = jsonObject.getJSONArray("reservations");
                         JSONArray checkouts = jsonObject.getJSONArray("checkouts");

                         for (int i = 0; i < checkouts.length(); i++) {
                             for (Book b : books) {
<<<<<<< HEAD
                                 if (b.getBookID() == checkouts.getJSONObject(i).getInt("bookID") &&
                                         checkouts.getJSONObject(i).getBoolean("active")) {
                                     b.setCheckedOut(true);//database.bookDao().getBookByID(b.getBookID()).setCheckedOut(true);
=======
                                 if (b.getId() == checkouts.getJSONObject(i).getInt("bookID") &&
                                         checkouts.getJSONObject(i).getBoolean("active")) {
                                     database.bookDao().getBookByTitle(b.getTitle()).setCheckedOut(true);
>>>>>>> parent of a790d91... Revert "Good Version"
                                     Toast.makeText(context, "Made checkout active", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }

                         for (int i = 0; i < reservations.length(); i++) {
                             for (Book b : books) {
<<<<<<< HEAD
                                 if (b.getBookID() == reservations.getJSONObject(i).getInt("bookID") &&
                                         reservations.getJSONObject(i).getBoolean("active")) {
                                     b.setReserved(true);//database.bookDao().getBookByID(b.getBookID()).setReserved(true);
=======
                                 if (b.getId() == reservations.getJSONObject(i).getInt("bookID") &&
                                         reservations.getJSONObject(i).getBoolean("active")) {
                                     database.bookDao().getBookByTitle(b.getTitle()).setReserved(true);
>>>>>>> parent of a790d91... Revert "Good Version"
                                 }
                             }
                         }

<<<<<<< HEAD
                         database.bookDao().deleteAll();
                         for(Book b:books) {
                             database.bookDao().insertBook(b);
                         }






=======
>>>>>>> parent of a790d91... Revert "Good Version"

                     } catch (JSONException e) {

                         e.printStackTrace();
                         Log.d(TAG, "FINDME: " + e);
                         Toast.makeText(context, "JSON parse @ loadUserInfo failed", Toast.LENGTH_SHORT).show();

                     }

                 }

             },
                     new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                             Toast.makeText(context, "loadData @ loadUserInfo failed", Toast.LENGTH_SHORT).show();
                             Log.d(TAG, "FINDME: " + error);

                         }
                     }) {
                 @Override
                 public Map<String, String> getHeaders() throws AuthFailureError {
                     Map<String, String> header = new HashMap<String, String>();
                     header.put("Content-Type", "application/json");
                     header.put("Authorization", "Bearer " + readTokenFile(context));
                     return header;
                 }
             };
             RequestQueue requestQueue = Volley.newRequestQueue(context);
             requestQueue.add(stringRequest);


         }

         private String readTokenFile(Context context) {
             String token = "";

             try {
                 InputStream inputStream = context.openFileInput("userToken.txt");

                 if (inputStream != null) {
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                     String receiveString = "";
                     StringBuilder stringBuilder = new StringBuilder();

                     while ((receiveString = bufferedReader.readLine()) != null) {
                         stringBuilder.append(receiveString);
                     }

                     inputStream.close();
                     token = stringBuilder.toString();
                 }
             } catch (FileNotFoundException e) {
                 Log.e("login activity", "File not found: " + e.toString());
             } catch (IOException e) {
                 Log.e("login activity", "Can not read file: " + e.toString());
             }

             return token;
         }


     }





    /*
    * Next Class
    * */
    //Gets all books and puts them into an ArrayList
//    public static class GetAllBooks extends AsyncTask<Void,Void,ArrayList<Book>> {
//
//        Context context;
//        AppDatabase database;
//
//        //Necessary inside the class
//        ArrayList<Book> books;
//
//        RecyclerViewFragment recyclerViewFragment;
//        ProfileFragment profileFragment;
//
//
//        public GetAllBooks(Context context, RecyclerViewFragment recyclerViewFragment, AppDatabase database, ArrayList<Book> books) {
//
//            this.context = context;
//            this.database = database;
//            this.books = books;
//            this.recyclerViewFragment = recyclerViewFragment;
//
//
//        }
//
//        public GetAllBooks(Context context, ProfileFragment profileFragment, AppDatabase database, ArrayList<Book> books) {
//
//            this.context = context;
//            this.database = database;
//            this.books = books;
//            this.profileFragment = profileFragment;
//
//
//        }
//
//
//        @Override
//        protected ArrayList<Book> doInBackground(Void... voids) {
//
//            books = (ArrayList<Book>) database.bookDao().getAllBooks();
//            Log.d(TAG, "doInBackground: " + books.toString());
//            return books;
//        }
//
//
//        @Override
//        protected void onPostExecute(ArrayList<Book> list) {
//            super.onPostExecute(list);
//            if(profileFragment == null) {
//                recyclerViewFragment.setBookList(new ArrayList<Book>());
//            } else {
//                profileFragment.setBookList(new ArrayList<Book>());
//            }
//
//
//        }
//    }





}