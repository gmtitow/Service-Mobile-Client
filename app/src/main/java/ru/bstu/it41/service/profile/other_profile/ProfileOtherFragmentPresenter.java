package ru.bstu.it41.service.profile.other_profile;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileOtherFragmentPresenter extends ReampPresenter<ProfileOtherFragmentState> implements Serializable {
    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //getStateModel().setCurrentFragment(0);

        //getStateModel().setEnabledButton(true);

        sendStateModel();
    }
}