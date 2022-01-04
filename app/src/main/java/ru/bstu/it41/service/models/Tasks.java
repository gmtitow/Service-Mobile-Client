package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Герман on 02.02.2018.
 */

@Table(name = "Tasks")
public class Tasks extends Model implements Serializable {

    //public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    private final  SimpleDateFormat mFormatFromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("taskId")
    @Expose
    private int taskId;
    @Column
    @SerializedName("userId")
    @Expose
    private int userId;
    @Column
    @SerializedName("categoryId")
    @Expose
    private int categoryId;
    @Column
    @SerializedName("name")
    @Expose
    private String name;
    @Column
    @SerializedName("description")
    @Expose
    private String description;
    @Column
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @Column
    @SerializedName("price")
    @Expose
    private int price;
    @Column
    @SerializedName("status")
    @Expose
    private String status;
    @Column
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @Column
    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @Column
    @SerializedName("address")
    @Expose
    private String address;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int  taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        try {
            return mFormatFromServer.parse(deadline);
        }
        catch(Exception e){
            return null;
        }
    }

    public void setDeadline(Date deadline) {
        this.deadline = mFormatFromServer.format(deadline);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address==null?"":address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public class Status{
        public static final String STATUS_SEARCH = "Поиск";
        public static final String STATUS_EXECUTING = "Выполняется";
        public static final String STATUS_EXECUTED = "Завершено";
        public static final String STATUS_NONEXECUTED = "Не выполнено";
    }
}
