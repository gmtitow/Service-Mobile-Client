package ru.bstu.it41.service.offers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersFragmentState extends SerializableStateModel {

    private List<OfferWithTask> mOffersWithTask = new ArrayList<>();
    private boolean mNeedDownload;
    private boolean mDownloadInProgress;
    private boolean mRefreshingEnd;

    private boolean mSelected;

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isDownloadInProgress() {
        return mDownloadInProgress;
    }

    public void setDownloadInProgress(boolean downloadInProgress) {
        mDownloadInProgress = downloadInProgress;
    }

    public boolean isRefreshingEnd() {
        return mRefreshingEnd;
    }

    public void setRefreshingEnd(boolean refreshingEnd) {
        mRefreshingEnd = refreshingEnd;
    }

    public List<OfferWithTask> getOffersWithTask() {
        return mOffersWithTask;
    }

    public void setOffersWithTask(List<OfferWithTask> offersWithTask) {
        mOffersWithTask = offersWithTask;
    }

    public boolean isNeedDownload() {
        return mNeedDownload;
    }

    public void setNeedDownload(boolean needDownload) {
        mNeedDownload = needDownload;
    }
}
