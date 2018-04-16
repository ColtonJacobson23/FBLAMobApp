package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.content.ContentValues.TAG;
import com.bumptech.glide.Glide;
import com.example.coltonjacobson.fblamobapp.Database.AppDatabase;
import com.example.coltonjacobson.fblamobapp.Database.Book;
import com.example.coltonjacobson.fblamobapp.Database.Checkout;
import com.example.coltonjacobson.fblamobapp.Database.Reservation;

import org.json.JSONException;
import org.json.JSONObject;

//import com.example.coltonjacobson.fblamobapp.bookData.BooksCollection;

/**
 * Created by colto on 1/15/2018.
 */
public class BookDetailActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener {
    /**
     * The Map container.
     */
    FrameLayout mapContainer;
    /**
     * The Book name text.
     */
    TextView bookNameText;
    /**
     * The Book author text.
     */
    TextView bookAuthorText;
    /**
     * The Book image view.
     */
    ImageView bookImageView;
    /**
     * The Checkout btn.
     */
    Button checkoutBtn;
    /**
     * The Reserve btn.
     */
    Button reserveBtn;
    /**
     * The Position.
     */
    int position;
    /**
     * The Database.
     */
    AppDatabase database;
    /**
     * The Books.
     */
    ArrayList<Book> books;
    /**
     * The Base 64 string.
     */
    String base64String;
    /**
     * The Cover image bitmap.
     */
    Bitmap coverImageBitmap;

    /**
     *
     * @param savedInstanceState
     */

    /**
     * Checkout request URL
     */
    String checkoutURL = "https://fblamobileapp.azurewebsites.net/library/checkout";

    /**
     * Checkout request URL
     */
    String checkinURL = "https://fblamobileapp.azurewebsites.net/library/checkin";

    /**
     * Reserve request URL
     */
    String reserveURL = "https://fblamobileapp.azurewebsites.net/library/reserve";
    String imagePath;

    String description;

    TextView descriptionText;
    Date currentTime;
    Date overdue;
    String userinfoURL = "https://fblamobileapp.azurewebsites.net/user/info"; //GET Request
    Boolean canCheckout;

    /**
     * Book ID
     *
     * @param savedInstanceState
     */
    int bookID;
    int USERID;
    boolean isReserved;
    boolean isCheckedOut;

    String bookTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "main")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookNameText = findViewById(R.id.textView_book_name);
        bookAuthorText = findViewById(R.id.textView_author);
        bookImageView = (ImageView) findViewById(R.id.imageView);
        checkoutBtn = findViewById(R.id.button_checkOut);
        reserveBtn = findViewById(R.id.button_reserve);
        mapContainer = findViewById(R.id.map_container);
        descriptionText = findViewById(R.id.textView_book_description);

        MapFragment fragment = new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_container, fragment, "FragmentName");
        fragmentTransaction.commit();

//        String token = readTokenFile(getApplicationContext());
//        String[] parts = token.split("\\.");
//        String body = parts[1];
//
//        String json = new String(Base64.decode(body,0));


        //Obtains data
        Intent intent = this.getIntent();
        bookID = intent.getExtras().getInt("BOOK_ID");
        bookTitle = intent.getExtras().getString("BOOK_NAME");
        String bookAuthor = intent.getExtras().getString("BOOK_AUTHOR");
        int imageID = intent.getExtras().getInt("BOOK_IMAGE");
        isCheckedOut = database.bookDao().getBookByID(bookID).isCheckedOut();
        isReserved = database.bookDao().getBookByID(bookID).isReserved();
        position = intent.getExtras().getInt("POSITION");
        imagePath = intent.getExtras().getString("BOOK_IMAGEPATH");
        description = intent.getExtras().getString("BOOK_DESCRIPTION");


        Calendar calendar = Calendar.getInstance();
        currentTime = calendar.getTime();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DATE, 28);
        overdue = calendar.getTime();
        calendar.setTime(currentTime);
        Log.d(TAG, "onCreate: " + currentTime);
        Log.d(TAG, "onCreate: " + overdue);


        Glide.with(this).load("https://fblamobileapp.azurewebsites.net/images/" + imagePath).into(bookImageView);


        //Binds data
        bookNameText.setText(bookTitle);
        bookAuthorText.setText(bookAuthor);
        bookImageView.setImageResource(imageID);
        descriptionText.setText(description);

        if (isCheckedOut) {
            checkoutBtn.setText("Check In");
        } else {
            checkoutBtn.setText("Check Out");
        }
        if (isReserved) {
            reserveBtn.setText("Cancel Reserve");
        } else {
            reserveBtn.setText("Reserve");

        }

    }

    /**
     * On checkout clicked.
     *
     * @param view the view
     */
    public void onCheckoutClicked(View view) {

        Button button = (Button) view;
        if (!isCheckedOut) {
            button.setText("Check In");
            database.bookDao().setCheckedOut(bookID, true);
            Toast.makeText(this, "You have checked out " + bookTitle + " for four weeks. It will be due on " + overdue.toString().substring(0, 10) + ".", Toast.LENGTH_LONG).show();
//            postBook(bookID, checkoutURL);
            database.checkoutDAO().insertCheckout(new Checkout(2,bookID,currentTime,overdue));

        } else {
            button.setText("Check Out");
            database.bookDao().setCheckedOut(bookID, false);
            Toast.makeText(this, "You have checked in " + bookTitle + ".", Toast.LENGTH_LONG).show();
//            postBook(bookID, checkinURL);
            database.checkoutDAO().delete(bookID);
        }

    }

    /**
     * On reserve clicked.
     *
     * @param view the view
     */
    public void onReserveClicked(View view) {

        Button button = (Button) view;
        if (!isReserved) {
            button.setText("Cancel Reserve");
            database.bookDao().setReserved(bookID, true);
            Toast.makeText(this, "You have reserved " + bookTitle + ". You are currently " + bookID/4 + "th in the queue.", Toast.LENGTH_LONG).show();
//            postBook(bookID, checkoutURL);
            database.reservationDAO().insertReservation(new Reservation(2,bookID,currentTime));

        } else {
            button.setText("Reserve");
            database.bookDao().setReserved(bookID, false);
            Toast.makeText(this, "You have cancelled your reservation for " + bookTitle + ".", Toast.LENGTH_LONG).show();
//            postBook(bookID, checkinURL);
            database.reservationDAO().delete(bookID);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * @param context
     * @return
     * @deprecated
     */
    private String readBase64File(Context context) {
        String base64String = "";

        try {
            InputStream inputStream = context.openFileInput("base64Text.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                base64String = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return base64String;
    }

    /**
     * Save cover to file boolean.
     *
     * @param fileName The file name.
     * @param bitmap   The Bitmap you want to save.
     * @return true if the Bitmap was saved successfully, false otherwise.
     * @deprecated
     */
    public boolean saveCoverToFile(String fileName, Bitmap bitmap) {

        File directory = new File("FBLAMobApp/app/src/main/res/drawable");//"FBLAMobApp/app/bookCovers"
        File imageFile = new File(directory, fileName);
        Toast.makeText(this, "Image file exists: " + imageFile.exists(), Toast.LENGTH_SHORT).show();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bitmap.compress(PNG, 100, fos);

            fos.close();

            return true;
        } catch (IOException e) {
            Toast.makeText(this, "Closing file error", Toast.LENGTH_SHORT).show();
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(this, "Closing file error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    public void onBackToMain(View view) {

        onBackPressed();

    }

    public int postBook(final int BOOKID, String URL) {


        //Storing username and password in JSONObject
        JSONObject info = new JSONObject();
        try {
            info.put("bookID", BOOKID);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "JSON Parse error @ authenticate before request", Toast.LENGTH_SHORT).show();
        }


        RequestQueue rQueue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, info,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //try {
                            //Toast.makeText(BookDetailActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                        //} catch (JSONException e) {
                        //    e.printStackTrace();
                        //}


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("Error response", error.toString());
                    }
                }
        ) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + readTokenFile(getApplicationContext()));
                //headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                Log.d(TAG, "getHeaders: a" + headers.get("Accept"));
                Log.d(TAG, "getHeaders: " + headers.get("Authorization"));
                return headers;
            }

        };
        rQueue.add(postRequest);
        return 0;
    }

    private String readTokenFile(Context context) {
        String token = "";

        try {
            InputStream inputStream = context.openFileInput("userToken.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                token = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return token;
    }

    /**
     * Gets user data.
     *
     * @throws JSONException the json exception
     */



}
