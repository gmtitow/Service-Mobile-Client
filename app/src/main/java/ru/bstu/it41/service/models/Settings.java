package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 28.04.2018.
 */

@Table(name="Settings")
public class Settings extends Model implements Serializable{

    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("userId")
    @Expose
    private int userId;

    @Column
    @SerializedName("notificationEmail")
    @Expose
    private int notificationEmail;

    @Column
    @SerializedName("notificationSms")
    @Expose
    private int notificationSms;

    @Column
    @SerializedName("notificationPush")
    @Expose
    private int notificationPush;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(int notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public int getNotificationSms() {
        return notificationSms;
    }

    public void setNotificationSms(int notificationSms) {
        this.notificationSms = notificationSms;
    }

    public int getNotificationPush() {
        return notificationPush;
    }

    public void setNotificationPush(int notificationPush) {
        this.notificationPush = notificationPush;
    }

}
