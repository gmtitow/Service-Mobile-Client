package ru.bstu.it41.service.offers.forTask;

import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.OffersWithUserinfo;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskFragmentPresenter extends ReampPresenter<OffersForTaskFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void downloadOffers(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.getOffers(getStateModel().getCurrentTenderId())
                .enqueue(new Callback<OffersWithUserinfo>() {
            @Override
            public void onResponse(Call<OffersWithUserinfo> call, Response<OffersWithUserinfo> response) {
                try {
                    if(response.body().getStatus().getStatus().equals(UserRequest.STATUS_OK)){
                        getStateModel().getOffersWithUser().clear();
                        if(response.body().getOfferWithUser()!= null) {
                            getStateModel().getOffersWithUser().addAll(response.body().getOfferWithUser());
                            for (OfferWithUser offer:response.body().getOfferWithUser()) {
                                offer.getOffer().save();
                                offer.getUserinfo().save();
                            }
                        }
                    }
                    else{
                        Toast.makeText(getView().getContext(), R.string.offer_download_error, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getView().getContext(), R.string.offer_download_error, Toast.LENGTH_SHORT).show();
                }
                getStateModel().setRefreshingEnd(true);
                sendStateModel();
            }

            @Override
            public void onFailure(Call<OffersWithUserinfo> call, Throwable t) {

                Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();

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