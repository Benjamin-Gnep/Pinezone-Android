package com.example.pinezone.squirrel;

public class ThingsItem {
    private String title;
    private String subTitle;
    private int imageId;
    private int value;
    private int mode;
    private double attr;

    ThingsItem(String title,String subTitle,int imageId,int value,int mode,double attr){
        this.mode = mode;
        this.title = title;
        this.subTitle = subTitle;
        this.imageId = imageId;
        this.value = value;
        this.attr = attr;
    }

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getValue() {
        return value;
    }

    public int getMode() {
        return mode;
    }

    public double getAttr() {
        return attr;
    }
}
