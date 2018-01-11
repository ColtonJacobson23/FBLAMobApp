package com.example.coltonjacobson.fblamobapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coltonjacobson.fblamobapp.LibraryData;
import com.example.coltonjacobson.fblamobapp.R;

/**
 * Created by Bill Jacobson on 1/10/2018.
 */

public class ListAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list,parent,false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ListViewHolder)holder).BindView(position);

    }

    @Override
    public int getItemCount() {
        return LibraryData.bookTitles.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBookName;
        private ImageView mBookImage;

        public ListViewHolder(View bookView) {

            super(bookView);
            mBookName = (TextView) bookView.findViewById(R.id.BookName);
            mBookImage = (ImageView) bookView.findViewById(R.id.bookImage);
            bookView.setOnClickListener(this);


        }

        public void BindView(int position) {

            mBookName.setText(LibraryData.bookTitles[position]);
            mBookImage.setImageResource(LibraryData.bookPicture[position]);

        }

        public void onClick(View view) {

        }


    }

}