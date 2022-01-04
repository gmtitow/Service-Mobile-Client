package ru.bstu.it41.service.test;

import android.databinding.Bindable;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.ResultAuthorizationInfo;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 08.10.2017.
 */

public class LoginFragmentPresenter extends ReampPresenter<LoginFragmentState> {

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //Это временно, само собой.
        getStateModel().setLogin("agggr@mail.ru");
        getStateModel().setPassword("1234");

        getStateModel().setShowProgress(false);
        getStateModel().setLoggedIn(null);
        getStateModel().setLostConnection(false);

        sendStateModel();
    }
    @Bindable
    public String getLogin(){
        return getStateModel().getLogin();
    }

    @Bindable
    public String getPassword(){
        return getStateModel().getPassword();
    }

    @Bindable
    public boolean getShowProgress(){
        return getStateModel().getShowProgress();
    }

    public void loginChanged(String login) {
        getStateModel().setLogin(login);
        sendStateModel();
    }

    public void passwordChanged(String password) {
        getStateModel().setPassword(password);
        sendStateModel();
    }

    public void login() {

        getStateModel().setShowProgress(true);
        getStateModel().setLoggedIn(null);
        sendStateModel();

        performLogin();
    }

    void performLogin() {
        serviceAPI itableApi= Controller.getGsonAPI();
        try {
            itableApi.login(getStateModel().getLogin(), getStateModel().getPassword()).enqueue(new Callback<ResultAuthorizationInfo>() {
            @Override
            public void onResponse(Call<ResultAuthorizationInfo> call, Response<ResultAuthorizationInfo> response) {
                try {
                    getStateModel().setShowProgress(false);
                    getStateModel().setLoggedIn(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK));
                    if(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK))
                    {
                        ((MainActivity)getView().getContext()).getPresenter().getStateModel().setUserinfo(
                                response.body().getAllForUser());
                        ((LoginTestFragment) getViews().get(getViews().size() - 1)).toProfile();
                    }
                    getStateModel().setLostConnection(false);
                    //getStateModel().message = "status: " + response.body().getStatus();
                    sendStateModel();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResultAuthorizationInfo> call, Throwable t) {
                getStateModel().setShowProgress(false);
                getStateModel().setLoggedIn(null);
                getStateModel().setLostConnection(true);
                getStateModel().message = t.getMessage();
                sendStateModel();
            }
        });
    }catch (Exception e){
        e.printStackTrace();
    }

    }
}
