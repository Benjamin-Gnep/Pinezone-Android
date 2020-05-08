package com.example.pinezone.user;

import android.util.Log;

public class User {

    private static final String TAG = "User";
    
    private int id;
    private int level;
    private String name;
    private int sex;
    private String location;
    private String profile;
    private int followsNum;
    private int fansNum;
    private int articleNum;
    private int pid;

    public int getId() {
        return id;
    }

    public int getArticleNum() {
        return articleNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public int getFollowsNum() {
        return followsNum;
    }

    public int getLevel() {
        return level;
    }

    public String getLocation() {
        return location;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public int getSex() {
        return sex;
    }

    public void setArticleNum(int articleNum) {
        this.articleNum = articleNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public void setFollowsNum(int followsNum) {
        this.followsNum = followsNum;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
    
    public String userShow(){
        Log.e(TAG, "User: " + id + name + sex + level + profile + pid );
        return "";
    }
}
