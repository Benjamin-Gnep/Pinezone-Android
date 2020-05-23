package com.example.pinezone.comment;

public class Comment {
    long comid;
    int uid;
    String uimg;
    String username;
    String content;
    String datetime;
//    int replycommid;

    public int getUid() {
        return uid;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getUimg() {
        return uimg;
    }

    public long getComid() {
        return comid;
    }

    public void setComid(long comid) {
        this.comid = comid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
