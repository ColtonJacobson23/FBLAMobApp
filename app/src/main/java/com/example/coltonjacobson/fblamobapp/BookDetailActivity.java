package com.example.coltonjacobson.fblamobapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by colto on 1/15/2018.
 */

public class BookDetailActivity extends AppCompatActivity {
    TextView bookNameText;
    TextView bookAuthorText;
    ImageView bookImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookNameText = findViewById(R.id.textView_book_name);
        bookAuthorText = findViewById(R.id.textView_author);
        bookImageView = findViewById(R.id.image_view);


        //Obtains data
        Intent intent = this.getIntent();
        String bookName = intent.getExtras().getString("BOOK_NAME");
        String bookAuthor = intent.getExtras().getString("BOOK_AUTHOR");
        int bookImage = intent.getExtras().getInt("BOOK_IMAGE");

        //Binds data
        bookNameText.setText(bookName);
        bookAuthorText.setText(bookAuthor);
        // THIS LINE BELOWWWWWW
        //
        //
        bookImageView.setImageResource(bookImage);
        // Attempt to invoke virtual method 'void android.widget.ImageView.setImageResource(int)' on a null object reference
        // at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3319)
        //
        //

}
