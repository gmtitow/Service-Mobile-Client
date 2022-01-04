package ru.bstu.it41.service.profile.settings;

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

public class SettingsFragmentState extends SerializableStateModel {

    //Это все только для организации интерфейса

    private boolean mProgressChange;

    public boolean isProgressChange() {
        return mProgressChange;
    }

    public void setProgressChange(boolean progressChange) {
        mProgressChange = progressChange;
    }

    private Settings mSettings;

    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Settings getSettings() {
        return mSettings;
    }

    public void setSettings(Settings settings) {
        mSettings = settings;
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
}
