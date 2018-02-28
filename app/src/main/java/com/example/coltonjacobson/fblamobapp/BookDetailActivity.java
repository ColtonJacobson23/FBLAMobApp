package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomMasterTable;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.coltonjacobson.fblamobapp.bookData.BooksCollection;

import org.w3c.dom.Text;
import static android.content.ContentValues.TAG;
import static android.graphics.Bitmap.CompressFormat.PNG;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by colto on 1/15/2018.
 */

public class BookDetailActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener {
    FrameLayout mapContainer;
    TextView bookNameText;
    TextView bookAuthorText;
    ImageView bookImageView;
    Button checkoutBtn;
    Button reserveBtn;
    int position;
    AppDatabase database;
    ArrayList<Book> books;
    String base64String;
    Bitmap coverImageBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookNameText = findViewById(R.id.textView_book_name);
        bookAuthorText = findViewById(R.id.textView_author);
        bookImageView = (ImageView)findViewById(R.id.imageView);
        checkoutBtn = findViewById(R.id.button_checkOut);
        reserveBtn = findViewById(R.id.button_reserve);
        mapContainer = findViewById(R.id.map_container);

        MapFragment fragment = new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_container,fragment,"FragmentName");
        fragmentTransaction.commit();








        //Obtains data
        Intent intent = this.getIntent();
        String bookName = intent.getExtras().getString("BOOK_NAME");
        String bookAuthor = intent.getExtras().getString("BOOK_AUTHOR");
        int imageID = intent.getExtras().getInt("BOOK_IMAGE");
        boolean isCheckedOut = intent.getExtras().getBoolean("BOOK_CHECKEDOUT");
        boolean isReserved = intent.getExtras().getBoolean("BOOK_RESERVED");
        position = intent.getExtras().getInt("POSITION");

        base64String = readBase64File(getApplicationContext());
        coverImageBitmap = Book.toBitmap(getApplicationContext(),base64String);
        Toast.makeText(this, "About to set bitmap", Toast.LENGTH_SHORT).show();
        
        bookImageView.setImageResource();

        //Binds data
        bookNameText.setText(bookName);
        bookAuthorText.setText(bookAuthor);
        bookImageView.setImageResource(imageID);

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
    public void onCheckoutClicked (View view) {

        Button button = (Button) view;
        if (button.getText().equals("Check In")) {
            button.setText("Check Out");
        } else {
            button.setText("Check In");
        }

    }

    public void onReserveClicked (View view) {

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

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                base64String = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return base64String;
    }
}
