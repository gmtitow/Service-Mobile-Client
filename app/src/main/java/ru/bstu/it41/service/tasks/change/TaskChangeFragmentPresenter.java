package ru.bstu.it41.service.tasks.change;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

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
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.StatusTender;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.serviceAPI;
import ru.bstu.it41.service.tasks.TaskFragmentState;
import ru.bstu.it41.service.tasks.addTender.TaskAddTenderFragment;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskChangeFragmentPresenter extends ReampPresenter<TaskChangeFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }

    public void changeTask(){

        if(getStateModel().getCurrentTask().getLatitude() == null || getStateModel().getCurrentTask().getLongitude() == null){
            List<Address> addresses = null;
            Geocoder mGeocoder = new Geocoder(getView().getContext().getApplicationContext());

            try {
                addresses = mGeocoder.getFromLocationName(getStateModel().getCurrentTask().getAddress(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(addresses!=null && addresses.size()!=0){
                Address addr = addresses.get(0);
                getStateModel().getCurrentTask().setLatitude(addr.getLatitude());
                getStateModel().getCurrentTask().setLongitude(addr.getLongitude());
            }
        }

        serviceAPI itableAPI = Controller.getGsonAPI();

        getStateModel().setChangeInProgress(true);
        sendStateModel();

        itableAPI.changeTask(getStateModel().getCurrentTask().getTaskId(),
                getStateModel().getCurrentTask().getName(),
                getStateModel().getCurrentTask().getDescription(),
                getStateModel().getCurrentTask().getCategoryId(),
                getStateModel().getCurrentTask().getDeadline(),
                getStateModel().getCurrentTask().getPrice(),
                getStateModel().getCurrentTask().getLatitude(),
                getStateModel().getCurrentTask().getLongitude(),
                getStateModel().getCurrentTask().getAddress()).
                enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {

                                getStateModel().getCurrentTask().save();

                                Toast.makeText(getView().getContext(), R.string.task_changed, Toast.LENGTH_SHORT).show();

                                getStateModel().setChangeInProgress(false);

                                sendStateModel();

                                ((TaskChangeFragment)getView()).toViewTask();

                            } else {
                                Toast.makeText(getView().getContext(), R.string.any_wrong, Toast.LENGTH_SHORT).show();
                            }
                            getStateModel().setChangeInProgress(false);
                            sendStateModel();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        try {
                            Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        sendStateModel();
                    }
                });
    }
}