package ru.bstu.it41.service.profile.change;

import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileChangeFragmentState extends SerializableStateModel {

    //Это все только для организации интерфейса
    private boolean mShowProgress;
    private boolean mDownloaded;
    private boolean mToViewPofile = false;
    private boolean mDownloadPhoto = true;

    public boolean isDownloadPhoto() {
        return mDownloadPhoto;
    }

    public void setDownloadPhoto(boolean downloadPhoto) {
        mDownloadPhoto = downloadPhoto;
    }

    public boolean isToViewPofile() {
        return mToViewPofile;
    }

    public void setToViewPofile(boolean toViewPofile) {
        mToViewPofile = toViewPofile;
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


    //Редактирование профиля
    private Userinfo mTemporaryUserInfo = new Userinfo();

    public Userinfo getTemporaryUserInfo() {
        return mTemporaryUserInfo;
    }

    public void setTemporaryUserInfo(Userinfo temporaryUserInfo) {
        mTemporaryUserInfo = temporaryUserInfo;
    }
    //
    private boolean mSaveButtonEnabled = true;

    public boolean isSaveButtonEnabled() {
        return mSaveButtonEnabled && ((mTemporaryUserInfo == null || !mTemporaryUserInfo.getFirstname().equals("")) &&
                !mTemporaryUserInfo.getLastname().equals(""));
    }

    public void setSaveButtonEnabled(boolean saveButtonEnabled) {
        mSaveButtonEnabled = saveButtonEnabled;
    }

    public String getFileName() {
        return "IMG_UserPhoto.jpg";
    }
}
