package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 07.05.2018.
 */

public class OfferWithUser implements Serializable {
    @SerializedName("Offer")
    @Expose
    private Offer offer;
    @SerializedName("Userinfo")
    @Expose
    private Userinfo userinfo;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

}
