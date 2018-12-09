package com.bamboospear.daily;

import android.graphics.Bitmap;

public class MyData {

    public String title;
    public String content;
    Bitmap image;
    String weather;
    String date;

    public MyData(String title, String content, Bitmap image, String weather, String date) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.weather = weather;
        this.date = date;
    }
}
