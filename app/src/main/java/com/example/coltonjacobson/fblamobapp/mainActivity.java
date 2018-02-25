package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class mainActivity extends BookListFragment implements MapFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {


    @Override
    protected Fragment createFragment() {
        return new RecyclerViewFragment().newInstance();
    }
    ArrayList<Book> bookList;
    boolean DBAttempt;
    private TextView mTextMessage;
    TextView mLoading;
    AppDatabase database;
    String getURL = "https://fblamobileapp.azurewebsites.net/simple/books";
    String postURL = "https://fblamobileapp.azurewebsites.net/user/login";
    String userInformationURL= "https://fblamobileapp.azurewebsites.net/user/info";
    ArrayList<Book> books;

    //Changes the fragment displayed on the screen to the one associated with each button
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {

                //Main fragment with RecyclerView
                case R.id.navigation_home:
                    RecyclerViewFragment fragment = new RecyclerViewFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.recyclerViewContainer,fragment,"FragmentName");
                    fragmentTransaction.commit();

                    setTitle("Home");
                    return true;

                //The Profile fragment
                case R.id.navigation_dashboard:
                    setTitle("My Profile");
                    ProfileFragment fragment2 = new ProfileFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.recyclerViewContainer,fragment2,"FragmentName");
                    fragmentTransaction2.commit();
                    return true;

                //The Help fragment
                case R.id.navigation_notifications:
                    setTitle("Help");
                    MapFragment fragment3 = new MapFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.recyclerViewContainer,fragment3,"FragmentName");
                    fragmentTransaction3.commit();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            loadBookData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            loadUserInformation();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "main")
                .allowMainThreadQueries()
                .build();

        books = (ArrayList)database.bookDao().getAllBooks();




    }


    public void loadBookData() throws JSONException {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Attempt to get JSON and parse it to a book ArrayList
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    database.bookDao().deleteAll();
                    bookList = Book.makeBookArrayList(getApplicationContext(),jsonArray,bookList);
                    for(Book b:bookList) {
                        database.bookDao().insertBook(b);
                    }


                } catch(JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void loadUserInformation() throws JSONException{

        StringRequest stringRequest = new StringRequest(Request.Method.GET, userInformationURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Attempt to get JSON objects for books checked out and books reserved
                try {

                    //Getting checkouts, renewals, and reservations
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray reservations = jsonObject.getJSONArray("reservations");
                    JSONArray checkouts = jsonObject.getJSONArray("checkouts");

                    for (int i = 0; i < checkouts.length(); i++) {
                        Toast.makeText(getApplicationContext(), "Inside checkouts for-loop", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), books.get(1).toString(), Toast.LENGTH_SHORT).show();
                        for (Book b : books) {
                            if (b.getId() == checkouts.getJSONObject(i).getInt("bookID") &&
                                    checkouts.getJSONObject(i).getBoolean("active")) {
                                database.bookDao().getBookByTitle(b.getTitle()).setCheckedOut(true);
                                Toast.makeText(getApplicationContext(), "Made checkout active", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    for (int i = 0; i < reservations.length(); i++) {
                        for (Book b : books) {
                            if (b.getId() == reservations.getJSONObject(i).getInt("bookID") &&
                                    reservations.getJSONObject(i).getBoolean("active")) {
                                database.bookDao().getBookByTitle(b.getTitle()).setReserved(true);
                            }
                        }
                    }


                } catch(JSONException e) {

                    e.printStackTrace();
                    Log.d(TAG, "FINDME: " + e);
                    Toast.makeText(getApplicationContext(), "JSON parse @ loadUserInfo failed", Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "loadData @ loadUserInfo failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "FINDME: " + error);

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<String,String>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", "Bearer " + readTokenFile(getApplicationContext()));
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);



    }

    private String readTokenFile(Context context) {
        String token = "";

        try {
            InputStream inputStream = context.openFileInput("userToken.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                token = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return token;
    }

    private boolean loadFragment(Fragment fragment)  {

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewContainer,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public AppDatabase getDatabase() {
        return database;
    }
}
