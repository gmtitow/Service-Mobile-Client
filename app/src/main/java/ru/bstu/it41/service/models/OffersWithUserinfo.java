package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Герман on 07.05.2018.
 */

public class OffersWithUserinfo implements Serializable {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("offersWithUser")
    @Expose
    private List<OfferWithUser> mOfferWithUser = null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OfferWithUser> getOfferWithUser() {
        return mOfferWithUser;
    }

    public void setOfferWithUser(List<OfferWithUser> offerWithUser) {
        this.mOfferWithUser = offerWithUser;
    }
}
