package com.example.pinezone.article;

import java.util.List;

public class Article{
    private String title;
    private int uid;
    private String uimg;
    private int aid;
    private int likenum;
    private List<ArticleImage> aimg;
    private String date;
    private String username;

    public static class ArticleImage{
        public int id;
        public int aid;
        public String path;
    }

    public String getTitle() {
        return title;
    }

    public int getAid() {
        return aid;
    }

    public int getLikenum() {
        return likenum;
    }

    public int getUid() {
        return uid;
    }

    public List<ArticleImage> getAimg() {
        return aimg;
    }

    public String getDate() {
        return date;
    }

    public String getUimg() {
        return uimg;
    }

    public String getUsername() {
        return username;
    }
}
