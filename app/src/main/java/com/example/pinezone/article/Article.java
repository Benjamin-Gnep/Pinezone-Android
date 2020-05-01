package com.example.pinezone.article;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Article implements Serializable {
    private String title;
    private int[] imageGroup;
    private int image;

    public Article(String title,int imageId) {
        this.title = title;
        this.image = imageId;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public int[] getImageGroup() {
        return imageGroup;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setImageGroup(int[] imageGroup) {
        this.imageGroup = imageGroup;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
