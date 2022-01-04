package ru.bstu.it41.service.login;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getShowProgress() {
        return showProgress;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isFailedLogin() {
        return loggedIn != null && !loggedIn;
    }

    public boolean isSuccessLogin() {
        return loggedIn != null && loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn){
        this.loggedIn = loggedIn;
    }

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
