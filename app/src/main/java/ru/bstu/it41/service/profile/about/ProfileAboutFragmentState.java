package ru.bstu.it41.service.profile.about;

import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileAboutFragmentState extends SerializableStateModel {

    //Это все только для организации интерфейса
    private boolean mShowProgress;
    private boolean mMayBeChanged;

    //Показ профиля
    String mAbout;

    //Редактирование профиля
    String mTemporaryAbout;

    public boolean isMayBeChanged() {
        return mMayBeChanged;
    }

    public void setMayBeChanged(boolean mayBeChanged) {
        mMayBeChanged = mayBeChanged;
    }

    public boolean isShowProgress() {
        return mShowProgress;
    }

    public void setShowProgress(boolean showProgress) {
        mShowProgress = showProgress;
    }

    public String getAbout() {
        return mAbout;
    }

    public void setAbout(String about) {
        mAbout = about;
    }

    public String getTemporaryAbout() {
        return mTemporaryAbout;
    }

    public void setTemporaryAbout(String temporaryAbout) {
        mTemporaryAbout = temporaryAbout;
    }

    //
    private boolean mSaveButtonEnabled = true;

    public boolean isSaveButtonEnabled() {
        return mSaveButtonEnabled;
    }

    public void setSaveButtonEnabled(boolean saveButtonEnabled) {
        mSaveButtonEnabled = saveButtonEnabled;
    }
}
