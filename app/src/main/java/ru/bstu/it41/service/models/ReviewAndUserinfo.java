package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 02.05.2018.
 */

public class ReviewAndUserinfo implements Serializable{
    @Expose
    @SerializedName("reviews")
    private Reviews reviews;
    @SerializedName("userinfo")
    @Expose
    private Userinfo userinfo;

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }
}
