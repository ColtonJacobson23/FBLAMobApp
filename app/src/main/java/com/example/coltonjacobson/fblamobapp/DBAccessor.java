package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coltonjacobson.fblamobapp.bookData.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by colton on 2/18/2018.
 */




public class DBAccessor {

    private Context context;

    public DBAccessor(Context c) {
        context = c;

    }

    public void loadData(String getURL, ArrayList<String> arrayList){



        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    setTitle(jsonObject.getString("title"));

                } catch(JSONException e) {

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Failed to load data.", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private Map<String, Book> makeBookMap(JSONArray jsonArray) {
        Map<String,Book> bookMap = new HashMap<>();
        JSONObject jsonObject = null;
        String title = "";
        Book book = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                title = jsonObject.getString("title");
                book = new Book(jsonObject.getString("title"),
                    jsonObject.getInt("pageCount"),
                    jsonObject.getString("cover")
                );
                bookMap.put(title,book);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

