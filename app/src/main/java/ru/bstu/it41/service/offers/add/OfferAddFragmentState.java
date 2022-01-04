package ru.bstu.it41.service.offers.add;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class OfferAddFragmentState extends SerializableStateModel {
    private int mCurrentTenderId;

    public int getCurrentTenderId() {
        return mCurrentTenderId;
    }

    public void setCurrentTenderId(int currentTenderId) {
        mCurrentTenderId = currentTenderId;
    }

    private Tasks mCurrentTask;

    private Offer mTempOffer;

    public Offer getTempOffer() {
        return mTempOffer;
    }

    public void setTempOffer(Offer tempOffer) {
        mTempOffer = tempOffer;
    }

    public Tasks getCurrentTask() {
        return mCurrentTask;
    }

    public void setCurrentTask(Tasks currentTask) {
        mCurrentTask = currentTask;
    }

    public boolean allowAddOffer(){
        Date today = new Date();
        return (getTempOffer().getDeadline()!= null && getTempOffer().getDeadline().getTime()>today.getTime()
                &&getTempOffer().getPrice()!= 0);
    }

    private boolean mCreatingOffer;

    public boolean isCreatingOffer() {
        return mCreatingOffer;
    }

    public void setCreatingOffer(boolean creatingOffer) {
        mCreatingOffer = creatingOffer;
    }

    //
    //public OtherTender mOtherTender;
}
