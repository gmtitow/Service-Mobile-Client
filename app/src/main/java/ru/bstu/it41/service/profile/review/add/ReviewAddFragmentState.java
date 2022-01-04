package ru.bstu.it41.service.profile.review.add;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewAddFragmentState extends SerializableStateModel {

    private boolean mDownloadInProgress;
    private boolean mObjectClient;
    private Userinfo mObject;

    private int mTenderId;

    private Reviews mReview;

    public boolean isObjectClient() {
        return mObjectClient;
    }

    public void setObjectClient(boolean objectClient) {
        mObjectClient = objectClient;
    }

    public Userinfo getObject() {
        return mObject;
    }

    public void setObject(Userinfo object) {
        mObject = object;
    }

    public int getTenderId() {
        return mTenderId;
    }

    public void setTenderId(int tenderId) {
        mTenderId = tenderId;
    }

    public boolean isDownloadInProgress() {
        return mDownloadInProgress;
    }

    public void setDownloadInProgress(boolean downloadInProgress) {
        mDownloadInProgress = downloadInProgress;
    }

    public Reviews getReview() {
        return mReview;
    }

    public void setReview(Reviews review) {
        mReview = review;
    }
}
