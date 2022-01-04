package ru.bstu.it41.service.tasks;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.StatusTender;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.TaskAndTenderWithOffers;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.serviceAPI;
import ru.bstu.it41.service.tasks.addTender.TaskAddTenderFragment;
import ru.bstu.it41.service.tasks.change.TaskChangeFragment;

import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskFragmentPresenter extends ReampPresenter<TaskFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }

    public void updateTasks(){
        getStateModel().getTaskAndTenders().clear();
        int userId = DataStore.getUserId(getView().getContext().getApplicationContext());
        if(getStateModel().isExecuted()){
            try {
                if (userId != -1) {
                    List<Tasks> tasks = new Select().from(Tasks.class).where(
                            "userId = ? and status != \"Поиск\"",
                            userId).orderBy("deadline DESC, status ASC").execute();
                    TaskAndTender taskAndTender;
                    for (Tasks task : tasks) {
                        taskAndTender = new TaskAndTender();
                        taskAndTender.setTasks(task);
                        taskAndTender.setTender((Tender) (new Select().from(Tender.class).where("taskId = ?", task.getTaskId()).executeSingle()));
                        getStateModel().getTaskAndTenders().add(taskAndTender);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                if (userId != -1) {
                    List<Tasks> tasks = new Select().from(Tasks.class).where(
                            "userId = ? and status = \"Поиск\"",
                            userId).orderBy("deadline DESC").execute();
                    TaskAndTender taskAndTender;
                    for (Tasks task : tasks) {
                        taskAndTender = new TaskAndTender();
                        taskAndTender.setTasks(task);
                        taskAndTender.setTender((Tender) (new Select().from(Tender.class).where("taskId = ?", task.getTaskId()).executeSingle()));
                        getStateModel().getTaskAndTenders().add(taskAndTender);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadTasks(boolean hide) {

        serviceAPI itableAPI = Controller.getGsonAPI();
        if(!hide)
            getStateModel().setDownloadInProgress(true);
        itableAPI.getTasks().enqueue(new Callback<List<TaskAndTenderWithOffers>>() {
            @Override
            public void onResponse(Call<List<TaskAndTenderWithOffers>> call, Response<List<TaskAndTenderWithOffers>> response) {
                try {
                    getStateModel().getTaskAndTenders().clear();
                    new Delete().from(Tasks.class).where("userId = ?",
                            DataStore.getUserId(getView().getContext().getApplicationContext())).execute();
                    //new Delete().from(Tender.class).execute();
                     ActiveAndroid.beginTransaction();
                   for (TaskAndTenderWithOffers taskAndTender: response.body()) {
                        taskAndTender.getTasks().save();

                        if(taskAndTender.getTender() != null) {
                            taskAndTender.getTender().save();
                            new Delete().from(Offer.class).where("tenderId = ?",
                                    taskAndTender.getTender().getTenderId()).execute();

                            if(taskAndTender.getOffers() != null) {

                                for (OfferWithUser offer : taskAndTender.getOffers()) {
                                    offer.getOffer().save();
                                    offer.getUserinfo().save();
                                }
                            }
                        }
                       TaskAndTender taskTender = new TaskAndTender();
                       taskTender.setTasks(taskAndTender.getTasks());
                       taskTender.setTender(taskAndTender.getTender());
                       taskTender.setOffersCount(taskAndTender.getOffers()!= null?taskAndTender.getOffers().size():0);
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                    updateTasks();
                    getStateModel().setRefreshingEnd(true);
                    getStateModel().setDownloadInProgress(false);
                    sendStateModel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<TaskAndTenderWithOffers>> call, Throwable t) {
                getStateModel().message = t.getMessage();
                //getStateModel().setShowMessage(true);
                Toast.makeText(getView().getContext(), getStateModel().message, Toast.LENGTH_SHORT).show();
                getStateModel().setDownloadInProgress(false);
                //getStateModel().setDownloaded(false);
                getStateModel().setRefreshingEnd(true);
                sendStateModel();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        });
    }
}