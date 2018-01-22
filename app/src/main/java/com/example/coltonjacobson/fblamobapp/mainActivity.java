package com.example.coltonjacobson.fblamobapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fragmentTransaction.replace(R.id.recyclerViewContainer,new RecyclerViewFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    fragmentTransaction.replace(R.id.recyclerViewContainer, new MapFragment()).commit();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_profile);
                    fragmentTransaction.replace(R.id.recyclerViewContainer,new ProfileFragment()).commit();
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
