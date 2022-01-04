package ru.bstu.it41.service;

import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Category;

/**
 * Created by Герман on 11.11.2017.
 */

public class MainActivityState extends SerializableStateModel {
    private Boolean authorized = false;
    private boolean first = true;
    public String message;

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    //Категории
    private boolean mCategoryDownloaded;
    private boolean mCategoryDownloadInProgress;

    public boolean isCategoryDownloadInProgress() {
        return mCategoryDownloadInProgress;
    }

    public void setCategoryDownloadInProgress(boolean categoryDownloadInProgress) {
        mCategoryDownloadInProgress = categoryDownloadInProgress;
    }

    public boolean isCategoryDownloaded() {
        return mCategoryDownloaded;
    }

    public void setCategoryDownloaded(boolean categoryDownloaded) {
        mCategoryDownloaded = categoryDownloaded;
    }

    private Controller mController;

    public Controller getController() {
        return mController;
    }

    public void setController(Controller controller) {
        mController = controller;
    }

    private AllForUser mUserinfo = null;

    public AllForUser getUserinfo() {
        return mUserinfo;
    }

    public void setUserinfo(AllForUser userinfo) {
        mUserinfo = userinfo;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
