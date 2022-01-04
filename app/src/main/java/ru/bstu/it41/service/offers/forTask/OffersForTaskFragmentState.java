package ru.bstu.it41.service.offers.forTask;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OfferWithUser;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskFragmentState extends SerializableStateModel {

    private List<OfferWithUser> mOffersWithUser = new ArrayList<OfferWithUser>();

    private boolean mDownloaded;

    private boolean mRefreshingEnd;

    public boolean isRefreshingEnd() {
        return mRefreshingEnd;
    }

    public void setRefreshingEnd(boolean refreshingEnd) {
        mRefreshingEnd = refreshingEnd;
    }

    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        mDownloaded = downloaded;
    }

    public List<OfferWithUser> getOffersWithUser() {
        return mOffersWithUser;
    }

    public void setOffersWithUser(List<OfferWithUser> offersWithUser) {
        mOffersWithUser = offersWithUser;
    }

    private int mCurrentTenderId;

    public int getCurrentTenderId() {
        return mCurrentTenderId;
    }

    public void setCurrentTenderId(int currentTenderId) {
        mCurrentTenderId = currentTenderId;
    }
}
