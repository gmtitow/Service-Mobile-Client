package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Table(name="Tender")
public class Tender extends Model implements Serializable {

    //static public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    public static SimpleDateFormat mFormatFromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("auctionId")
    @Expose
    private int tenderId;
    @Column
    @SerializedName("taskId")
    @Expose
    private int taskId;
    @Column
    @SerializedName("dateStart")
    @Expose
    private String dateStart;
    @Column
    @SerializedName("dateEnd")
    @Expose
    private String dateEnd;

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Date getDateStart() {
        try {
            return mFormatFromServer.parse(dateStart);
        }
        catch(Exception e){
            return null;
        }
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = mFormatFromServer.format(dateStart);
    }

    public Date getDateEnd() {
        try {
            return mFormatFromServer.parse(dateEnd);
        }
        catch(Exception e){
            return null;
        }
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = mFormatFromServer.format(dateEnd);
    }

}
