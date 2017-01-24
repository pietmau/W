package com.waracle.androidtest.fragment.pojo;

public class Cake {
    private final String title;
    private final String desc;
    private final String image;

    public Cake(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

}