package ru.bstu.it41.service.profile;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.io.File;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileFragmentState extends SerializableStateModel {

    //Это все только для организации интерфейса
    private boolean mShowProgress;
    private boolean mProgressChange;
    private boolean mDownloaded;
    private boolean mEnableDownload = true;

    public boolean isEnableDownload() {
        return mEnableDownload;
    }

    public void setEnableDownload(boolean enableDownload) {
        mEnableDownload = enableDownload;
    }


    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        mDownloaded = downloaded;
    }

    public boolean isShowProgress() {
        return mShowProgress;
    }

    public void setShowProgress(boolean showProgress) {
        mShowProgress = showProgress;
    }

    public boolean isProgressChange() {
        return mProgressChange;
    }

    public void setProgressChange(boolean progressChange) {
        mProgressChange = progressChange;
    }

    private AllForUser mAllForUser;

    public AllForUser getAllForUser() {
        return mAllForUser;
    }

    public void setAllForUser(AllForUser allForUser) {
        mAllForUser = allForUser;
    }

    public Settings getSettings() {
        return mAllForUser.getSettings();
    }

    public void setSettings(Settings settings) {
        mAllForUser.setSettings(settings);
    }

    public User getUser() {
        return mAllForUser.getUser();
    }

    public void setUser(User user) {
        mAllForUser.setUser(user);
    }

    public void setUserinfo(Userinfo user) {
        mAllForUser.setUserinfo(user);
    }

    public Userinfo getUserinfo() {
        return mAllForUser.getUserinfo();
    }

    private Settings mTempSettings;

    public Settings getTempSettings() {
        return mTempSettings;
    }

    public void setTempSettings(Settings tempSettings) {
        mTempSettings = tempSettings;
    }

    public boolean isSettingsChanged(){
        return getSettings().getNotificationPush()!= mTempSettings.getNotificationPush() ||
                getSettings().getNotificationEmail()!= mTempSettings.getNotificationEmail() ||
                getSettings().getNotificationSms()!= mTempSettings.getNotificationSms();
    }

    public void copySettings(Settings destSettings, Settings sourceSettings){
        destSettings.setNotificationPush(sourceSettings.getNotificationPush());
        destSettings.setNotificationSms(sourceSettings.getNotificationSms());
        destSettings.setNotificationEmail(sourceSettings.getNotificationEmail());
    }

    //Этого тут быть не должно
    //Places
    private List<Place> mPlaces;

    public List<Place> getPlaces() {
        return mPlaces;
    }

    public void setPlaces(List<Place> places) {
        mPlaces = places;
    }

    //Для фотографии профиля
    private boolean mPhotoChanged;

    public boolean isPhotoChanged() {
        return mPhotoChanged;
    }

    public void setPhotoChanged(boolean photoChanged) {
        mPhotoChanged = photoChanged;
    }

    public String getFileName() {
        return "IMG_UserPhoto.jpg";
    }

    public String getTemporaryPhotoFileName(){
        return "IMG_TempUserPhoto.jpg";
    }
}
