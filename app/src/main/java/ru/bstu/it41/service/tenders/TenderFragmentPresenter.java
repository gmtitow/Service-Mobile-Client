package ru.bstu.it41.service.tenders;

import android.widget.Toast;
import android.widget.Toolbar;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.serviceAPI;


/**
 * Created by Герман on 16.11.2017.
 */

public class TenderFragmentPresenter extends ReampPresenter<TenderFragmentState> {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
    }

    public List<OtherTender> getTenders(){
        return getStateModel().getTenders();
    }

    /*public void changeTenders(List<OtherTender> tenders){
        getStateModel().mTenders = tenders;
    }*/

    public void getAllTendersFromServer(){
        serviceAPI api = Controller.getGsonAPI();
        api.getTenders().enqueue(new retrofit2.Callback<List<OtherTender>>() {
            @Override
            public void onResponse(Call<List<OtherTender>> call, Response<List<OtherTender>> response) {
                getStateModel().getTenders().clear();

                ActiveAndroid.beginTransaction();
                try {
                    for (OtherTender otherTender : response.body()) {
                        otherTender.getTasks().save();
                        otherTender.getTender().save();
                        otherTender.getUserinfo().save();
                        if(otherTender.getOffer() != null)
                            otherTender.getOffer().save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                }catch(Exception e){
                    e.printStackTrace();
                }
                finally {
                    ActiveAndroid.endTransaction();
                }

                //getStateModel().getTenders().addAll(response.body());

                List<Tender> tenders = new Select().from(Tender.class).where("dateEnd > ?",
                        Tender.mFormatFromServer.format(new Date())).orderBy("dateEnd").execute();
                //getStateModel().setTenders(new ArrayList<OtherTender>());
                OtherTender otherTender;
                for (Tender tender : tenders) {
                    otherTender = new OtherTender();
                    otherTender.setTender(tender);
                    otherTender.setTasks(new Select().from(Tasks.class).where("taskId = ?", otherTender.getTender().getTaskId())
                            .<Tasks>executeSingle());
                    otherTender.setUserinfo(new Select().from(Userinfo.class).where("userId = ?", otherTender.getTasks().getUserId())
                            .<Userinfo>executeSingle());

                    if (otherTender.getUserinfo().getUserId() != DataStore.getUserId(getView().getContext().getApplicationContext())
                            && otherTender.getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH))
                        getStateModel().getTenders().add(otherTender);
                }

                getStateModel().setDataUpdated(true);
                getStateModel().setRefreshingEnd(true);
                sendStateModel();
            }

            @Override
            public void onFailure(Call<List<OtherTender>> call, Throwable t) {
                try{
                    Toast.makeText(getView().getContext(), R.string.lost_connection,Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                }
                getStateModel().setRefreshingEnd(true);
                sendStateModel();
            }
        });
    }
}
