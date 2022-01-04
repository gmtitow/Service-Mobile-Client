package ru.bstu.it41.service.tenders;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.yandex.mapkit.map.MapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.tenders.tendersMap.TendersMapFragment;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderFragmentState extends SerializableStateModel {

    private boolean mDownloaded = false;

    private List<OtherTender> mTenders = new ArrayList<>();
    private boolean dataUpdated = false;
    private boolean mRefreshingEnd;

    public OtherTender getTenderById(int id){
        //mTenders.indexOf()
        for (OtherTender tender:mTenders) {
            if(tender.getTender().getTenderId() == id)
                return tender;
        }
        return null;
    }

    /*private SparseBooleanArray mOpenedTenders = new SparseBooleanArray();

    public SparseBooleanArray getOpenedTenders() {
        return mOpenedTenders;
    }

    public void setOpenedTenders(SparseBooleanArray openedTenders) {
        mOpenedTenders = openedTenders;
    }*/

    public boolean isDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(boolean dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public List<OtherTender> getTenders() {
        return mTenders;
    }

    public void setTenders(List<OtherTender> tenders) {
        mTenders = tenders;
    }

    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        mDownloaded = downloaded;
    }

    public boolean isRefreshingEnd() {
        return mRefreshingEnd;
    }

    public void setRefreshingEnd(boolean refreshingEnd) {
        mRefreshingEnd = refreshingEnd;
    }


    private int mOpenedTender;

    public int getOpenedTender() {
        return mOpenedTender;
    }

    public void setOpenedTender(int openedTender) {
        mOpenedTender = openedTender;
    }
}
