package com.example.coltonjacobson.fblamobapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by 2149292 on 3/14/2018.
 */

public class AppViewModel extends ViewModel {
    private MutableLiveData<List<Book>> books;
    public LiveData<List<Book>> getUsers() {
        if (books == null) {
            books = new MutableLiveData<List<Book>>();
            loadUsers();
        }
        return books;
    }

    private void loadUsers() {
        // Do an asyncronous operation to fetch users.
    }
}

