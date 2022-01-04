package ru.bstu.it41.service.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Герман on 02.05.2018.
 */

@Table(name = "Reviews")
public class Reviews extends Model implements Serializable {
    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("idReview")
    @Expose
    private int idReview;

    @Column
    @SerializedName("textReview")
    @Expose
    private String textReview;

    @Column
    @SerializedName("reviewDate")
    @Expose
    private Date reviewDate;

    @Column
    @SerializedName("executor")
    @Expose
    private int executor;

    @Column
    @SerializedName("userId_object")
    @Expose
    private int userIdObject;

    @Column
    @SerializedName("userId_subject")
    @Expose
    private int userIdSubject;

    @Column
    @SerializedName("raiting")
    @Expose
    private int raiting;

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getExecutor() {
        return executor;
    }

    public void setExecutor(int executor) {
        this.executor = executor;
    }

    public int getUserIdObject() {
        return userIdObject;
    }

    public void setUserIdObject(int userIdObject) {
        this.userIdObject = userIdObject;
    }

    public int getUserIdSubject() {
        return userIdSubject;
    }

    public void setUserIdSubject(int userIdSubject) {
        this.userIdSubject = userIdSubject;
    }

    public int getRaiting() {
        return raiting;
    }

    public void setRaiting(int raiting) {
        this.raiting = raiting;
    }
}
