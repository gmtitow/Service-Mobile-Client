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

@Table(name = "User")
public class User extends Model implements Serializable {

    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("userId")
    @Expose
    private int userId;
    @Column
    @SerializedName("email")
    @Expose
    private String email;
    @Column
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
