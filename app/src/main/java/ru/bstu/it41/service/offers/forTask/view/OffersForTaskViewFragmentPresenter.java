package ru.bstu.it41.service.offers.forTask.view;

import android.content.Intent;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.OffersWithUserinfo;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.offers.forTask.OffersForTaskFragmentState;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskViewFragmentPresenter extends ReampPresenter<OffersForTaskViewFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void selectOffer(){
        getStateModel().setProgress(true);
        sendStateModel();
        serviceAPI itableAPI = Controller.getGsonAPI();
        itableAPI.selectOffer(getStateModel().getCurrentOffer().getOffer().getTenderId(),
                getStateModel().getCurrentOffer().getOffer().getOfferId())
                .enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        try {
                            if(response.body().getStatus().equals(UserRequest.STATUS_OK)){

                                getStateModel().getCurrentOffer().getOffer().setSelected(1);
                                getStateModel().getCurrentOffer().getOffer().save();

                                Tender tender = new Select().from(Tender.class).
                                        where("tenderId = ?",getStateModel().getCurrentOffer().getOffer().getTenderId()).executeSingle();
                                Tasks task = new Select().from(Tasks.class).
                                        where("taskId = ?",tender.getTaskId()).executeSingle();

                                task.setStatus(Tasks.Status.STATUS_EXECUTING);
                                task.save();

                                Intent intent = new Intent(getView().getContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_COORDINATION);
                                intent.putExtra(ChatFragment.EXTRA_TENDER_ID,getStateModel().getCurrentOffer().getOffer().getTenderId());
                                intent.putExtra(ChatFragment.EXTRA_IS_CLIENT,true);

                                getView().getContext().startActivity(intent);
                                ((OfferForTaskViewActivity)getView().getContext()).finish();
                            }
                            else{
                                Toast.makeText(getView().getContext(), R.string.toast_offer_select_error, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getView().getContext(), R.string.offer_download_error, Toast.LENGTH_SHORT).show();
                        }
                        getStateModel().setProgress(false);
                        sendStateModel();
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                        Toast.makeText(getView().getContext(), R.string.lost_connection, Toast.LENGTH_SHORT).show();

                        getStateModel().setProgress(false);
                        sendStateModel();
                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();
                    }
                });
    }
}