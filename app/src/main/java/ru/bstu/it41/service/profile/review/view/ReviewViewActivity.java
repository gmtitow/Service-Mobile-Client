package ru.bstu.it41.service.profile.review.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.activeandroid.query.Select;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.profile.review.ReviewsActivity;

public class ReviewViewActivity extends AppCompatActivity {

    public static String REVIEW_ID_KEY = "ru.bstu.it41.service.profile.review.view.activity_view_review_id";

    public static Intent getIntent(Context packageContext, int reviewId) {
        Intent intent = new Intent(packageContext, ReviewViewActivity.class);
        intent.putExtra(REVIEW_ID_KEY, reviewId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_view);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_review_view);
        if (fragment == null) {
            fragment = ReviewViewFragment.getInstance(getIntent().getIntExtra(REVIEW_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_review_view, fragment).commit();
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
