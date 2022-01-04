package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tender;

public class StatusTender implements Serializable{

    @SerializedName("auction")
    @Expose
    private Tender mTender;

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

    public Tender getTender() {
        return mTender;
    }

    public void setTender(Tender tender) {
        mTender = tender;
    }
}