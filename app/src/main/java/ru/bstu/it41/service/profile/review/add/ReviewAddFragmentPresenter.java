package ru.bstu.it41.service.profile.review.add;

import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Status;
import ru.bstu.it41.service.profile.review.ReviewFragmentState;
import ru.bstu.it41.service.serviceAPI;

import static ru.bstu.it41.service.any.UserRequest.STATUS_OK;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewAddFragmentPresenter extends ReampPresenter<ReviewAddFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        //sendStateModel();
    }

    public void addReview(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        getStateModel().setDownloadInProgress(true);
        itableAPI.addReview(getStateModel().getTenderId(),
                getStateModel().getReview().getRaiting(),
                getStateModel().getReview().getTextReview()
        ).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.body().getStatus().equals(STATUS_OK)) {
                    Intent intent = new Intent(getView().getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_PROFILE);

                    getView().getContext().startActivity(intent);
                    ((ReviewAddActivity)getView().getContext()).finish();
                }
                else{
                    Toast.makeText(getView().getContext(), R.string.toast_problem_with_adding, Toast.LENGTH_SHORT).show();
                }
                getStateModel().setDownloadInProgress(false);
                sendStateModel();
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                getStateModel().setDownloadInProgress(false);
                sendStateModel();
            }
        });
    }
}