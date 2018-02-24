package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */

public class AuthorConverter {

    @TypeConverter
    public ArrayList<String> storedStringToList(String s) {
        String[] strings = s.split(",");
        ArrayList<String> authors = new ArrayList<String>();
        for(int i = 0; i < strings.length; i++) {
            authors.add(strings[i]);
        }
        return authors;

    }

    @TypeConverter
    public String listToStoredString(ArrayList<String> authors) {
        String string = "";
        for(String s:authors) {
            string += s + ",";
        }
        return string;
    }
}
