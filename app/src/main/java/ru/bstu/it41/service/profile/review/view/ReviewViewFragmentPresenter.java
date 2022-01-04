package ru.bstu.it41.service.profile.review.view;

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
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.profile.review.ReviewFragmentState;
import ru.bstu.it41.service.serviceAPI;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewViewFragmentPresenter extends ReampPresenter<ReviewViewFragmentState> implements Serializable {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();

        sendStateModel();
    }
}