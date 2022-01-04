package ru.bstu.it41.service.models;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 24.05.2018.
 */

public class OfferWithTask extends BaseObservable implements Serializable {
    @SerializedName("Offer")
    @Expose
    private Offer offer;
    @SerializedName("Tasks")
    @Expose
    private Tasks tasks;
    @SerializedName("Userinfo")
    @Expose
    private Userinfo userinfo;

    @SerializedName("Tender")
    @Expose
    private Tender tender;

    @Bindable
    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Bindable
    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    @Bindable
    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    @Bindable
    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }
}
