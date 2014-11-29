package com.db.javaschool.tdd.multithreading;

/**
 *
 */
public class Article {
    private volatile boolean isLocked = false;
    private volatile int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isModerated() {
        return isLocked;
    }

    public void setModerated() {
        isLocked = true;
    }
}