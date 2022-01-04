package ru.bstu.it41.service.tasks.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskViewFragmentState extends SerializableStateModel {



    public String message;

    private boolean mRefreshingEnd;


    public boolean isRefreshingEnd() {
        return mRefreshingEnd;
    }

    public void setRefreshingEnd(boolean refreshingEnd) {
        mRefreshingEnd = refreshingEnd;
    }

    //При добавлении и изменении

    //Для просмотра подробностей задания
    private TaskAndTender mCurrentTaskAndTender = new TaskAndTender();

    private Offer mSelectedOffer = null;

    public TaskAndTender getCurrentTaskAndTender() {
        return mCurrentTaskAndTender;
    }

    public void setCurrentTaskAndTender(TaskAndTender currentTaskAndTender) {
        mCurrentTaskAndTender = currentTaskAndTender;
    }

    private boolean mTaskDelete;

    private boolean mTenderDelete;

    public Offer getSelectedOffer() {
        return mSelectedOffer;
    }

    public void setSelectedOffer(Offer selectedOffer) {
        mSelectedOffer = selectedOffer;
    }

    public boolean isTaskDelete() {
        return mTaskDelete;
    }

    public void setTaskDelete(boolean taskDelete) {
        mTaskDelete = taskDelete;
    }

    public boolean isTenderDelete() {
        return mTenderDelete;
    }

    public void setTenderDelete(boolean tenderDelete) {
        mTenderDelete = tenderDelete;
    }
}
