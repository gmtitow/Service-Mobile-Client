package ru.bstu.it41.service.offers.add;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.StatusOffer;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class OfferAddFragmentPresenter extends ReampPresenter<OfferAddFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
        sendStateModel();
    }

    public void addOffer() {
        getStateModel().setCreatingOffer(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();

        itableAPI.addOffer(getStateModel().getTempOffer().getTenderId(),
                getStateModel().getTempOffer().getDeadline(),
                getStateModel().getTempOffer().getPrice(),
                getStateModel().getTempOffer().getDescription()).
                enqueue(new Callback<StatusOffer>() {
                    @Override
                    public void onResponse(Call<StatusOffer> call, Response<StatusOffer> response) {
                        try {
                            if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                response.body().getOffer().save();
                                //getStateModel().getTempOffer().save();
                                Toast.makeText(getView().getContext(), R.string.offer_add_toast_success, Toast.LENGTH_SHORT).show();
                                ((OffersAddFragment)getView()).toOffers();
                            } else
                                Toast.makeText(getView().getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getStateModel().setCreatingOffer(false);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<StatusOffer> call, Throwable t) {
                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();
                        getStateModel().setCreatingOffer(false);
                        sendStateModel();
                    }
                });
    }

}