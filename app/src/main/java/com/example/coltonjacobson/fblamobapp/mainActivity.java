package com.example.coltonjacobson.fblamobapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class mainActivity extends BookListFragment{

    @Override
    protected Fragment createFragment() {
        return new RecyclerViewFragment().newInstance();
    }

    TextView mDBAttempt;
    private TextView mTextMessage;
    String message;
    String url = "http://lizardswimmer.azurewebsites.net/simple/books";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    RecyclerViewFragment fragment = new RecyclerViewFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.recyclerViewContainer,fragment,"FragmentName");
                    fragmentTransaction.commit();

                    setTitle("Home");
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("Library Map");
                    RecyclerViewFragment fragment2 = new RecyclerViewFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.recyclerViewContainer,fragment2,"FragmentName");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_notifications:
                    setTitle("My Profile");
                    RecyclerViewFragment fragment3 = new RecyclerViewFragment();
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mDBAttempt = (TextView) findViewById(R.id.dbAttempt);

        loadBookData();
        mTextMessage.setText("");







    }

    private boolean loadFragment(Fragment fragment)  {

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewContainer,fragment).commit();
            return true;
        }
        return false;
    }

    private void loadBookData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    mDBAttempt.setText(response);
                    mDBAttempt.setText("");
                    JSONObject jsonObject = new JSONObject(response);

                } catch(JSONException e) {

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mDBAttempt.setText(R.string.db_get_failed);

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

    }
}
