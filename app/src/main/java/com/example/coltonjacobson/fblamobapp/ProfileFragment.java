package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by colto on 1/12/2018.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    String getURL = "https://fblamobileapp.azurewebsites.net/simple/books";
    AppDatabase database;
    JSONArray jsonArray;
    ArrayList<Book> bookList;
    Button logoutButton;
    ArrayList<Book> books;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        logoutButton = (Button) view.findViewById(R.id.sign_out_button);
        logoutButton.setOnClickListener(this);

        //Room Database
        database = ((mainActivity)getActivity()).getDatabase();
<<<<<<< HEAD
        Log.d(TAG, "doInBackground: after Profile frag is declared" );
=======
>>>>>>> parent of a790d91... Revert "Good Version"
        try {
            Thread.sleep(500);
            books = (ArrayList<Book>)database.bookDao().getAllBooks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD





=======
>>>>>>> parent of a790d91... Revert "Good Version"

//        //Makes a simple request for all book information in the Simple Endpoint
//        try {
//            loadBookData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Data could not be loaded.", Toast.LENGTH_SHORT).show();
//        }


        RecyclerView checkoutsRecyclerView = view.findViewById(R.id.profile_recycler_view);
        RecyclerView reservationsRecyclerView = view.findViewById(R.id.profile_recycler_view2);

        //Setting the checkouts adapter
        //Input ArrayList of checkouts in place of books
        checkoutsRecyclerView.setAdapter(new RecyclerViewAdapter(books,getContext()));

        //Setting the reservations adapter
        //Input ArrayList of reservations in place of books
        reservationsRecyclerView.setAdapter(new RecyclerViewAdapter(books, getContext()));


        //Creates the manager for a horizontal scrolling view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        checkoutsRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        reservationsRecyclerView.setLayoutManager(layoutManager2);



        return view;

    }

    public void loadBookData() throws JSONException{

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
//        try {
//            loadBookData();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "FROM RESUME FAILURE", Toast.LENGTH_SHORT).show();
//        }
    }



    public static Fragment newInstance() {

        return new RecyclerViewFragment();

    }

    @Override
    public void onClick(View view) {
        if (view == logoutButton) {
            Toast.makeText(getContext(), "Logout clicked", Toast.LENGTH_SHORT).show();
            logOut();
        }
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

            super(layoutInflater.inflate(R.layout.profile_card_view,container,false));

            mCardview = itemView.findViewById(R.id.profile_card_view);
            mBookName = itemView.findViewById(R.id.profile_text_view);
            mAuthorName =itemView.findViewById(R.id.profile_text_view2);
            mImageView =itemView.findViewById(R.id.profile_image_view);
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
        private List<Book> books;

        public RecyclerViewAdapter(List<Book> books, Context context) {

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

    private void logOut() {

        String s = "";
        writeTokenToFile(s,getContext());
        database.bookDao().deleteAll();
        getActivity().finish();

    }

    private void writeTokenToFile(String token, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("userToken.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(token);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
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





    //Factory methods for fragment

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setBookList(ArrayList<Book> list) {
        books = list;
    }




}

