package ru.bstu.it41.service.profile.settings;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Place;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.ProfileFragmentState;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class SettingsFragmentPresenter extends ReampPresenter<SettingsFragmentState> implements Serializable {
    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }


    public void changeSettings(){

        getStateModel().setProgressChange(true);
        sendStateModel();
        //Запрос к серверу
        serviceAPI api = Controller.getGsonAPI();
        api.changeSettings(
                getStateModel().getTempSettings().getNotificationEmail(),
                getStateModel().getTempSettings().getNotificationSms(),
                getStateModel().getTempSettings().getNotificationPush())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();
                                getStateModel().getTempSettings().save();

                                getStateModel().getSettings().setNotificationEmail(getStateModel().getTempSettings().getNotificationEmail());
                                getStateModel().getSettings().setNotificationSms(getStateModel().getTempSettings().getNotificationSms());
                                getStateModel().getSettings().setNotificationPush(getStateModel().getTempSettings().getNotificationPush());
                            } else {
                                Toast.makeText(getView().getContext(), R.string.profile_change_error, Toast.LENGTH_SHORT).show();
                            }

                            getStateModel().setProgressChange(false);
                            sendStateModel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                        getStateModel().setProgressChange(false);
                        sendStateModel();
                    }
                });
    }
}