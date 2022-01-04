package ru.bstu.it41.service.tasks.change;

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

public class TaskChangeFragmentState extends SerializableStateModel {


    private boolean mChangeInProgress;
    private Tasks mCurrentTask;

    public Tasks getCurrentTask() {
        return mCurrentTask;
    }

    public void setCurrentTask(Tasks currentTask) {
        mCurrentTask = currentTask;
    }

    public boolean allowNext(){
        return (mCurrentTask.getPrice() != 0 && mCurrentTask.getDeadline() != null && mCurrentTask.getName().length()>0);
    }

    public boolean isChangeInProgress() {
        return mChangeInProgress;
    }

    public void setChangeInProgress(boolean changeInProgress) {
        mChangeInProgress = changeInProgress;
    }
}
