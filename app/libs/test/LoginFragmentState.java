package ru.bstu.it41.service.test;

import android.databinding.Bindable;

import etr.android.reamp.mvp.SerializableStateModel;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Герман on 08.10.2017.
 */

public class LoginFragmentState extends SerializableStateModel {

    private String login;
    private String password;

    private Boolean showProgress;
    private Boolean loggedIn;
    private Boolean lostConnection;
    public String message;

    @Bindable
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public boolean getShowProgress() {
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress = showProgress;
    }

    @Bindable
    public boolean isFailedLogin() {
        return loggedIn != null && !loggedIn;
    }

    @Bindable
    public boolean isSuccessLogin() {
        return loggedIn != null && loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    @Bindable
    public boolean getLostConnection() {
        return lostConnection;
    }

    public void setLostConnection(Boolean lostConnection) {
        this.lostConnection = lostConnection;
    }
    public boolean isLoginActionEnabled() {
        return !showProgress
                && !isEmpty(login)
                && !isEmpty(password);
    }
}
