package com.example.jazibhassan.thelibrary;

/**
 * Created by Jazib Hassan on 31-Oct-15.
 */
public class HistoryClass {
    public String title;
    public String author;
    public String issuedate;
    public String duedate;
    public String returndate;
    public String edition;
    public HistoryClass(String title, String author, String issuedate, String duedate, String returndate){
        this.title = title;
        this.author= author;
        this.issuedate=issuedate;
        this.duedate=duedate;
        this.returndate= returndate;
    }

    public HistoryClass(String title, String author, String issuedate, String duedate, String returndate, String edition){
        this.title = title;
        this.author= author;
        this.issuedate=issuedate;
        this.duedate=duedate;
        this.returndate= returndate;
        this.edition = edition;
    }
}
