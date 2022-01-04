package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Герман on 07.05.2018.
 */

@Table(name = "Offer")
public class Offer extends Model implements Serializable {

    public SimpleDateFormat mFormatFromServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("offerId")
    @Expose
    private int offerId;

    @Column
    @SerializedName("userId")
    @Expose
    private int userId;

    @Column
    @SerializedName("auctionId")
    @Expose
    private int tenderId;

    @Column
    @SerializedName("deadline")
    @Expose
    private String deadline;

    @Column
    @SerializedName("description")
    @Expose
    private String description;

    @Column
    @SerializedName("price")
    @Expose
    private int price;

    @Column
    @SerializedName("selected")
    @Expose
    private int selected;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
    }

    public Date getDeadline() {
        try {
            return mFormatFromServer.parse(deadline);
        }
        catch(Exception e){
            return null;
        }
    }

    public Calendar getCalendarDeadline() {
        try {
            Date date = mFormatFromServer.parse(deadline);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        catch(Exception e){
            return null;
        }
    }

    public void setDeadline(Date deadline) {
        this.deadline = mFormatFromServer.format(deadline);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public class Status{
        public static final String STATUS_WAITING = "Ожидание";
        public static final String STATUS_SELECTED = "Выбрано";
        public static final String STATUS_NONSELECTED = "Не выбрано";
        public static final String STATUS_COMPLETE = "Выполнено";
        public static final String STATUS_FINISHED = "Завершено";
    }
}
