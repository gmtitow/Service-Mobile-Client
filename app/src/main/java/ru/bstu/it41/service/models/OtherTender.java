package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 10.05.2018.
 */

public class OtherTender implements Serializable{
    @SerializedName("tender")
    @Expose
    private Tender tender;
    @SerializedName("tasks")
    @Expose
    private Tasks tasks;
    @SerializedName("userinfo")
    @Expose
    private Userinfo userinfo;

    @SerializedName("offer")
    @Expose
    private Offer offer;

   /* @SerializedName("offerExists")
    @Expose
    private byte offerExists;
    public byte getOfferExists() {
        return offerExists;
    }

    public void setOfferExists(byte offerExists) {
        this.offerExists = offerExists;
    }
    */

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
