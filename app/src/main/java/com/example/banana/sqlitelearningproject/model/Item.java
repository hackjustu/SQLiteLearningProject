package com.example.banana.sqlitelearningproject.model;


public class Item {

    private int id;
    private String title;
    private String front;
    private String back;

    public Item() {
    }

    public Item(String title, String front, String back) {
        super();
        this.title = title;
        this.front = front;
        this.back = back;
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

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", front=" + front + ", back=" + back + "]";
    }
}
