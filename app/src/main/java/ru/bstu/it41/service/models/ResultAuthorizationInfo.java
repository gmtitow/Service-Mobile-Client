package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 12.05.2018.
 */

public class ResultAuthorizationInfo implements Serializable{
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("allForUser")
    @Expose
    private AllForUser mAllForUser;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AllForUser getAllForUser() {
        return mAllForUser;
    }

    public void setAllForUser(AllForUser allForUser) {
        mAllForUser = allForUser;
    }
}
