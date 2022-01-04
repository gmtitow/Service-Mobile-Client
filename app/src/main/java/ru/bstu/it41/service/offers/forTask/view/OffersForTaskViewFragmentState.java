package ru.bstu.it41.service.offers.forTask.view;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OfferWithUser;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskViewFragmentState extends SerializableStateModel {

    private boolean mProgress;

    private OfferWithUser mCurrentOffer;

    public boolean isProgress() {
        return mProgress;
    }

    public void setProgress(boolean progress) {
        mProgress = progress;
    }

    public OfferWithUser getCurrentOffer() {
        return mCurrentOffer;
    }

    public void setCurrentOffer(OfferWithUser currentOffer) {
        mCurrentOffer = currentOffer;
    }

}
