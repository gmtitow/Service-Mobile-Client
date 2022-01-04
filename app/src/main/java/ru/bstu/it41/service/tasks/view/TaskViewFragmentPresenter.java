package ru.bstu.it41.service.tasks.view;

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
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskViewFragmentPresenter extends ReampPresenter<TaskViewFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void deleteTask() {
        getStateModel().setTaskDelete(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        try {
            itableAPI.deleteTask(getStateModel().getCurrentTaskAndTender().getTasks().getTaskId()).
                    enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            try {
                                if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                    //getStateModel().getTaskAndTenders().remove(getStateModel().getCurrentTaskAndTender());
                                    getStateModel().getCurrentTaskAndTender().getTasks().delete();

                                    ((Activity)getView().getContext()).finish();
                                    Toast.makeText(getView().getContext(), R.string.task_success_deleted, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getView().getContext(), R.string.task_fail_delete, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            getStateModel().setTaskDelete(false);
                            sendStateModel();
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Toast.makeText(getView().getContext(), R.string.task_fail_delete, Toast.LENGTH_SHORT).show();
                            getStateModel().setTaskDelete(false);
                            sendStateModel();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTender() {
        getStateModel().setTenderDelete(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        try {
            itableAPI.deleteTender(getStateModel().getCurrentTaskAndTender().getTender().getTenderId()).
                    enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            try {
                                if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                    getStateModel().getCurrentTaskAndTender().getTender().delete();
                                    getStateModel().getCurrentTaskAndTender().setTender(null);

                                    //((TaskViewFragment) getViews().get(getViews().size() - 1)).toTasks();
                                    Toast.makeText(getView().getContext(), R.string.tender_success_deleted, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getView().getContext(), R.string.tender_fail_delete, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            getStateModel().setTenderDelete(false);
                            sendStateModel();
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Toast.makeText(getView().getContext(), R.string.tender_fail_delete, Toast.LENGTH_SHORT).show();
                            getStateModel().setTenderDelete(false);
                            sendStateModel();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}