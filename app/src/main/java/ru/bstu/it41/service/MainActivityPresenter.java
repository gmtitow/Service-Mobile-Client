package ru.bstu.it41.service;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;

import java.util.List;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.Status;

import static ru.bstu.it41.service.any.UserRequest.STATUS_OK;

/**
 * Created by Герман on 11.11.2017.
 */

public class MainActivityPresenter extends ReampPresenter<MainActivityState> {

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        getStateModel().message="";
        //getStateModel().setAuthorized(false);
        sendStateModel();
    }

    public void onAuthorizedChanged(Boolean auth){
        getStateModel().setAuthorized(auth);
        sendStateModel();
    }

    public void exit(){

        serviceAPI itableApi= Controller.getStaticAPI();

            itableApi.logout(1).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        //getStateModel().setAuthorized(false);

                        getStateModel().message = response.body();
                        if(getStateModel().message.equals("success")){
                            getStateModel().message = "Вы разлогинились.";
                        }

                        sendStateModel();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    getStateModel().message = t.getMessage();
                    sendStateModel();
                }
            });
    }

    public void clearTokens(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.clearTokens()
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        exit();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        exit();
                    }
                });
    }

    public void downloadCategories(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        getStateModel().setCategoryDownloadInProgress(true);
        itableAPI.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                try {
                    //getStateModel().setCategories(response.body());
                    new Delete().from(Category.class).execute();
                    ActiveAndroid.beginTransaction();
                    try {
                        for (Category category : response.body()) {
                            category.save();
                        }
                        ActiveAndroid.setTransactionSuccessful();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        ActiveAndroid.endTransaction();
                    }

                    getStateModel().setCategoryDownloaded(true);
                    getStateModel().setCategoryDownloadInProgress(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                getStateModel().setCategoryDownloadInProgress(false);
            }
        });
    }
}
