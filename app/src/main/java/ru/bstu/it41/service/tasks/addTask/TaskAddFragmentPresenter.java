package ru.bstu.it41.service.tasks.addTask;

import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

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
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.StatusTask;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddFragmentPresenter extends ReampPresenter<TaskAddFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void addTask() {
        serviceAPI itableAPI = Controller.getGsonAPI();

        getStateModel().setCreatingInProgress(true);
        sendStateModel();

        if(getStateModel().getCurrentLatitude() == null || getStateModel().getCurrentLongitude() == null){
            List<Address> addresses = null;
            Geocoder mGeocoder = new Geocoder(getView().getContext().getApplicationContext());

            try {
                addresses = mGeocoder.getFromLocationName(getStateModel().getCurrentAddress(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(addresses!=null && addresses.size()!=0){
                Address addr = addresses.get(0);
                getStateModel().setCurrentLatitude(addr.getLatitude());
                getStateModel().setCurrentLongitude(addr.getLongitude());
            }
        }

        itableAPI.addTask(getStateModel().getCurrentName(),
                getStateModel().getCurrentDescription(),
                getStateModel().getCurrentCategory(),
                getStateModel().getCurrentDeadline().getTime(),
                getStateModel().getCurrentPrice(),
                getStateModel().getCurrentLatitude(),
                getStateModel().getCurrentLongitude(),
                getStateModel().getCurrentAddress()).
                enqueue(new Callback<StatusTask>() {
                    @Override
                    public void onResponse(Call<StatusTask> call, Response<StatusTask> response) {
                        try {
                            //getStateModel().setDownloaded(true);
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                response.body().getTaskAndTender().getTasks().save();

                                Toast.makeText(getView().getContext(), R.string.task_created, Toast.LENGTH_SHORT).show();
                                getStateModel().setCreatingInProgress(false);

                                ((TaskAddFragment)getView()).toAddTender(
                                        response.body().getTaskAndTender().getTasks().getTaskId());
                                //getStateModel().setTaskCreated(true);
                                //Может этого и можно избежать
                            } else {
                                //Возникла куча ошибок, увы
                                Toast.makeText(getView().getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusTask> call, Throwable t) {
                        //getStateModel().setShowMessage(true);
                        getStateModel().setCreatingInProgress(false);
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        sendStateModel();
                    }
                });
    }
}