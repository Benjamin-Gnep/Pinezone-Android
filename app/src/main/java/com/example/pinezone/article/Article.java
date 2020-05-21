package com.example.pinezone.article;

import java.util.List;

public class Article{
    private String title;
    private int uid;
    private String uimg;
    private int aid;
    private int likenum;
    private List<ArticleImage> aimg;
    private String username;

    private String content;
    private String datetime;
    private int islike;
    private int isStar;
    private String describe;
    private int commentnum;
    private int starnum;

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


    public String getUimg() {
        return uimg;
    }

    public String getUsername() {
        return username;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public int getIslike() {
        return islike;
    }

    public int getIsStar() {
        return isStar;
    }

    public String getContent() {
        return content;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getStarnum() {
        return starnum;
    }

    public String getDescribe() {
        return describe;
    }
}
