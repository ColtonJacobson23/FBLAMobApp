package com.example.coltonjacobson.fblamobapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static android.content.ContentValues.TAG;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;

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
    String imagePath;

    String description;

    TextView descriptionText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

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


        //Obtains data
        Intent intent = this.getIntent();
        String bookName = intent.getExtras().getString("BOOK_NAME");
        String bookAuthor = intent.getExtras().getString("BOOK_AUTHOR");
        int imageID = intent.getExtras().getInt("BOOK_IMAGE");
        boolean isCheckedOut = intent.getExtras().getBoolean("BOOK_CHECKEDOUT");
        boolean isReserved = intent.getExtras().getBoolean("BOOK_RESERVED");
        position = intent.getExtras().getInt("POSITION");
        imagePath = intent.getExtras().getString("BOOK_IMAGEPATH");
        description = intent.getExtras().getString("BOOK_DESCRIPTION");








        Glide.with(this).load("https://fblamobileapp.azurewebsites.net/images/" + imagePath).into(bookImageView);




        //Binds data
        bookNameText.setText(bookName);
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
        if (button.getText().equals("Check In")) {
            button.setText("Check Out");
        } else {
            button.setText("Check In");
        }

    }

    /**
     * On reserve clicked.
     *
     * @param view the view
     */
    public void onReserveClicked(View view) {

        Button button = (Button) view;
        if (button.getText().equals("Cancel Reserve")) {
            button.setText("Reserve");
        } else {
            button.setText("Cancel Reserve");
            //BooksCollection.getBooks().get(position).setReserved(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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


}
