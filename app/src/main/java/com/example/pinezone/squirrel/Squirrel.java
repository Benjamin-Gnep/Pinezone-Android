package com.example.pinezone.squirrel;

import android.util.Log;

public class Squirrel {
    private int id;
    private int uid;
    private String name;
    private int hp;
    private int pinecone;
    private float food;
    private float achievement;
    private float companion;
    private int state;

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getAchievement() {
        return achievement;
    }

    public int getHp() {
        return hp;
    }

    public float getFood() {
        return food;
    }

    public float getCompanion() {
        return companion;
    }

    public int getPinecone() {
        return pinecone;
    }

    public void show(){
        Log.e("TAG", id + " " + name + " " + hp + " "  + pinecone + " " +
                food + " " + achievement + " " + companion );
    }
}
