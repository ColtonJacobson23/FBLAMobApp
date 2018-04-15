package com.example.coltonjacobson.fblamobapp.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
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
public class Checkout{

    @PrimaryKey(autoGenerate = false)
    public int cID;

    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "bookID")
    private int bookID;

    @ColumnInfo(name  = "checkoutDate")
    private Date checkoutDate;

    @ColumnInfo(name = "dueDate")
    private Date dueDate;

    @ColumnInfo(name = "renewals")
    private int renewals;


    public Checkout(int cID, int userID, int bookID, Date checkoutDate, Date dueDate, int renewals) {
        this.cID = cID;
        this.userID = userID;
        this.bookID = bookID;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.renewals = renewals;

    }


    public int getcID() {
        return cID;
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

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getRenewals() {
        return renewals;
    }

    public void setRenewals(int renewals) {
        this.renewals = renewals;
    }

    public static ArrayList<Checkout> makeCheckoutList(JSONArray jArray) throws JSONException, ParseException {

        ArrayList<Checkout> arrayList = new ArrayList<Checkout>();

        if (jArray.equals(new JSONArray()) || jArray.equals(null)) {
            Log.d(TAG, "makeTransactionList: new array list");

            return new ArrayList<Checkout>();
        }

        for (int i = 0; i < jArray.length();i++)  {
            arrayList.add(makeCheckout(jArray.getJSONObject(i)));
            Log.d(TAG, "makeTransactionList: formatting array list" + i);
        }

        return arrayList;
    }

    public static Checkout makeCheckout(JSONObject jsonObject) throws JSONException, ParseException {
        Checkout checkout = new Checkout(jsonObject.getInt("id"),
                jsonObject.getInt("userID"),
                jsonObject.getInt("bookID"),
                storedStringToDate(jsonObject.getString("checkoutDate")),
                storedStringToDate(jsonObject.getString("dueDate")),
                jsonObject.getInt("renewals"));
        return checkout;
    }

    private static Date storedStringToDate(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date = new Date();
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



}
