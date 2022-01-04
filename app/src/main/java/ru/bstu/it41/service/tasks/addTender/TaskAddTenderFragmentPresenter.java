package ru.bstu.it41.service.tasks.addTender;

import android.app.Activity;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.StatusTender;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddTenderFragmentPresenter extends ReampPresenter<TaskAddTenderFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }

    public void addTender() {
        serviceAPI itableAPI = Controller.getGsonAPI();

        getStateModel().setCreatingInProgress(true);
        sendStateModel();

        itableAPI.addTender(getStateModel().getCurrentTask().getTaskId(),
                getStateModel().getCurrentDateEndTender().getTime()).
                enqueue(new Callback<StatusTender>() {
                    @Override
                    public void onResponse(Call<StatusTender> call, Response<StatusTender> response) {
                        //getStateModel().setDownloaded(true);
                        if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {

                            Toast.makeText(getView().getContext(), R.string.tender_created, Toast.LENGTH_SHORT).show();
                            getStateModel().setCreatingInProgress(false);
                            response.body().getTender().save();

                            //((Activity)getView().getContext()).setResult(Activity.RESULT_OK);
                            //getNavigation().getActivity().finish();
                            ((Activity)getView().getContext()).finish();

                            //Может этого и можно избежать
                        } else {
                            //Возникла куча ошибок, увы
                            getStateModel().setCreatingInProgress(false);
                            sendStateModel();
                            Toast.makeText(getView().getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusTender> call, Throwable t) {
                        getStateModel().setCreatingInProgress(false);
                        sendStateModel();
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}