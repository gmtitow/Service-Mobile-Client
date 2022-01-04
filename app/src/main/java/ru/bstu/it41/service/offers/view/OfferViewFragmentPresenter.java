package ru.bstu.it41.service.offers.view;

import android.app.Activity;
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
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class OfferViewFragmentPresenter extends ReampPresenter<OfferViewFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void deleteOffer(){
        getStateModel().setOfferDelete(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        try {
            itableAPI.deleteOffer(getStateModel().getCurrentOffer().getOffer().getOfferId()).
                    enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            try {
                                if (response.body().getStatus().equals(UserRequest.STATUS_OK)) {
                                    //getStateModel().getTaskAndTenders().remove(getStateModel().getCurrentTaskAndTender());
                                    getStateModel().getCurrentOffer().getOffer().delete();

                                    ((Activity)getView().getContext()).finish();
                                    Toast.makeText(getView().getContext(), R.string.offer_success_deleted, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getView().getContext(), R.string.offer_fail_delete, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            getStateModel().setOfferDelete(false);
                            sendStateModel();
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            Toast.makeText(getView().getContext(), R.string.offer_fail_delete, Toast.LENGTH_SHORT).show();
                            getStateModel().setOfferDelete(false);
                            sendStateModel();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}