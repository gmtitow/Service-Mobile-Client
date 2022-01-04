package ru.bstu.it41.service.offers.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.query.Select;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.offers.forTask.OffersForTaskActivity;

public class OfferViewActivity extends AppCompatActivity {

    public static String OFFER_ID_KEY = "ru.bstu.it41.service.offers.forTask.view.activity_view_offer_id";

    public static Intent getIntent(Context packageContext, int offerId) {
        Intent intent = new Intent(packageContext, OfferViewActivity.class);
        intent.putExtra(OFFER_ID_KEY, offerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_view);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_offer_view);
        if (fragment == null) {
            fragment = OfferViewFragment.getInstance(getIntent().getIntExtra(OFFER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_offer_view, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT, FragmentRetainer.FRAGMENT_OFFERS);
        intent.putExtra(OFFER_ID_KEY,getIntent().getIntExtra(OFFER_ID_KEY,-1));
        return intent;
    }
}
