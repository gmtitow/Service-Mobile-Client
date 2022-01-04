package ru.bstu.it41.service.offers.forTask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.query.Select;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.tasks.view.TaskViewActivity;

public class OffersForTaskActivity extends AppCompatActivity {

    public static String TENDER_ID_KEY = "ru.bstu.it41.service.offers.forTask.activity_offers_for_task_tender_id";

    public static Intent getIntent(Context packageContext, int tenderID) {
        Intent intent = new Intent(packageContext, OffersForTaskActivity.class);
        intent.putExtra(TENDER_ID_KEY, tenderID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_for_task);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_offers_for_task);
        if (fragment == null) {
            fragment = OffersForTaskFragment.getInstance(getIntent().getIntExtra(TENDER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_offers_for_task, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        int tenderId = getIntent().getIntExtra(TENDER_ID_KEY, -1);
        Tender tender = new Select().from(Tender.class).where("tenderId = ?",tenderId).executeSingle();
        Intent intent = super.getSupportParentActivityIntent();

        intent.putExtra(TaskViewActivity.TASK_ID_KEY,tender.getTaskId());

        return intent;
    }
}
