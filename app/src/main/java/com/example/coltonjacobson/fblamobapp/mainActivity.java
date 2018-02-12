package com.example.coltonjacobson.fblamobapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class mainActivity extends BookListFragment implements MapFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    @Override
    protected Fragment createFragment() {
        return new RecyclerViewFragment().newInstance();
    }

    boolean DBAttempt;
    private TextView mTextMessage;
    TextView mLoading;
    String getURL = "http://lizardswimmer.azurewebsites.net/simple/books";
    String postURL = "http://lizardswimmer.azurewebsites.net/login";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    RecyclerViewFragment fragment = new RecyclerViewFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.recyclerViewContainer,fragment,"FragmentName");
                    fragmentTransaction.commit();
                    loadBookData();

                    setTitle("Home");
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("Library Map");
                    MapFragment fragment2 = new MapFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.recyclerViewContainer,fragment2,"FragmentName");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigation_notifications:
                    setTitle("My Profile");
                    ProfileFragment fragment3 = new ProfileFragment();
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
        mLoading = (TextView) findViewById(R.id.loading_text_view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadBookData();
        mTextMessage.setText("");
        authenticate();



    }

    private boolean loadFragment(Fragment fragment)  {

        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.recyclerViewContainer,fragment).commit();
            return true;
        }
        return false;
    }

    private void loadBookData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    DBAttempt = true;
                    setTitle(jsonObject.getString("title"));

                } catch(JSONException e) {

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        DBAttempt = false;

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Sends a push request to the database to authenticate the username and password
    //Returns either a JSONObject with a token verifying the username and password, or returns an empty JSON Object
    public JSONObject authenticate() {

        //Stores the token value, if any
        final JSONObject jResponse = new JSONObject();

        //Storing username and password in JSONObject
        final JSONObject info = new JSONObject();
        try {
            info.put("username", "1111111");
            info.put("password", "password");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue rQueue = Volley.newRequestQueue(this);
        //Posting the object containing the username and password
        //If correct, a JWT token will be inserted into jResponse
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,postURL,info,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            jResponse.put("token",response.get("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mainActivity.this, "IT WORKED!", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error response", error.toString());
                        Toast.makeText(mainActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            public Map<String,String> getParams()
            {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Content-Type", "application/json");
                parameters.put("Accept", "application/json");
                return parameters;
            }

        };
        rQueue.add(postRequest);

        //Returns the token, or the empty JSON
        return jResponse;

    }


}
