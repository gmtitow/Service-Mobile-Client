package ru.bstu.it41.service.profile.other_profile;

import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileOtherFragmentState extends SerializableStateModel {
    //Показ профиля
    private Userinfo mUserInfo = new Userinfo();

    public Userinfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(Userinfo userInfo) {
        mUserInfo = userInfo;
    }
}
