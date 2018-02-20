//package com.example.coltonjacobson.fblamobapp.bookData;
//
//import com.example.coltonjacobson.fblamobapp.R;
//
//import java.util.ArrayList;
//
///**
// * Created by colto on 1/15/2018.
// */
//
//public class BooksCollection {
//
//
//    public static ArrayList<Book> getBooks() {
//        ArrayList<Book> books = new ArrayList<Book>();
//        Book book = null;
//        String url = "http://lizardswimmer.azurewebsites.net/simple/books";
//
//
//
//        //Harry Potter
//        book = new Book();
//        book.setAuthor("J.K Rowling");
//        book.setName("Harry Potter and the Prisoner of Askaban");
//        book.setImage(R.drawable.mockingjay_image);
//        book.setCheckedOut(true);
//        book.setReserved(false);
//        books.add(book);
//
//        //Christine
//        book = new Book();
//        book.setAuthor("Stephen King");
//        book.setName("Christine");
//        book.setImage(R.drawable.mockingjay_image);
//        book.setCheckedOut(false);
//        book.setReserved(true);
//        books.add(book);
//
//        //To Kill A Mockingbird
//        book = new Book();
//        book.setAuthor("Harper Lee");
//        book.setName("To Kill a Mockingbird");
//        book.setImage(R.drawable.mockingjay_image);
//        book.setCheckedOut(false);
//        book.setReserved(false);
//        books.add(book);
//
//        //Just a Little Christmas
//        book = new Book();
//        book.setAuthor("Janet Dailey");
//        book.setName("Just a Little Christmas");
//        book.setImage(R.drawable.mockingjay_image);
//        book.setCheckedOut(true);
//        book.setReserved(false);
//        books.add(book);
//
//        //Mockingjay
//        book = new Book();
//        book.setAuthor("Suzanne Collins");
//        book.setName("Mockingjay");
//        book.setImage(R.drawable.mockingjay_image);
//        book.setCheckedOut(false);
//        book.setReserved(true);
//        books.add(book);
//
//
//
//
//        return books;
//
//    }
//}
