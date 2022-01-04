package ru.bstu.it41.service.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Userinfo;

import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class RegisterFragmentState extends SerializableStateModel {

    private Userinfo mUserinfo;

    public Userinfo getUserinfo() {
        return mUserinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        mUserinfo = userinfo;
    }

    private Boolean showProgress;
    private Boolean lostConnection;

    private Boolean loggedIn;
    private Boolean alreadyExists;
    public String message;

    //В этом перечне нет необязательных полей, которые, тем не менее,
    //присутствуют на сайте. Придется добавить позже.

    private String firstname;
    private Boolean fail_firstname;
    private String lastname;
    private Boolean fail_lastname;
    private String email;
    private Boolean fail_email;
    private String telephone;
    private Boolean fail_telephone;

    private byte male;

    public byte getMale() {
        return male;
    }

    public void setMale(byte male) {
        this.male = male;
    }

    private String password;
    private Boolean fail_password;

    private String confirm;
    private Boolean fail_confirm;


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if(firstname.length() < 1 || firstname.length()>32) {
            fail_firstname = true;
            this.firstname = firstname;
        }
        else {
            fail_firstname = false;
            this.firstname = firstname;
        }
    }

    public Boolean isFail_firstname() {
        return fail_firstname != null && fail_firstname;
    }

    public void setFail_firstname(Boolean fail_firstname) {
        this.fail_firstname = fail_firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if(lastname.length() < 1 || lastname.length()>32) {
            fail_lastname = true;
            this.lastname = lastname;
        }
        else {
            fail_lastname = false;
            this.lastname = lastname;
        }
    }

    public Boolean isFail_lastname() {
        return fail_lastname != null && fail_lastname;
    }

    public void setFail_lastname(Boolean fail_lastname) {
        this.fail_lastname = fail_lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        //Регулярное выражение
        Pattern pattern = Pattern.compile(".+@.+\\..+");
        Matcher m = pattern.matcher(email);
        if(m.matches()) {
            this.email = email;
            fail_email = false;
        }
        else {
            fail_email = true;
            this.email = email;
        }
    }

    public Boolean isFail_email() {
        return fail_email != null && fail_email;
    }

    public void setFail_email(Boolean fail_email) {
        this.fail_email = fail_email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if(telephone.length() < 3 || telephone.length() > 32){
            fail_telephone = true;
            this.telephone = telephone;
        }
        else {
            this.telephone = telephone;
            fail_telephone = false;
        }
    }

    public Boolean isFail_telephone() {
        return fail_telephone != null && fail_telephone;
    }

    public void setFail_telephone(Boolean fail_telephone) {
        this.fail_telephone = fail_telephone;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length() < 4) {
            this.password = password;
            fail_password = true;
        }else{
            this.password = password;
            fail_password = false;
        }
    }

    public Boolean isFail_password() {
        return fail_password != null && fail_password;
    }

    public void setFail_password(Boolean fail_password) {
        this.fail_password = fail_password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        if(!confirm.equals(password)){
            this.confirm = confirm;
            fail_confirm = true;
        }else {
            this.confirm = confirm;
            fail_confirm = false;
        }
    }

    public Boolean isFail_confirm() {
        return fail_confirm != null && fail_confirm;
    }

    public void setFail_confirm(Boolean fail_confirm) {
        this.fail_confirm = fail_confirm;
    }

    public Boolean showFailedLogin() {
        return loggedIn != null && !loggedIn;
    }

    public Boolean showSuccessLogin() {
        return loggedIn != null && loggedIn;
    }

    public Boolean showAlreadyExists() {
        return alreadyExists;
    }

    public void setAlreadyExists(Boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    public Boolean getShowProgress() {
        return showProgress;
    }

    public void setLoggedIn(Boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    public void setShowProgress(Boolean showProgress) {
        this.showProgress = showProgress;
    }

    public Boolean getLostConnection() {
        return lostConnection;
    }

    public void setLostConnection(Boolean lostConnection) {
        this.lostConnection = lostConnection;
    }
}
