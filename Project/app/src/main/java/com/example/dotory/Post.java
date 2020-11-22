package com.example.dotory;

public class Post {

    private String title;
    private String date;
    private String time;
    private String content;
    private boolean visible;

    public Post(){}

    public Post(String title, String content, String date, String time, boolean visible){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.visible = visible;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


}
