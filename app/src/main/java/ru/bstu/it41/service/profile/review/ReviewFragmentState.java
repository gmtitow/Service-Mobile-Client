package ru.bstu.it41.service.profile.review;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.ReviewAndUserinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewFragmentState extends SerializableStateModel {

    private boolean mDownloaded;
    private boolean mDownloadInProgress;

    private List<ReviewAndUserinfo> mReviewAndUserinfos = new ArrayList<>();

    public boolean isDownloadInProgress() {
        return mDownloadInProgress;
    }

    public void setDownloadInProgress(boolean downloadInProgress) {
        mDownloadInProgress = downloadInProgress;
    }

    public List<ReviewAndUserinfo> getReviewAndUserinfos() {
        return mReviewAndUserinfos;
    }

    public void setReviewAndUserinfos(List<ReviewAndUserinfo> reviewAndUserinfos) {
        mReviewAndUserinfos = reviewAndUserinfos;
    }

    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        mDownloaded = downloaded;
    }

    private boolean mRefreshingEnd;

    public boolean isRefreshingEnd() {
        return mRefreshingEnd;
    }

    public void setRefreshingEnd(boolean refreshingEnd) {
        mRefreshingEnd = refreshingEnd;
    }

    private int currentReview;

    public int getCurrentReview() {
        return currentReview;
    }

    public void setCurrentReview(int currentReview) {
        this.currentReview = currentReview;
    }

    private int mCurrentUserId;

    public int getCurrentUserId() {
        return mCurrentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        mCurrentUserId = currentUserId;
    }
}
