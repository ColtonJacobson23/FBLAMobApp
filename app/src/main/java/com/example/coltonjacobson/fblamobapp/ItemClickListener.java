package com.example.coltonjacobson.fblamobapp;

import android.view.View;

/**
 * Created by colto on 1/15/2018.
 */
public interface ItemClickListener {

    /**
     * On click.
     *
     * @param view        the view
     * @param position    the position
     * @param isLongClick the is long click
     */
    void onClick(View view, int position, boolean isLongClick);



}
