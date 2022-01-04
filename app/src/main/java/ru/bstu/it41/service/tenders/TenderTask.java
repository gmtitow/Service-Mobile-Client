package ru.bstu.it41.service.tenders;

/**
 * Created by Герман on 16.11.2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;

public class TenderTask {

    @SerializedName("auctions")
    @Expose
    private Tender auction;
    @SerializedName("tasks")
    @Expose
    private Tasks task;

    public Tender getAuctions() {
        return auction;
    }

    public void setAuctions(Tender auctions) {
        this.auction = auctions;
    }

    public Tasks getTasks() {
        return task;
    }

    public void setTasks(Tasks tasks) {
        this.task = tasks;
    }

}

