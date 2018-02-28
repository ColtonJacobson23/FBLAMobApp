package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.coltonjacobson.fblamobapp.bookData.BooksCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by colto on 1/12/2018.
 */
public class ProfileRecyclerViewFragment extends Fragment {

    /**
     * The Url.
     */
    String url = "https://lizardswimmer.azurewebsites.net/simple/books";
    /**
     * The Json array.
     */
    JSONArray jsonArray;
    /**
     * The Authors.
     */
    ArrayList<String> Authors;
    /**
     * The Titles.
     */
    ArrayList<String> Titles;
    /**
     * The Isb ns.
     */
    ArrayList<String> ISBNs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment,container,false);


        //ArrayList<Book> books = BooksCollection.getBooks();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(new RecyclerViewAdapter(books,getContext()));
        loadBookData();


        return view;

    }

//Simple request function to get all book objects from DB
    private void loadBookData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    jsonArray = new JSONArray(response);
                    Authors = jsonToArraylist(jsonArray, "authors");
                    Titles = jsonToArraylist(jsonArray,"title");
                    ISBNs = jsonToArraylist(jsonArray,"ISBN");



                } catch(JSONException e) {

                    e.printStackTrace();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Couldn't retrieve data", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Json to arraylist array list.
     *
     * @param jArray the j array
     * @param name   the name
     * @return the array list
     * @throws JSONException the json exception
     */
//To turn a JSON array into Arraylists of Book Data
    public ArrayList<String> jsonToArraylist(JSONArray jArray, String name) throws JSONException {

        JSONObject jObject = jArray.getJSONObject(0);
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < jArray.length();i++) {
            jObject = jArray.getJSONObject(i);
            strings.add(jObject.getString(name));
        }
        return strings;

    }


    /**
     * New instance fragment.
     *
     * @return the fragment
     */
    public static Fragment newInstance() {

        return new ProfileRecyclerViewFragment();

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
        private ArrayList<String> authors;
        private ArrayList<String> titles;
        private ArrayList<String> isbns;
        private ArrayList<Book> books;


        /**
         * Instantiates a new Recycler view adapter.
         *
         * @param books   the books
         * @param context the context
         */
        public RecyclerViewAdapter(ArrayList<Book> books, Context context) {

            this.authors = Authors;
            this.titles = Titles;
            this.isbns = ISBNs;
            this.cText = context;
            this.books = books;


        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

            final String bookTitle = "Title";//books.get(position).getName();
            final int bookImage = R.drawable.book_redbackground_launcher_foreground;//books.get(position).getImage();
            final String bookAuthor = "Authos";//books.get(position).getAuthor();
            final boolean isCheckedOut = false;//books.get(position).isCheckedOut();
            final boolean isReserved = false;//books.get(position).isReserved();

            holder.mBookName.setText(bookTitle);
            holder.mImageView.setImageResource(bookImage);
            holder.mAuthorName.setText(bookAuthor);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {

                    Toast.makeText(cText, "You clicked " + bookTitle,Toast.LENGTH_LONG).show();
                    openBookDetailActivity(bookTitle,bookImage,bookAuthor,isCheckedOut,isReserved,position);


                }
            });

        }

        @Override
        public int getItemCount() {
            //Five or more
            return books.size();
        }

        private void openBookDetailActivity(String bookName, int image,String bookAuthor,boolean isCheckedOut, boolean isReserved,int position) {

            Intent intent = new Intent(cText, BookDetailActivity.class);

            //Get data ready to send
            intent.putExtra("BOOK_NAME", bookName);
            intent.putExtra("BOOK_IMAGE", image );
            intent.putExtra("BOOK_AUTHOR",bookAuthor);
            intent.putExtra("BOOK_CHECKEDOUT",isCheckedOut);
            intent.putExtra("BOOK_RESERVED",isReserved);
            intent.putExtra("POSITION",position);


            //Start my activity

            cText.startActivity(intent);

        }


    }
}
