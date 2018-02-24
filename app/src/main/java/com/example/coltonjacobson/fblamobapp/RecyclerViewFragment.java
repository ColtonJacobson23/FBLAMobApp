package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
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
import com.example.coltonjacobson.fblamobapp.bookData.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by colto on 1/12/2018.
 */

public class RecyclerViewFragment extends Fragment {

    ArrayList<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    String getURL = "https://lizardswimmer.azurewebsites.net/simple/books";

    AppDatabase database;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment,container,false);

        try {
            loadData();
            Toast.makeText(getContext(), bookList.toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "FAILUREEEEEEEE", Toast.LENGTH_SHORT).show();
        }

        //Room Database
        Toast.makeText(getContext(), bookList.toString() + "2nd", Toast.LENGTH_SHORT).show();
        database = Room.databaseBuilder(getContext(),AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();
        List<Book> books = database.bookDao().getAllBooks();
        Toast.makeText(getContext(), "*******\n" + books.toString() + "\n*******", Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(bookList,getContext()));
        Toast.makeText(getContext(), bookList.toString() + "3RD", Toast.LENGTH_SHORT).show();


        return view;

    }

    public void loadData() throws JSONException{

        StringRequest stringRequest = new StringRequest(Request.Method.GET, getURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Attempt to get JSON and parse it to a book ArrayList
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    bookList = Book.makeBookArrayList(getContext(),jsonArray,bookList);
                    Toast.makeText(getContext(), bookList.toString() + "in the method", Toast.LENGTH_SHORT).show();


                    //Testing if room DB works from inside request
                    Book a = new Book();
                    Book b = new Book();
                    database.bookDao().insertAll(a,b);

                } catch(JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(getContext(), "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "FROM RESUME FAILURE", Toast.LENGTH_SHORT).show();
        }
    }


    public static Fragment newInstance() {

        return new RecyclerViewFragment();

    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardview;
        private TextView mBookName;
        private TextView mAuthorName;
        private ImageView mImageView;
        private ItemClickListener itemClickListener;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater layoutInflater, ViewGroup container) {

            super(layoutInflater.inflate(R.layout.card_view,container,false));

            mCardview = itemView.findViewById(R.id.card_view);
            mBookName = itemView.findViewById(R.id.text_view);
            mAuthorName =itemView.findViewById(R.id.text_view2);
            mImageView =itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);


        }

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
        private ArrayList<Book> books;


        public RecyclerViewAdapter(ArrayList<Book> booklist, Context context) {


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

            final String bookTitle = books.get(position).getTitle();
            final int bookImage = R.drawable.mockingjay_image;
            final String bookAuthor = books.get(position).getAuthors().toString();
            final boolean isCheckedOut = books.get(position).isCheckedOut();
            final boolean isReserved = books.get(position).isReserved();

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
