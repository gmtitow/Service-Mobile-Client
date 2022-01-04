package ru.bstu.it41.service.register;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class RegisterFragmentPresenter extends ReampPresenter<RegisterFragmentState> {

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //Это временно, само собой.

        getStateModel().setShowProgress(false);
        getStateModel().setLostConnection(false);
        getStateModel().setLoggedIn(null);

        getStateModel().setMale((byte)1);

        //fail-ы
        getStateModel().setFail_firstname(null);
        getStateModel().setFail_lastname(null);
        getStateModel().setFail_email(null);
        getStateModel().setFail_telephone(null);
        getStateModel().setFail_password(null);
        getStateModel().setFail_confirm(null);
        getStateModel().setAlreadyExists(false);

        sendStateModel();
    }

    public void register() {

        if(!getStateModel().isFail_confirm()) {
            getStateModel().setShowProgress(true);

            performRegister();

            sendStateModel();
        }
    }

    public void firstnameChanged(String firstname){
        getStateModel().setFirstname(firstname);
        sendStateModel();
    }

    public void lastnameChanged(String lastname){
        getStateModel().setLastname(lastname);
        sendStateModel();
    }

    public void emailChanged(String email){
        getStateModel().setEmail(email);
        sendStateModel();
    }

    public void telephoneChanged(String telephone){
        getStateModel().setTelephone(telephone);
        sendStateModel();
    }


    public void passwordChanged(String password){
        getStateModel().setPassword(password);
        sendStateModel();
    }
    public void confirmChanged(String confirm){
        getStateModel().setConfirm(confirm);
        sendStateModel();
    }

    public void maleChanged(byte male){
        getStateModel().setMale(male);
    }

    private void performRegister() {

        serviceAPI itableApi= Controller.getGsonAPI();

        itableApi.register(getStateModel().getFirstname(),
                getStateModel().getLastname(),
                getStateModel().getMale(),
                getStateModel().getEmail(),
                getStateModel().getTelephone(),
                getStateModel().getPassword()
        ).enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            getStateModel().message = /*response.body();*/"Status: " + response.body().getStatus();
                            getStateModel().setShowProgress(false);
                            sendStateModel();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        getStateModel().setShowProgress(false);
                        getStateModel().setLoggedIn(null);
                        getStateModel().setLostConnection(true);
                        getStateModel().message = t.getMessage();

                        sendStateModel();
                    }
                });
    }
}