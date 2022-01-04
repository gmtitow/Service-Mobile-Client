package ru.bstu.it41.service.offers.view;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.Reviews;

/**
 * Created by Герман on 18.10.2017.
 */

public class OfferViewFragmentState extends SerializableStateModel {


    private OfferWithTask mCurrentOffer;

    private boolean mOfferDelete;


    public OfferWithTask getCurrentOffer() {
        return mCurrentOffer;
    }

    public void setCurrentOffer(OfferWithTask currentOffer) {
        mCurrentOffer = currentOffer;
    }

    public boolean isOfferDelete() {
        return mOfferDelete;
    }

    public void setOfferDelete(boolean offerDelete) {
        mOfferDelete = offerDelete;
    }
}
