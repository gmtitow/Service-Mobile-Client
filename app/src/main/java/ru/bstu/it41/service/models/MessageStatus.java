package ru.bstu.it41.service.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Герман on 26.05.2018.
 */

public class MessageStatus {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    @SerializedName("reviewWritten")
    @Expose
    private int ReviewWritten;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getReviewWritten() {
        return ReviewWritten;
    }

    public void setReviewWritten(int reviewWritten) {
        ReviewWritten = reviewWritten;
    }
}
