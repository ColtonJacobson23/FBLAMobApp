//package com.example.coltonjacobson.fblamobapp;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import static android.content.ContentValues.TAG;
//
///**
// * Contains all of the Async tasks needed throughout the program
// */
//
//public class DataLoader {
//
//    public static class checkOutBook extends AsyncTask<Void, Void, Void> {
//
//        Context context;
//        AppDatabase database;
//        String getUserInfoURL;
//        String getURL;
//
//        //Necessary inside the class
//        ArrayList<Book> books;
//        ArrayList<Book> bookList;
//
//
//        public checkOutBook (Context context, AppDatabase database, String getURL, String getUserInfoURL) {
//
//            this.context = context;
//            this.database = database;
//            this.getUserInfoURL = getUserInfoURL;
//            this.getURL = getURL;
//            this.books = new ArrayList<Book>();
//
//
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            try {
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            Toast.makeText(context, "Loading Data Now...", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//    }
//}
