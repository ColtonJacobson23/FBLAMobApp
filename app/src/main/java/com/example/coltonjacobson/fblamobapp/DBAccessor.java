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
 * Created bycolton on 2/18/2018.
 */




public class DBAccessor {

    private ArrayList<Book> bookList;

    private ArrayList components;

    private Context context;

    public DBAccessor(Context c) {
        context = c;

    }

    public ArrayList<Book> loadData(String getURL) throws JSONException{

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(context, jsonArray.toString()+"HI", Toast.LENGTH_SHORT).show();
                    bookList = makeBookArrayList(jsonArray);



                } catch(JSONException e) {

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

        return bookList;
    }

    private ArrayList<Book> makeBookArrayList(JSONArray jsonArray) throws JSONException {

        makeComponents(jsonArray);
        JSONObject jsonObject;
        Book book;
        bookList = new ArrayList<Book>();

        Toast.makeText(this.context, jsonArray.getJSONObject(0).toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this.context, jsonArray.getJSONObject(1).toString(), Toast.LENGTH_SHORT).show();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                book = new Book(jsonObject.getString("title"),
                        jsonObject.getInt("pageCount"),
                        jsonObject.getJSONObject("cover").getString("base64Encoded"),
                        Book.makeAuthorList(jsonObject.getJSONArray("authors")),
                        jsonObject.getString("isbn"),
                        false,
                        false,
                        jsonObject.getString("deweyDecimal"),
                        jsonObject.getString("ficID"));
                bookList.add(book);
                Toast.makeText(context, bookList.toString(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                e.printStackTrace();

            }

        }

        return bookList;

    }

    private void makeComponents(JSONArray jsonArray) {
        components = new ArrayList();
        String titleB = null;
        String pageCountB = null;
        Object coverB = null;
        String base64EncodedB = null;
        ArrayList<String> authorsB = null;
        String isbnB = null;
        String deweyDecimalB = null;
        String ficIDB = null;
        components.add(titleB);
        components.add(pageCountB);
        components.add(coverB);
        components.add(base64EncodedB);
        components.add(authorsB);
        components.add(isbnB);
        components.add(deweyDecimalB);
        components.add(ficIDB);




    }


}

