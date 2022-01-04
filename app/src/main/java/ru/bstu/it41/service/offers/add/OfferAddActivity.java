package ru.bstu.it41.service.offers.add;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ru.bstu.it41.service.R;

public class OfferAddActivity extends AppCompatActivity {

    public static String TENDER_ID_KEY = "ru.bstu.it41.service.tenders.tenderView.activity_view_tender_id";

    public static Intent getIntent(Context packageContext, int tenderId) {
        Intent intent = new Intent(packageContext, OfferAddActivity.class);
        intent.putExtra(TENDER_ID_KEY, tenderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_add);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_offer_add);
        if (fragment == null) {
            fragment = OffersAddFragment.getInstance(getIntent().getIntExtra(TENDER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_offer_add, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
