package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Герман on 25.04.2018.
 */

@Table(name = "taskAndTender")
public class TaskAndTender implements Serializable {

    @Column
    @SerializedName("offersCount")
    @Expose
    private int offersCount;

    @Column
    @SerializedName("auctions")
    @Expose
    private Tender tender;

    @Column
    @SerializedName("tasks")
    @Expose
    private Tasks tasks;

    public TaskAndTender(){

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

    public int getOffersCount() {
        return offersCount;
    }

    public void setOffersCount(int offersCount) {
        this.offersCount = offersCount;
    }
}
