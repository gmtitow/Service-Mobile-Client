package ru.bstu.it41.service.coordination;

import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.Message;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ChatFragmentState extends SerializableStateModel {

    private List<Message> mMessages;

    private boolean mDownloadInProgress;
    private boolean mDownloaded;
    private boolean mTokenSent;

    private boolean mClient;

    private int mTenderId;

    private boolean mReviewWritten = true;

    private Tasks mTask;

    private Userinfo mOtherUserinfo;

    public Tasks getTask() {
        return mTask;
    }

    public void setTask(Tasks task) {
        mTask = task;
    }

    public boolean isTokenSent() {
        return mTokenSent;
    }

    public void setTokenSent(boolean tokenSent) {
        mTokenSent = tokenSent;
    }

    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        mDownloaded = downloaded;
    }

    public Userinfo getOtherUserinfo() {
        return mOtherUserinfo;
    }

    public void setOtherUserinfo(Userinfo otherUserinfo) {
        mOtherUserinfo = otherUserinfo;
    }

    public int getTenderId() {
        return mTenderId;
    }

    public void setTenderId(int tenderId) {
        mTenderId = tenderId;
    }

    public boolean isDownloadInProgress() {
        return mDownloadInProgress;
    }

    public void setDownloadInProgress(boolean downloadInProgress) {
        mDownloadInProgress = downloadInProgress;
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
    }

    public boolean isClient() {
        return mClient;
    }

    public void setClient(boolean client) {
        mClient = client;
    }

    public boolean isReviewWritten() {
        return mReviewWritten;
    }

    public void setReviewWritten(boolean reviewWritten) {
        mReviewWritten = reviewWritten;
    }
}
