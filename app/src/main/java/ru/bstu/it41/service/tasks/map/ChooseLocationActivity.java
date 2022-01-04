package ru.bstu.it41.service.tasks.map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.bstu.it41.service.R;

import static ru.bstu.it41.service.tasks.map.ChooseLocationFragment.EXTRA_ADDRESS;
import static ru.bstu.it41.service.tasks.map.ChooseLocationFragment.EXTRA_CHANGE;
import static ru.bstu.it41.service.tasks.map.ChooseLocationFragment.EXTRA_LATITUDE;
import static ru.bstu.it41.service.tasks.map.ChooseLocationFragment.EXTRA_LONGITUDE;

public class ChooseLocationActivity extends AppCompatActivity {
    public static final String EXTRA_INTENT = "ru.bstu.it41.service.tasks.map.choice_activity";

    static public Intent getIntent(Context packageContext, Double latitude, Double longitude,String address){
        Intent intent = new Intent(packageContext,ChooseLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LONGITUDE, longitude);
        bundle.putSerializable(EXTRA_LATITUDE, latitude);
        bundle.putSerializable(EXTRA_ADDRESS, address);
        intent.putExtra(EXTRA_INTENT,bundle);
        return intent;
    }

    static public Intent getIntent(Context packageContext, Double latitude, Double longitude,String address,boolean nochange){
        Intent intent = new Intent(packageContext,ChooseLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_LONGITUDE, longitude);
        bundle.putSerializable(EXTRA_LATITUDE, latitude);
        bundle.putSerializable(EXTRA_ADDRESS, address);
        bundle.putBoolean(EXTRA_CHANGE, nochange);
        intent.putExtra(EXTRA_INTENT,bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_choose_location);
        if (fragment == null) {
            if(getIntent().getBundleExtra(EXTRA_INTENT)!=null)
                fragment = ChooseLocationFragment.getInstance(getIntent().getBundleExtra(EXTRA_INTENT));
            else
                fragment = new ChooseLocationFragment();

            fm.beginTransaction().add(R.id.container_choose_location, fragment).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
