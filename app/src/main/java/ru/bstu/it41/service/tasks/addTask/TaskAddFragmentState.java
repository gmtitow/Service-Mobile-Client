package ru.bstu.it41.service.tasks.addTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddFragmentState extends SerializableStateModel {
    //При добавлении и изменении
    private String mCurrentName;
    private String mCurrentDescription;
    private int mCurrentCategory;
    private Calendar mCurrentDeadline;
    private Integer mCurrentPrice;
    private Double mCurrentLatitude = null;
    private Double mCurrentLongitude = null;
    private String mCurrentAddress;

    public String getCurrentAddress() {
        return mCurrentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        mCurrentAddress = currentAddress;
    }

    public Double getCurrentLatitude() {
        return mCurrentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        mCurrentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return mCurrentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        mCurrentLongitude = currentLongitude;
    }

    public String getCurrentName() {
        return mCurrentName;
    }

    public void setCurrentName(String currentName) {
        mCurrentName = currentName;
    }

    public String getCurrentDescription() {
        return mCurrentDescription;
    }

    public void setCurrentDescription(String currentDescription) {
        mCurrentDescription = currentDescription;
    }

    public int getCurrentCategory() {
        return mCurrentCategory;
    }

    public void setCurrentCategory(int currentCategory) {
        mCurrentCategory = currentCategory;
    }

    public Calendar getCurrentDeadline() {
        return mCurrentDeadline;
    }

    public void setCurrentDeadline(Date currentDeadline) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDeadline);
        mCurrentDeadline = cal;
    }

    public void setCurrentDeadline(Calendar currentDeadline) {
        mCurrentDeadline = currentDeadline;
    }

    public int getCurrentPrice() {
        return mCurrentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        mCurrentPrice = currentPrice;
    }

    public boolean allowNext(){
        return (mCurrentPrice != null && mCurrentPrice != 0 && mCurrentDeadline != null && mCurrentName.length()>0
                && mCurrentCategory != 0);
    }

    private Calendar mCurrentDateEndTender;

    public Calendar getCurrentDateEndTender() {
        return mCurrentDateEndTender;
    }

    public void setCurrentDateEndTender(Calendar currentDateEndTender) {
        mCurrentDateEndTender = currentDateEndTender;
    }

    public boolean allowCreate(){
        return (mCurrentDateEndTender != null && mCurrentDateEndTender.getTime().getTime() < mCurrentDeadline.getTime().getTime());
    }

    private boolean mCreatingInProgress = false;

    public boolean isCreatingInProgress() {
        return mCreatingInProgress;
    }

    public void setCreatingInProgress(boolean creatingInProgress) {
        mCreatingInProgress = creatingInProgress;
    }
}
