package ru.bstu.it41.service.tasks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskFragmentState extends SerializableStateModel {

    private List<TaskAndTender> mTaskAndTenders = new ArrayList<TaskAndTender>();

    private boolean mDownloaded;
    private boolean mDownloadInProgress;
    private boolean mShowMessage;

    private boolean mExecuted;

    public boolean isExecuted() {
        return mExecuted;
    }

    public void setExecuted(boolean executed) {
        mExecuted = executed;
    }

    public String message;

    public boolean isDownloadInProgress() {
        return mDownloadInProgress;
    }

    public void setDownloadInProgress(boolean downloadInProgress) {
        mDownloadInProgress = downloadInProgress;
    }

    private boolean mRefreshingEnd;

    public boolean isShowMessage() {
        return mShowMessage;
    }

    public void setShowMessage(boolean showMessage) {
        mShowMessage = showMessage;
    }

    public List<TaskAndTender> getTaskAndTenders() {
        return mTaskAndTenders;
    }

    public void setTaskAndTenders(List<TaskAndTender> taskAndTenders) {
        mTaskAndTenders = taskAndTenders;
    }

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

    //При добавлении и изменении

    public void copyToTemp(Tasks task){
        setCurrentName(task.getName());
        setCurrentDescription(task.getDescription());
        Calendar calend = Calendar.getInstance();
        calend.setTime(task.getDeadline());
        setCurrentDeadline(calend);
        setCurrentCategory(task.getCategoryId());
        setCurrentPrice(task.getPrice());
        setCurrentTaskId(task.getTaskId());
    }

    public void copyFromTempToObject(TaskAndTender task){
        task.getTasks().setName(getCurrentName());
        task.getTasks().setCategoryId(getCurrentCategory());
        task.getTasks().setDescription(getCurrentDescription());
        task.getTasks().setDeadline(getCurrentDeadline().getTime());
        task.getTasks().setPrice(getCurrentPrice());
    }

    private String mCurrentName;
    private String mCurrentDescription;
    private int mCurrentCategory;
    private Calendar mCurrentDeadline;
    private Integer mCurrentPrice;
    private Double mCurrentLatitude = null;
    private Double mCurrentLongitude = null;

    public Double getCurrentLatitude() {
        return mCurrentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        mCurrentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return mCurrentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        mCurrentLongitude = currentLongitude;
    }

    public String getCurrentName() {
        return mCurrentName;
    }

    public void setCurrentName(String currentName) {
        mCurrentName = currentName;
    }

    public String getCurrentDescription() {
        return mCurrentDescription;
    }

    public void setCurrentDescription(String currentDescription) {
        mCurrentDescription = currentDescription;
    }

    public int getCurrentCategory() {
        return mCurrentCategory;
    }

    public void setCurrentCategory(int currentCategory) {
        mCurrentCategory = currentCategory;
    }

    public Calendar getCurrentDeadline() {
        return mCurrentDeadline;
    }

    public void setCurrentDeadline(Date currentDeadline) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDeadline);
        mCurrentDeadline = cal;
    }

    public void setCurrentDeadline(Calendar currentDeadline) {
        mCurrentDeadline = currentDeadline;
    }

    public int getCurrentPrice() {
        return mCurrentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        mCurrentPrice = currentPrice;
    }

    public boolean allowNext(){
        return (mCurrentPrice != null && mCurrentPrice != 0 && mCurrentDeadline != null && mCurrentName.length()>0
                && mCurrentCategory != 0);
    }

    private Calendar mCurrentDateEndTender;

    public Calendar getCurrentDateEndTender() {
        return mCurrentDateEndTender;
    }

    public void setCurrentDateEndTender(Calendar currentDateEndTender) {
        mCurrentDateEndTender = currentDateEndTender;
    }

    public boolean allowCreate(){
        return (mCurrentDateEndTender != null && mCurrentDateEndTender.getTime().getTime() < mCurrentDeadline.getTime().getTime());
    }

    private boolean mTaskCreated;

    public boolean isTaskCreated() {
        return mTaskCreated;
    }

    public void setTaskCreated(boolean taskCreated) {
        mTaskCreated = taskCreated;
    }

    //Для фрагмента создания тендера
    private int mCurrentTaskId;

    public int getCurrentTaskId() {
        return mCurrentTaskId;
    }

    public void setCurrentTaskId(int currentTaskId) {
        mCurrentTaskId = currentTaskId;
    }

    public TaskAndTender findTaskById(int taskId){
        for(TaskAndTender task : mTaskAndTenders){
            if(task.getTasks().getTaskId() == taskId)
                return task;
        }
        return null;
    }

    //Для просмотра подробностей задания
    private TaskAndTender mCurrentTaskAndTender;

    public TaskAndTender getCurrentTaskAndTender() {
        return mCurrentTaskAndTender;
    }

    public void setCurrentTaskAndTender(TaskAndTender currentTaskAndTender) {
        mCurrentTaskAndTender = currentTaskAndTender;
    }

    private boolean mTaskDelete;

    private boolean mTenderDelete;

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
