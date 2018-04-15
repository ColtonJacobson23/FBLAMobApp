package com.example.coltonjacobson.fblamobapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

@Entity
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    public int rID;

    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "bookID")
    private int bookID;

    @ColumnInfo(name = "dateTime")
    private Date dateTime;



    public Reservation( int userID, int bookID, Date dateTime) {
        this.bookID = bookID;
        this.userID = userID;
        this.dateTime = dateTime;

    }

    public int getrID() {
        return rID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

//    public static ArrayList<Reservation> makeReservationList(JSONArray jArray) throws JSONException, ParseException {
//
//        ArrayList<Reservation> arrayList = new ArrayList<Reservation>();
//
//        if (jArray.equals(new JSONArray()) || jArray.equals(null)) {
//            Log.d(TAG, "makeTransactionList: new array list");
//
//            return new ArrayList<Reservation>();
//        }
//
//        for (int i = 0; i < jArray.length();i++)  {
//            arrayList.add(makeReservation(jArray.getJSONObject(i)));
//            Log.d(TAG, "makeTransactionList: formatting array list" + i);
//        }
//
//        return arrayList;
//    }
//
//    public static Reservation makeReservation(JSONObject jsonObject) throws JSONException, ParseException {
//        Reservation reservation = new Reservation(
//                jsonObject.getInt("id"),
//                jsonObject.getInt("userID"),
//                jsonObject.getInt("bookID"),
//                storedStringToDate(jsonObject.getString("datetime")));
//        return reservation;
//    }
//
//    private static Date storedStringToDate(String s) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        Date date = null;
//        try {
//            date = dateFormat.parse(s);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }

}
