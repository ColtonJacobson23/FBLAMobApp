package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */
public class AuthorConverter {

    /**
     * Stored string to list array list.
     *
     * @param s the s
     * @return the array list
     */
    @TypeConverter
    public ArrayList<String> storedStringToList(String s) {
        String[] strings = s.split(",");
        ArrayList<String> authors = new ArrayList<String>();
        for(int i = 0; i < strings.length; i++) {
            authors.add(strings[i]);
        }
        return authors;

    }

    /**
     * List to stored string string.
     *
     * @param authors the authors
     * @return the string
     */
    @TypeConverter
    public String listToStoredString(ArrayList<String> authors) {
        String string = "";
        for(String s:authors) {
            string += s + ",";
        }
        return string;
    }
}
