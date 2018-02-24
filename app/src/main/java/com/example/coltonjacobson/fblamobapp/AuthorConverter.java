package com.example.coltonjacobson.fblamobapp;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 2149292 on 2/24/2018.
 */

public class AuthorConverter {

    @TypeConverter
    public List<String> storedStringToList(String s) {

        List<String> authors = Arrays.asList(s.split(","));
        return authors;

    }

    @TypeConverter
    public String listToStoredString(List<String> authors) {
        String string = "";
        for(String s:authors) {
            string += s + ",";
        }
        return string;
    }
}
