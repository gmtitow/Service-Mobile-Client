package ru.bstu.it41.service.offers;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.OffersWithUserinfo;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.TaskAndTenderWithOffers;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.serviceAPI;
import ru.bstu.it41.service.tasks.TaskFragmentState;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersFragmentPresenter extends ReampPresenter<OffersFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public void updateOffers(){

        getStateModel().getOffersWithTask().clear();

        if (getStateModel().isSelected()) {
            List<Offer> offers = new Select().from(Offer.class).where("userId = ? and selected = 1",
                    DataStore.getUserId(getView().getContext().getApplicationContext())).orderBy("deadline DESC").execute();

            OfferWithTask offerWithTask;

            for (Offer offer : offers) {
                offerWithTask = new OfferWithTask();
                offerWithTask.setOffer(offer);
                offerWithTask.setTender(new Select().from(Tender.class).where("tenderId = ?",
                        offer.getTenderId()).<Tender>executeSingle());
                offerWithTask.setTasks(new Select().from(Tasks.class).where("taskId = ?",
                        offerWithTask.getTender().getTaskId()).<Tasks>executeSingle());
                offerWithTask.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        offerWithTask.getTasks().getUserId()).<Userinfo>executeSingle());

                getStateModel().getOffersWithTask().add(offerWithTask);
            }
        } else {
            List<Offer> offers = new Select().from(Offer.class).where("userId = ? and selected = 0",
                    DataStore.getUserId(getView().getContext().getApplicationContext())).orderBy("deadline").execute();

            OfferWithTask offerWithTask;

            for (Offer offer : offers) {
                offerWithTask = new OfferWithTask();
                offerWithTask.setOffer(offer);
                offerWithTask.setTender(new Select().from(Tender.class).where("tenderId = ?",
                        offer.getTenderId()).<Tender>executeSingle());
                offerWithTask.setTasks(new Select().from(Tasks.class).where("taskId = ?",
                        offerWithTask.getTender().getTaskId()).<Tasks>executeSingle());
                offerWithTask.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        offerWithTask.getTasks().getUserId()).<Userinfo>executeSingle());

                getStateModel().getOffersWithTask().add(offerWithTask);
            }
        }

        sendStateModel();
    }

    public void downloadOffers(){
        getStateModel().setDownloadInProgress(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.getOffersForUser()
                .enqueue(new Callback<List<OfferWithTask>>() {
                    @Override
                    public void onResponse(Call<List<OfferWithTask>> call, Response<List<OfferWithTask>> response) {
                        try {
                            getStateModel().getOffersWithTask().clear();
                            getStateModel().getOffersWithTask().addAll(response.body());

                            new Delete().from(Offer.class).where("userId = ?",DataStore.getUserId(getView().getContext().getApplicationContext())).execute();
                            ActiveAndroid.beginTransaction();
                            for(OfferWithTask offer : response.body()){
                                offer.getTasks().save();
                                offer.getOffer().save();
                                offer.getTender().save();
                                offer.getUserinfo().save();
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();

                            updateOffers();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getView().getContext(), R.string.offer_download_error, Toast.LENGTH_SHORT).show();
                        }

                        getStateModel().setDownloadInProgress(false);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<List<OfferWithTask>> call, Throwable t) {

                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();

                        getStateModel().setDownloadInProgress(false);
                        sendStateModel();
                    }
                });
    }
}