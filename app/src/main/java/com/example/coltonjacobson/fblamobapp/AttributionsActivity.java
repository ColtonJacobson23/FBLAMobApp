package com.example.coltonjacobson.fblamobapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AttributionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBackClicked() {

        onBackPressed();
    }
}
