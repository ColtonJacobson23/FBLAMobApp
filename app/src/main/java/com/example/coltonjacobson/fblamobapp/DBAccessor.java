package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
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

/*Deprecated after 2/23/18*/




//public class DBAccessor {
//
//    private ArrayList<Book> bookList;
//
//    private RecyclerView recyclerView;
//
//    private ArrayList components;
//
//    private Context context;
//
//    public DBAccessor(Context c, RecyclerView rView) {
//        context = c;
//        recyclerView = rView;
//
//    }
//
//    public ArrayList<Book> loadData(String getURL) throws JSONException{
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//
//
//                    JSONArray jsonArray = new JSONArray(response);
//                    Toast.makeText(context, jsonArray.toString()+"Inside loadData, before makeBookArrayList", Toast.LENGTH_SHORT).show();
//                    makeBookArrayList(context,jsonArray,bookList);
//                    Toast.makeText(context, bookList.toString()+"Inside loadData, after makeBookArrayList", Toast.LENGTH_SHORT).show();
//
//
//
//                } catch(JSONException e) {
//
//                    e.printStackTrace();
//                    Toast.makeText(context, "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(context, "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);
//
//        //bookList is null when it gets here
//        Toast.makeText(context, bookList.toString(), Toast.LENGTH_SHORT).show();
//        return bookList;
//    }
//
//    public static ArrayList<Book> makeBookArrayList(Context context, JSONArray jsonArray, ArrayList<Book> bookList) throws JSONException {
//
//        JSONObject jsonObject;
//        Book book;
//        bookList = new ArrayList<Book>();
//
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                jsonObject = jsonArray.getJSONObject(i);
//                book = new Book(jsonObject.getString("title"),
//                        jsonObject.getInt("pageCount"),
//                        jsonObject.getJSONObject("cover").getString("base64Encoded"),
//                        Book.makeAuthorList(jsonObject.getJSONArray("authors")),
//                        jsonObject.getString("isbn"),
//                        false,
//                        false,
//                        jsonObject.getString("deweyDecimal"),
//                        jsonObject.getString("ficID"));
//                bookList.add(book);
//                Toast.makeText(context, bookList.toString() + "\nInside makeAuthorList", Toast.LENGTH_SHORT).show();
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//
//            }
//
//        }
//
//        return bookList;
//
//    }
//
//}
//
