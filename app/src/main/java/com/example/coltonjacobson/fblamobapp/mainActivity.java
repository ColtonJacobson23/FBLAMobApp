package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


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
        Intent login = new Intent(mainActivity.this,LoginActivity.class);
        startActivity(login);

        mTextMessage = (TextView) findViewById(R.id.message);
        //mLoading = (TextView) findViewById(R.id.loading_text_view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        database = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "main")
                .allowMainThreadQueries()
                .build();




    }

    private boolean loadFragment(Fragment fragment)  {

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewContainer,fragment).commit();
            return true;
        }
        return false;
    }

    public AppDatabase getDatabase() {
        return database;
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }




}
