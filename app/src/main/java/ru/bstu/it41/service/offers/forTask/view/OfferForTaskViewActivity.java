package ru.bstu.it41.service.offers.forTask.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.query.Select;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.offers.forTask.OffersForTaskActivity;
import ru.bstu.it41.service.offers.forTask.OffersForTaskFragment;

public class OfferForTaskViewActivity extends AppCompatActivity {

    public static String OFFER_ID_KEY = "ru.bstu.it41.service.offers.forTask.view.activity_view_offer_id";

    public static Intent getIntent(Context packageContext, int offerId) {
        Intent intent = new Intent(packageContext, OfferForTaskViewActivity.class);
        intent.putExtra(OFFER_ID_KEY, offerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_for_task_view);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_offer_for_task_view);
        if (fragment == null) {
            fragment = OffersForTaskViewFragment.getInstance(getIntent().getIntExtra(OFFER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_offer_for_task_view, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();

        if(intent!= null){
            Offer offer = new Select().from(Offer.class).where("offerId = ?",
                    getIntent().getIntExtra(OFFER_ID_KEY, -1)).executeSingle();
            intent.putExtra(OffersForTaskActivity.TENDER_ID_KEY,offer.getTenderId());
        }
        return intent;
    }
}
