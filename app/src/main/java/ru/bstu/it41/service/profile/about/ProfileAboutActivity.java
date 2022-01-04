package ru.bstu.it41.service.profile.about;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ru.bstu.it41.service.R;

public class ProfileAboutActivity extends AppCompatActivity {

    public static String USER_ID_KEY = "ru.bstu.it41.service.profile.about.activity_user_id";

    public static Intent getIntent(Context packageContext, int userId) {
        Intent intent = new Intent(packageContext, ProfileAboutActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_about);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_profile_about);
        if (fragment == null) {
            fragment = ProfileAboutFragment.getInstance(getIntent().getIntExtra(USER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_profile_about, fragment).commit();
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
