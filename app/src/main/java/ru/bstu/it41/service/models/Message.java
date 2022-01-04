package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Герман on 26.05.2018.
 */

@Table(name = "Message")
public class Message extends Model implements Serializable{

    public final static SimpleDateFormat mFormatFromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("messageId")
    @Expose
    private int messageId;

    @Column
    @SerializedName("input")
    @Expose
    private int input;

    @Column
    @SerializedName("message")
    @Expose
    private String message;

    @Column
    @SerializedName("date")
    @Expose
    private String date;

    @Column
    @SerializedName("auctionId")
    @Expose
    private int auctionId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        try {
            return mFormatFromServer.parse(date);
        }
        catch(Exception e){
            return null;
        }
    }

    public void setDate(Date date) {
        this.date = mFormatFromServer.format(date);
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }
}
