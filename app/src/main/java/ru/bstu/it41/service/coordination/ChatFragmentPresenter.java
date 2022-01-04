package ru.bstu.it41.service.coordination;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;

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
import ru.bstu.it41.service.models.Message;
import ru.bstu.it41.service.models.MessageStatus;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.serviceAPI;

import static ru.bstu.it41.service.any.UserRequest.STATUS_OK;

/**
 * Created by Герман on 18.10.2017.
 */

public class ChatFragmentPresenter extends ReampPresenter<ChatFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }

    public void downloadAllMessages(){
        getStateModel().setDownloadInProgress(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.getAllMessages(getStateModel().getTenderId())
                .enqueue(new Callback<MessageStatus>() {
                    @Override
                    public void onResponse(Call<MessageStatus> call, Response<MessageStatus> response) {
                        if(response.body().getStatus().getStatus().equals(STATUS_OK)){
                            getStateModel().getMessages().clear();
                            getStateModel().getMessages().addAll(response.body().getMessages());

                            if(!getStateModel().getTask().getStatus().equals(Tasks.Status.STATUS_EXECUTING)){
                                if(response.body().getReviewWritten()!=1)
                                    getStateModel().setReviewWritten(false);
                            }

                            new Delete().from(Message.class).where("auctionId = ?",getStateModel().getTenderId()).execute();
                            ActiveAndroid.beginTransaction();
                            for(Message message : response.body().getMessages()){
                                message.save();
                            }

                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();
                        }
                        else
                        {
                            Toast.makeText(getView().getContext(), R.string.messages_dont_loaded, Toast.LENGTH_SHORT).show();
                        }
                        getStateModel().setDownloadInProgress(false);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<MessageStatus> call, Throwable t) {
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        getStateModel().setDownloadInProgress(false);
                        sendStateModel();
                    }
                });
    }

    public void sendMessage(String message){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.sendMessage(getStateModel().getTenderId(),
                message)
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(!response.body().getStatus().equals(STATUS_OK)){
                            Toast.makeText(getView().getContext(), R.string.messages_not_sent, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void sendToken(String token){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.sendToken(token)
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(!response.body().getStatus().equals(STATUS_OK)){
                            //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        }else
                            getStateModel().setTokenSent(true);
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void clearTokens(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.clearTokens()
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(!response.body().getStatus().equals(STATUS_OK)){
                            //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        }else
                            getStateModel().setTokenSent(false);
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void completeTask(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.completeTask(getStateModel().getTenderId())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(!response.body().getStatus().equals(STATUS_OK)){
                            Toast.makeText(getView().getContext(), R.string.toast_task_not_completed, Toast.LENGTH_SHORT).show();
                        }else {
                            //getStateModel().setTokenSent(false);
                            getStateModel().getTask().setStatus(Tasks.Status.STATUS_EXECUTED);
                            getStateModel().getTask().save();
                            getStateModel().setReviewWritten(false);
                            sendStateModel();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void finishTask(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.finishTask(getStateModel().getTenderId())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(!response.body().getStatus().equals(STATUS_OK)){
                            Toast.makeText(getView().getContext(), R.string.toast_task_not_completed, Toast.LENGTH_SHORT).show();
                        }else {
                            //getStateModel().setTokenSent(false);
                            getStateModel().getTask().setStatus(Tasks.Status.STATUS_NONEXECUTED);
                            getStateModel().getTask().save();
                            getStateModel().setReviewWritten(false);
                            sendStateModel();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}