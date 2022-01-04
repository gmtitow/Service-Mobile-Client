package ru.bstu.it41.service.tenders.tenderView;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OtherTender;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderViewFragmentState extends SerializableStateModel {
    private OtherTender mTender;


    public OtherTender getTender() {
        return mTender;
    }

    public void setTender(OtherTender tender) {
        mTender = tender;
    }


}
