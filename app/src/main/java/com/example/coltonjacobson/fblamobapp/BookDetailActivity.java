package com.example.coltonjacobson.fblamobapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coltonjacobson.fblamobapp.bookData.BooksCollection;

import org.w3c.dom.Text;

/**
 * Created by colto on 1/15/2018.
 */

public class BookDetailActivity extends AppCompatActivity {
    TextView bookNameText;
    TextView bookAuthorText;
    ImageView bookImageView;
    Button checkoutBtn;
    Button reserveBtn;
    int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookNameText = findViewById(R.id.textView_book_name);
        bookAuthorText = findViewById(R.id.textView_author);
        bookImageView = findViewById(R.id.imageView);
        checkoutBtn = findViewById(R.id.button_checkOut);
        reserveBtn = findViewById(R.id.button_reserve);



        //Obtains data
        Intent intent = this.getIntent();
        String bookName = intent.getExtras().getString("BOOK_NAME");
        String bookAuthor = intent.getExtras().getString("BOOK_AUTHOR");
        int imageID = intent.getExtras().getInt("BOOK_IMAGE");
        boolean isCheckedOut = true; //intent.getExtras().getBoolean("BOOK_CHECKEDOUT");
        boolean isReserved = true; //intent.getExtras().getBoolean("BOOK_RESERVED");
        position = intent.getExtras().getInt("POSITION");


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
            BooksCollection.getBooks().get(position).setCheckedOut(false);
        } else {
            button.setText("Check In");
            BooksCollection.getBooks().get(position).setCheckedOut(true);
        }

    }

    public void onReserveClicked (View view) {

        Button button = (Button) view;
        if (button.getText().equals("Cancel Reserve")) {
            button.setText("Reserve");
            BooksCollection.getBooks().get(position).setReserved(false); //These set functions don't appear to be storing any data yet
        } else {
            button.setText("Cancel Reserve");
            BooksCollection.getBooks().get(position).setReserved(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
