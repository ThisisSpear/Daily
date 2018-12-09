package com.bamboospear.daily;

public class DailyItem {
    public String id;
    public String content;
    public String details;

    public DailyItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}