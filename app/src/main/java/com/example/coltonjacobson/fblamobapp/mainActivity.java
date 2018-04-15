package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coltonjacobson.fblamobapp.Database.AppDatabase;
import com.example.coltonjacobson.fblamobapp.Database.Book;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;


/**
 * The type Main activity.
 */
public class mainActivity extends BookListFragment implements MapFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {


    @Override
    protected Fragment createFragment() {
        return new RecyclerViewFragment().newInstance();
    }

    /**
     * The Book list.
     */
    ArrayList<Book> bookList;
    /**
     * The Db attempt.
     */
    boolean DBAttempt;
    private TextView mTextMessage;
    /**
     * The M loading.
     */
    TextView mLoading;
    /**
     * The Database.
     */
    AppDatabase database;
    /**
     * The Get url.
     */
    String getURL = "https://fblamobileapp.azurewebsites.net/simple/books";
    /**
     * The Post url.
     */
    String postURL = "https://fblamobileapp.azurewebsites.net/user/login";
    /**
     * The User information url.
     */
    String userInformationURL= "https://fblamobileapp.azurewebsites.net/user/info";
    /**
     * The Books.
     */
    ArrayList<Book> books;
    /**
     * The Recycler view fragment.
     */
    RecyclerViewFragment recyclerViewFragment;
    /**
     * The Profile fragment.
     */
    ProfileFragment profileFragment;
    /**
     * The Map fragment.
     */
    MapFragment mapFragment;


    //Changes the fragment displayed on the screen to the one associated with each button
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {

                //Main fragment with RecyclerView
                case R.id.navigation_home:
                    recyclerViewFragment = new RecyclerViewFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.recyclerViewContainer,recyclerViewFragment,"FragmentName");
                    fragmentTransaction.commit();

                    setTitle("Home");
                    return true;

                //The Profile fragment
                case R.id.navigation_dashboard:
                    setTitle("My Profile");
                    ProfileFragment profileFragment = new ProfileFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.recyclerViewContainer,profileFragment,"FragmentName");
                    fragmentTransaction2.commit();
                    return true;

                //The Help fragment
                case R.id.navigation_notifications:
                    setTitle("Map");
                    MapFragment mapFragment = new MapFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.recyclerViewContainer,mapFragment,"FragmentName");
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
        database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "main")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        try {
            getBookData();
            books = (ArrayList)database.bookDao().getAllBooks();
            for(Book b:books) {
                Log.d(TAG, "doInBackground: " + b.isCheckedOut());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }


    /**
     * Gets book data.
     *
     * @throws JSONException the json exception
     */
//Loads all of the books from the database
    public void getBookData() throws JSONException {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Attempt to get JSON and parse it to a book ArrayList
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    database.bookDao().deleteAll();
                    bookList = Book.makeBookArrayList(getApplicationContext(), jsonArray, bookList);
                    for (Book b : bookList) {
                        database.bookDao().insertBook(b);
                    }


                } catch (JSONException e) {

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


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Gets database.
     *
     * @return the database
     */
    public AppDatabase getDatabase() {
        return database;
    }

    /**
     * Gets profile fragment.
     *
     * @return the profile fragment
     */
    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    /**
     * Gets recycler view fragment.
     *
     * @return the recycler view fragment
     */
    public RecyclerViewFragment getRecyclerViewFragment() {
        return recyclerViewFragment;
    }
}
