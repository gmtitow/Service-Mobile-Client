package ru.bstu.it41.service.models;

import android.content.Context;
import android.content.res.Resources;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.profile.ProfileFragmentState;

/**
 * Created by Герман on 28.04.2018.
 */

@Table(name="Userinfo")
public class Userinfo extends Model implements Serializable {
    @Column(unique = true,onUniqueConflict = Column.ConflictAction.IGNORE)
    @SerializedName("userId")
    @Expose
    private int userId;
    @Column
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @Column
    @SerializedName("patronymic")
    @Expose
    private String patronymic;
    @Column
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @Column
    @SerializedName("birthday")
    @Expose
    private Date birthday;

    @Column
    @SerializedName("male")
    @Expose
    private int male;

    @Column
    @SerializedName("about")
    @Expose
    private String about;

    @Column
    @SerializedName("raitingExecutor")
    @Expose
    private float raitingExecutor;

    @Column
    @SerializedName("raitingClient")
    @Expose
    private float raitingClient;

    @Column
    @SerializedName("address")
    @Expose
    private String address;

    @Column
    @SerializedName("pathToPhoto")
    @Expose
    private String pathToPhoto;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Calendar getBirthday() {
        if(birthday!= null) {
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(birthday);
            return cal;
        }
       return null;
    }

    public void setBirthday(Date birthday) {
       /* if(birthday!=null) {
            Calendar cal = Calendar.getIntent(Locale.getDefault());
            cal.setTime(birthday);
            this.birthday = cal;
        }
        else
            this.birthday = null;*/
       this.birthday = birthday;
    }
    public void setBirthday(Calendar birthday) {
        this.birthday = birthday.getTime();
    }


    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public float getRaitingExecutor() {
        return raitingExecutor;
    }

    public void setRaitingExecutor(float raitingExecutor) {
        this.raitingExecutor = raitingExecutor;
    }

    public float getRaitingClient() {
        return raitingClient;
    }

    public void setRaitingClient(float raitingClient) {
        this.raitingClient = raitingClient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public void setPathToPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    public String getFIO(){
        return (getFirstname() == null ? "" : getFirstname()) + " " + (getLastname() == null ? "" : getLastname()) +
                " " + (getPatronymic() == null ? "" : getPatronymic());
    }

    public String getCountYear(Context mContext){
        //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(getBirthday() != null) {
            Resources res = mContext.getResources();
            Calendar today = Calendar.getInstance(Locale.getDefault());
            int count = today.get(Calendar.YEAR) - getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);
            return years;
        }
        else
            return "";
    }
}
