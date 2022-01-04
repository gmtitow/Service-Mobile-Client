package ru.bstu.it41.service.tenders.tenderView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampPresenter;
import retrofit2.Call;
import retrofit2.Response;
import ru.bstu.it41.service.Controller;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.serviceAPI;
import ru.bstu.it41.service.tenders.TenderFragmentState;


/**
 * Created by Герман on 16.11.2017.
 */

public class TenderViewFragmentPresenter extends ReampPresenter<TenderViewFragmentState> {

    public SimpleDateFormat mFormatForDate = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    @Override
    public void onPresenterCreated() {
        super.onPresenterCreated();
    }

}
