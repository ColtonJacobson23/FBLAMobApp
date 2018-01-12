package com.example.coltonjacobson.fblamobapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colto on 1/12/2018.
 */

public class RecyclerViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment,container,false);

        List<String> bookNamesList = new ArrayList<>();
        for (int i = 0; i < LibraryData.bookTitles.length;i++) {
            bookNamesList.add(LibraryData.bookTitles[i]);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(bookNamesList));


        return view;

    }

    public static Fragment newInstance() {

        return new RecyclerViewFragment();

    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardview;
        private TextView mTextView;
        private ImageView mImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater layoutInflater, ViewGroup container) {

            super(layoutInflater.inflate(R.layout.card_view,container,false));

            mCardview = itemView.findViewById(R.id.card_view);
            mTextView = itemView.findViewById(R.id.text_view);


        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        private List<String> mBookNameList;


        public RecyclerViewAdapter(List<String> list) {

            this.mBookNameList = list;

        }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {

            holder.mTextView.setText(mBookNameList.get(position));



        }

        @Override
        public int getItemCount() {
            //Five or more
            return mBookNameList.size();
        }
    }
}
