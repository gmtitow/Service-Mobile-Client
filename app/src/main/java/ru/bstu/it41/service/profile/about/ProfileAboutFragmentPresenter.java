package ru.bstu.it41.service.profile.about;

import android.widget.Toast;

import com.activeandroid.query.Select;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class ProfileAboutFragmentPresenter extends ReampPresenter<ProfileAboutFragmentState> implements Serializable {
    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void saveAbout(){
        getStateModel().setShowProgress(true);
        sendStateModel();
        serviceAPI api = Controller.getGsonAPI();
        api.changeProfileAbout(
                getStateModel().getTemporaryAbout())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            getStateModel().setShowProgress(false);
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                Toast.makeText(getView().getContext(), R.string.profile_change_success, Toast.LENGTH_SHORT).show();
                                getStateModel().setAbout(getStateModel().getTemporaryAbout());

                                Userinfo userinfo = new Select().from(Userinfo.class).where("userId = ?",
                                        DataStore.getUserId(getView().getContext().getApplicationContext())).executeSingle();
                                userinfo.setAbout(getStateModel().getTemporaryAbout());
                                userinfo.save();

                                getStateModel().setAbout(getStateModel().getTemporaryAbout());

                            } else {
                                Toast.makeText(getView().getContext(), R.string.profile_change_error, Toast.LENGTH_SHORT).show();
                            }

                            sendStateModel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                        sendStateModel();
                    }
                });
    }

    public void updateView() {
        sendStateModel();
    }
}