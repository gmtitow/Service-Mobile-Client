package ru.bstu.it41.service.profile.review;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
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
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewFragmentPresenter extends ReampPresenter<ReviewFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }

    public void updateReviews(){
        getStateModel().getReviewAndUserinfos().clear();
        List<Reviews> reviews = new Select().from(Reviews.class).where("userIdObject = ?",
                getStateModel().getCurrentUserId()).orderBy("reviewDate DESC").execute();

        ReviewAndUserinfo reviewAndUserinfo;
        for (Reviews review: reviews) {
            try {
                reviewAndUserinfo = new ReviewAndUserinfo();
                reviewAndUserinfo.setReviews(review);
                reviewAndUserinfo.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        review.getUserIdSubject()).<Userinfo>executeSingle());
                getStateModel().getReviewAndUserinfos().add(reviewAndUserinfo);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void downloadReviews(){
        serviceAPI itableAPI = Controller.getGsonAPI();
        getStateModel().setDownloadInProgress(true);
        itableAPI.getReviews(getStateModel().getCurrentUserId()).enqueue(new Callback<List<ReviewAndUserinfo>>() {
            @Override
            public void onResponse(Call<List<ReviewAndUserinfo>> call, Response<List<ReviewAndUserinfo>> response) {
                try {

                    //getStateModel().getReviewAndUserinfos().addAll(response.body());

                    ActiveAndroid.beginTransaction();

                    for (ReviewAndUserinfo reviewAndInfo:
                         response.body()) {
                        reviewAndInfo.getReviews().save();
                        reviewAndInfo.getUserinfo().save();
                    }

                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                    updateReviews();

                    getStateModel().setRefreshingEnd(true);

                    sendStateModel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAndUserinfo>> call, Throwable t) {
                Toast.makeText(getView().getContext(), R.string.profile_change_connection_error, Toast.LENGTH_SHORT).show();
                getStateModel().setRefreshingEnd(true);
                sendStateModel();
            }
        });
    }
}