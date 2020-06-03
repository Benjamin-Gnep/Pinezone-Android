package com.example.pinezone.ui.home;

public class ThingsItem {
    private String title;
    private String subTitle;
    private int imageId;

    ThingsItem(String title,String subTitle,int imageId){
        this.title = title;
        this.subTitle = subTitle;
        this.imageId = imageId;
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

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
