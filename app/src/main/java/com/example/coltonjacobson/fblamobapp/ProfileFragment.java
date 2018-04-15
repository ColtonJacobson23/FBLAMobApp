package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bumptech.glide.Glide;
import com.example.coltonjacobson.fblamobapp.Database.AppDatabase;
import com.example.coltonjacobson.fblamobapp.Database.Book;
import com.example.coltonjacobson.fblamobapp.Database.Checkout;
import com.example.coltonjacobson.fblamobapp.Database.Reservation;
//import com.example.coltonjacobson.fblamobapp.bookData.BooksCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by colto on 1/12/2018.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    /**
     * The Get url.
     */
    String getURL = "https://fblamobileapp.azurewebsites.net/simple/books";
    /**
     * The Database.
     */
    AppDatabase database;
    /**
     * The Json array.
     */
    JSONArray jsonArray;
    /**
     * The Book list.
     */
    ArrayList<Book> bookList;
    /**
     * The Logout button.
     */
    Button logoutButton;
    /**
     * The Books.
     */
    ArrayList<Book> books;

    ArrayList<Checkout> myCheckedOut = new ArrayList<Checkout>();
    ArrayList<Reservation> myReserved = new ArrayList<Reservation>();
    ArrayList<Book> myReservedBooks = new ArrayList<Book>();
    ArrayList<Book> myCheckedOutBooks = new ArrayList<Book>();
    String userinfoURL = "https://fblamobileapp.azurewebsites.net/user/info"; //GET Request

    //Shared preferences file
    SharedPreferences sharedPref;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        logoutButton = (Button) view.findViewById(R.id.sign_out_button);
        logoutButton.setOnClickListener(this);

        TextView noCheckoutsText = view.findViewById(R.id.noCheckoutsText);
        TextView noReservationsText = view.findViewById(R.id.noReservationsText);

        //Room Database
        database = ((mainActivity)getActivity()).getDatabase();
        Log.d(TAG, "doInBackground: after Profile frag is declared" );

        try {
            Thread.sleep(500);
            books = (ArrayList<Book>)database.bookDao().getAllBooks();
            for (int i = 0; i < database.checkoutDAO().getAllCheckouts().size(); i++) {
                List<Checkout> ck = database.checkoutDAO().getAllCheckouts();
                myCheckedOutBooks.add(database.bookDao().getBookByID(ck.get(i).getBookID()));
            }



            for (int i = 0; i < database.reservationDAO().getAllReservations().size(); i++) {
                List<Reservation> re = database.reservationDAO().getAllReservations();
                myReservedBooks.add(database.bookDao().getBookByID(re.get(i).getBookID()));
            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecyclerView checkoutsRecyclerView = view.findViewById(R.id.profile_recycler_view);
        RecyclerView reservationsRecyclerView = view.findViewById(R.id.profile_recycler_view2);
        if (myCheckedOutBooks.size()==0) {
            checkoutsRecyclerView.setVisibility(View.INVISIBLE);
            noCheckoutsText.setVisibility(View.VISIBLE);
        } else {
            noCheckoutsText.setVisibility(View.INVISIBLE);
            noCheckoutsText.setHeight(0);
        }

        if (myReservedBooks.size()==0) {
            reservationsRecyclerView.setVisibility(View.INVISIBLE);
            noReservationsText.setVisibility(View.VISIBLE);
        } else {
            noReservationsText.setVisibility(View.INVISIBLE);
            noReservationsText.setHeight(0);
        }

        //Linear Searches GO HERE
        for(int i = myCheckedOut.size()-2; i >= 0; i--) {
            if (myCheckedOutBooks.get(i).getBookID() == myCheckedOutBooks.get(i+1).getBookID()) {
                myCheckedOutBooks.remove(myCheckedOutBooks.get(i+1));
            }
        }

        for(int i = myReservedBooks.size()-2; i >= 0; i--) {
            if (myReservedBooks.get(i).getBookID() == myReservedBooks.get(i+1).getBookID()) {
                myReservedBooks.remove(myReservedBooks.get(i+1));
            }
        }


        //Setting the checkouts adapter
        //Input ArrayList of checkouts in place of books
        checkoutsRecyclerView.setAdapter(new RecyclerViewAdapter(myCheckedOutBooks,getContext()));

        //Setting the reservations adapter
        //Input ArrayList of reservations in place of books
        reservationsRecyclerView.setAdapter(new RecyclerViewAdapter(myReservedBooks, getContext()));


        //Creates the manager for a horizontal scrolling view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        checkoutsRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        reservationsRecyclerView.setLayoutManager(layoutManager2);



        return view;

    }

    /**
     * Load book data.
     *
     * @throws JSONException the json exception
     */
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


    /**
     * New instance fragment.
     *
     * @return the fragment
     */
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

            super(layoutInflater.inflate(R.layout.profile_card_view,container,false));

            mCardview = itemView.findViewById(R.id.profile_card_view);
            mImageView =itemView.findViewById(R.id.profile_image_view);
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
         * @param books   the books
         * @param context the context
         */
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
        public void onBindViewHolder(ProfileFragment.RecyclerViewHolder holder, int position) {

            String authors = books.get(position).getAuthors().toString();

            final int bookID = books.get(position).getBookID();
            final String bookTitle = books.get(position).getTitle();
            final int bookImage = R.drawable.mockingjay_image;
            final String bookAuthor = authors.substring(1,authors.length()-1);
            final boolean isCheckedOut = books.get(position).isCheckedOut();
            final boolean isReserved = books.get(position).isReserved();
            final String imagePath = books.get(position).getImagePath();
            final String description = books.get(position).getDescription();

            Glide.with(getContext()).load("https://fblamobileapp.azurewebsites.net/images/" + imagePath).into(holder.mImageView);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {

                    openBookDetailActivity(bookID,bookTitle,bookImage,bookAuthor,isCheckedOut,isReserved,position,imagePath, description);


                }
            });

        }


        @Override
        public int getItemCount() {
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
            intent.putExtra("BOOK_RESERVED",isReserved);


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

    /**
     * Instantiates a new Profile fragment.
     */
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


    /**
     * On button pressed.
     *
     * @param uri the uri
     */
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
        /**
         * On fragment interaction.
         *
         * @param uri the uri
         */
// TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Sets book list.
     *
     * @param list the list
     */
    public void setBookList(ArrayList<Book> list) {
        books = list;
    }

    /**
     * Gets user data.
     *
     * @throws JSONException the json exception
     */
////Loads all of the books from the database
//    public void getUserInfo() throws JSONException {
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, userinfoURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                //Attempt to get JSON and parse it to a book ArrayList
//                try {
//
//                    Toast.makeText(getContext(), "First Checkout", Toast.LENGTH_SHORT).show();
//                    JSONObject jsonObject = new JSONObject(response);
//                    myReserved = Reservation.makeReservationList(jsonObject.getJSONArray("reservations"));
//                    myCheckedOut = Checkout.makeCheckoutList(jsonObject.getJSONArray("checkouts"));
//                    for (int i = 0; i < myCheckedOut.size(); i++) {
//                        database.checkoutDAO().insertCheckout(myCheckedOut.get(i));
//                    }
//                    for (int i = 0; i < myReserved.size(); i++) {
//                        database.reservationDAO().insertReservation(myReserved.get(i));
//                    }
//
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(getContext(), "loadData @ DBAccessor failed", Toast.LENGTH_SHORT).show();
//
//                    }
//                }) {
//
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer " + readTokenFile(getContext()));
//                //headers.put("Content-Type", "application/json");
//                headers.put("Accept", "application/json");
//                Log.d(TAG, "getHeaders: a" + headers.get("Accept"));
//                Log.d(TAG, "getHeaders: " + headers.get("Authorization"));
//                return headers;
//            }
//
//
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//    }


}

