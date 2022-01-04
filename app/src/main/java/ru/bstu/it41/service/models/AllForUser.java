package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 28.04.2018.
 */

public class AllForUser implements Serializable {
    @SerializedName("userinfo")
    @Expose
    private Userinfo userinfo;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("settings")
    @Expose
    private Settings settings;

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
