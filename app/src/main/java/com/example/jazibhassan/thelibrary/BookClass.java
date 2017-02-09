package com.example.jazibhassan.thelibrary;

/**
 * Created by Jazib Hassan on 28-Oct-15.
 */
public class BookClass {

    public String title;
    public String author;
    public String edition;
    public String id;
    public String desc;
    public String location;
    public String status;

    public BookClass(String title,String author,String edition, String desc, String location,String status){
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.desc = desc;
        this.location = location;
        this.status = status;
    }

}
