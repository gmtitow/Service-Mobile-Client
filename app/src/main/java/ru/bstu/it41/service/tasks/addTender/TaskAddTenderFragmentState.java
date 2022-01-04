package ru.bstu.it41.service.tasks.addTender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddTenderFragmentState extends SerializableStateModel {

    private Calendar mCurrentDateEndTender;

    public Calendar getCurrentDateEndTender() {
        return mCurrentDateEndTender;
    }

    public void setCurrentDateEndTender(Calendar currentDateEndTender) {
        mCurrentDateEndTender = currentDateEndTender;
    }

    public boolean allowCreate(){
        return (mCurrentDateEndTender != null && mCurrentDateEndTender.getTime().getTime() < mCurrentTask.getDeadline().getTime());
    }
    //Для фрагмента создания тендера
    private Tasks mCurrentTask;

    public Tasks getCurrentTask() {
        return mCurrentTask;
    }

    public void setCurrentTask(Tasks currentTask) {
        mCurrentTask = currentTask;
    }

    private boolean mCreatingInProgress = false;

    public boolean isCreatingInProgress() {
        return mCreatingInProgress;
    }

    public void setCreatingInProgress(boolean creatingInProgress) {
        mCreatingInProgress = creatingInProgress;
    }
}
