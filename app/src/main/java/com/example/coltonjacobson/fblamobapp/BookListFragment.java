package com.example.coltonjacobson.fblamobapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by colto on 1/12/2018.
 */
public abstract class BookListFragment extends AppCompatActivity {
    /**
     * Create fragment fragment.
     *
     * @return the fragment
     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.recyclerViewContainer);

        if(fragment == null) {

            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recyclerViewContainer,fragment)
                    .commit();



        }

    }
}
