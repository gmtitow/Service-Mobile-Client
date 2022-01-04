package ru.bstu.it41.service.login;

import com.activeandroid.ActiveAndroid;
import com.google.firebase.iid.FirebaseInstanceId;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.ResultAuthorizationInfo;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.serviceAPI;

import static ru.bstu.it41.service.any.UserRequest.STATUS_OK;

/**
 * Created by Герман on 08.10.2017.
 */

public class LoginFragmentPresenter extends ReampPresenter<LoginFragmentState> {

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //Это временно, само собой.
        /*getStateModel().setLogin("agggr@mail.ru");
        getStateModel().setPassword("1234");*/

        getStateModel().setShowProgress(false);
        getStateModel().setLoggedIn(null);
        getStateModel().setLostConnection(false);

        sendStateModel();
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
                    DataStore.clearAll(getView().getContext().getApplicationContext());
                    getStateModel().setShowProgress(false);
                    getStateModel().setLoggedIn(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK));

                    //
                    ActiveAndroid.beginTransaction();
                    if(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK))
                    {
                        DataStore.setUserId(getView().getContext().getApplicationContext(),response.body().getAllForUser().getUserinfo().getUserId());

                        response.body().getAllForUser().getUserinfo().save();
                        response.body().getAllForUser().getUser().save();
                        response.body().getAllForUser().getSettings().save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                    getStateModel().setLostConnection(false);

                    if(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK))
                    {
                        DataStore.setUserId(getView().getContext().getApplicationContext(),response.body().getAllForUser().getUserinfo().getUserId());
                        ((MainActivity)getView().getContext()).getPresenter().getStateModel().setUserinfo(
                                response.body().getAllForUser());
                        ((LoginFragment) getViews().get(getViews().size() - 1)).toProfile();
                    }
                    //getStateModel().message = "status: " + response.body().getStatus();
                    sendToken(FirebaseInstanceId.getInstance().getToken());
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

    public void sendToken(String token){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.sendToken(token)
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        /*if(!response.body().getStatus().equals(STATUS_OK)){
                            //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        }else
                            //getStateModel().setTokenSent(true);*/
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        //Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
