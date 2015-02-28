package com.example.banana.sqlitelearningproject.model;


public class Item {

    private int id;
    private String title;
    private String frontSide;
    private String backSide;
    private int bookMark;

    public Item() {
    }

    public Item(String title, String frontSide, String backSide, int bookMark) {
        super();
        this.title = title;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.bookMark = bookMark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public int getBookMark() {
        return bookMark;
    }

    public void setBookMark(int bookMark) {
        this.bookMark = bookMark;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", frontSide=" + frontSide + ", backSide=" + backSide + "]";
    }
}
