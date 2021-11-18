package org.ewha5.clorapp;

public class Clor {
    int _id;
    String category;
    String comb;
    String mood;
    String picture;

    public Clor(int _id, String category, String comb, String mood, String picture) {
        this._id = _id;
        this.category = category;
        this.comb = comb;
        this.mood = mood;
        this.picture = picture;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComb() {
        return comb;
    }

    public void setComb(String comb) {
        this.comb = comb;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
