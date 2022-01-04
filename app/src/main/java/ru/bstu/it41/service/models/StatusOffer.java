package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StatusOffer implements Serializable{

    @SerializedName("offer")
    @Expose
    private Offer mOffer;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("error")
    @Expose
    private List<String> error = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public Offer getOffer() {
        return mOffer;
    }

    public void setOffer(Offer offer) {
        mOffer = offer;
    }
}