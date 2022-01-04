package ru.bstu.it41.service.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Герман on 25.04.2018.
 */

public class TaskAndTenderWithOffers implements Serializable {

    @SerializedName("offers")
    @Expose
    private List<OfferWithUser> offers = null;

    @SerializedName("auctions")
    @Expose
    private Tender tender;

    @SerializedName("tasks")
    @Expose
    private Tasks tasks;

    public TaskAndTenderWithOffers(){

    }

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

    public List<OfferWithUser> getOffers() {
        return offers;
    }
    public void setOffers(List<OfferWithUser> offers) {
        this.offers = offers;
    }
}
