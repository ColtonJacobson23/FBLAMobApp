package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * Created by colto on 1/12/2018.
 */
public class RecyclerViewFragment extends Fragment {

    /**
     * The Book list.
     */
    ArrayList<Book> bookList = new ArrayList<>();
    /**
     * The Recycler view.
     */
    RecyclerView recyclerView;
    /**
     * The Get url.
     */
    String getURL = "https://fblamobileapp.azurewebsites.net/simple/books";
    /**
     * The User information url.
     */
    String userInformationURL= "https://fblamobileapp.azurewebsites.net/user/info";

    /**
     * The Database.
     */
    AppDatabase database;
    /**
     * The Books.
     */
    ArrayList<Book> books;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment,container,false);


        //Room Database
        database = ((mainActivity)getActivity()).getDatabase();

        try {
            Thread.sleep(500);
            books = (ArrayList<Book>) database.bookDao().getAllBooks();
            for (Book b : books) {
                Log.d(TAG, "doInBackground: bookIsCheckedOut: " + b.isCheckedOut());
            }
            Log.d(TAG, "doInBackground: after getting books in RecyclerView");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(books,getContext()));
        Log.d(TAG, "doInBackground: after recyclerview is declared" );


        return view;

    }

    /**
     * Load data.
     *
     * @throws JSONException the json exception
     */
    public void loadData() throws JSONException{

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Attempt to get JSON and parse it to a book ArrayList
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    database.bookDao().deleteAll();
                    bookList = Book.makeBookArrayList(getContext(),jsonArray,bookList);
                    for(Book b:bookList) {
                        database.bookDao().insertBook(b);
                    }


                } catch(JSONException e) {

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    /**
     * Load user information.
     *
     * @throws JSONException the json exception
     */
//    public void loadUserInformation() throws JSONException{
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, userInformationURL, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                //Attempt to get JSON objects for books checked out and books reserved
//                try {
//
//                    //Getting checkouts, renewals, and reservations
//                    JSONObject jsonObject = new JSONObject(response);
//                    Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Line 145", Toast.LENGTH_SHORT).show();
//
//                    //This is breaking the JSON
//                    JSONArray checkouts = jsonObject.getJSONArray("checkouts");
//
//                    Toast.makeText(getContext(), "Line 147", Toast.LENGTH_SHORT).show();
//                    JSONArray reservations = jsonObject.getJSONArray("reservations");
//
//                    Toast.makeText(getContext(), "Line 149", Toast.LENGTH_SHORT).show();
//
//
//
//                    for (int i = 0; i < checkouts.length(); i++) {
//                        Toast.makeText(getContext(), "Inside checkouts for-loop", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), books.get(1).toString(), Toast.LENGTH_SHORT).show();
//                        for (Book b : books) {
//                            if (b.getBookID() == checkouts.getJSONObject(i).getInt("bookID") &&
//                                    checkouts.getJSONObject(i).getBoolean("active")) {
//                                database.bookDao().getBookByTitle(b.getTitle()).setCheckedOut(true);
//                                Toast.makeText(getContext(), "Made checkout active", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                    for (int i = 0; i < reservations.length(); i++) {
//                        for (Book b : books) {
//                            if (b.getBookID() == reservations.getJSONObject(i).getInt("bookID") &&
//                                    reservations.getJSONObject(i).getBoolean("active")) {
//                                database.bookDao().getBookByTitle(b.getTitle()).setReserved(true);
//                            }
//                        }
//                    }
//
//
//            } catch(JSONException e) {
//
//                    e.printStackTrace();
//                    Log.d(TAG, "FINDME: " + e);
//                    Toast.makeText(getContext(), "JSON parse @ loadUserInfo failed", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//        },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Toast.makeText(getContext(), "loadData @ loadUserInfo failed", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "FINDME: " + error);
//
//                }
//            })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> header = new HashMap<String,String>();
//                header.put("Content-Type", "application/json");
//                header.put("Authorization", "Bearer " + readTokenFile(getContext()));
//                return header;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
//        try {
//            loadData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "FROM RESUME FAILURE", Toast.LENGTH_SHORT).show();
//        }
    }


    /**
     * New instance fragment.
     *
     * @return the fragment
     */
    public static Fragment newInstance() {

        return new RecyclerViewFragment();

    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardview;
        private TextView mBookName;
        private TextView mAuthorName;
        private ImageView mImageView;
        private ItemClickListener itemClickListener;

        /**
         * Instantiates a new Recycler view holder.
         *
         * @param itemView the item view
         */
        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Instantiates a new Recycler view holder.
         *
         * @param layoutInflater the layout inflater
         * @param container      the container
         */
        public RecyclerViewHolder(LayoutInflater layoutInflater, ViewGroup container) {

            super(layoutInflater.inflate(R.layout.card_view,container,false));

            mCardview = itemView.findViewById(R.id.card_view);
            mBookName = itemView.findViewById(R.id.text_view);
            mAuthorName =itemView.findViewById(R.id.text_view2);
            mImageView =itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);


        }

        /**
         * Sets item click listener.
         *
         * @param itemClickListener the item click listener
         */
        public void setItemClickListener(ItemClickListener itemClickListener) {

            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View view) {

            itemClickListener.onClick(view,getAdapterPosition(),false);

        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        private Context cText;
        private List<Book> books;


        /**
         * Instantiates a new Recycler view adapter.
         *
         * @param booklist the booklist
         * @param context  the context
         */
        public RecyclerViewAdapter(List<Book> booklist, Context context) {


            this.cText = context;
            this.books = booklist;


        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

            String authors = books.get(position).getAuthors().toString();

            final int bookID = books.get(position).getBookID();
            final String bookTitle = books.get(position).getTitle();
            final int bookImage = R.drawable.mockingjay_image;
            final String bookAuthor = authors.substring(1,authors.length()-1);
            final boolean isCheckedOut = books.get(position).isCheckedOut();
            final boolean isReserved = books.get(position).isReserved();
            final String imagePath = books.get(position).getImagePath();
            final String description = books.get(position).getDescription();


            holder.mBookName.setText(bookTitle);
            holder.mImageView.setImageResource(bookImage);
            holder.mAuthorName.setText(bookAuthor);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    openBookDetailActivity(bookID,bookTitle,bookImage,bookAuthor,isCheckedOut,isReserved,position,imagePath, description);


                }
            });

        }

        @Override
        public int getItemCount() {
            //Five or more
            return books.size();
        }

        private void openBookDetailActivity(int bookID, String bookName, int image,String bookAuthor,boolean isCheckedOut,
                                            boolean isReserved,int position, String imagePath, String description) {

            Intent intent = new Intent(cText, BookDetailActivity.class);

            //Get data ready to send
            intent.putExtra("BOOK_ID",bookID);
            intent.putExtra("BOOK_NAME", bookName);
            intent.putExtra("BOOK_IMAGE", image );
            intent.putExtra("BOOK_AUTHOR",bookAuthor);
            intent.putExtra("POSITION",position);
            intent.putExtra("BOOK_IMAGEPATH",imagePath);
            intent.putExtra("BOOK_DESCRIPTION", description);
            intent.putExtra("BOOK_CHECKEDOUT",isCheckedOut);
            intent.putExtra("BOOK_RESERVED",isCheckedOut);


            //Start my activity

            cText.startActivity(intent);

        }


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


    /**
     * Sets book list.
     *
     * @param list the list
     */
    public void setBookList(ArrayList<Book> list) {
        books = list;
    }


//    private void writeBase64ToFile(String base64String, Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("base64Text.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(base64String);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }

}
