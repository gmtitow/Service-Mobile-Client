package ru.bstu.it41.service.profile.review.add;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.query.Select;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.tasks.view.TaskViewFragment;

public class ReviewAddActivity extends AppCompatActivity {

    public static String TENDER_ID_KEY = "ru.bstu.it41.service.profile.review.add.activity_add_review_tender_id";
    public static String USER_ID_KEY = "ru.bstu.it41.service.profile.review.add.activity_add_review_user_id";

    public static Intent getIntent(Context packageContext, int tenderId, int userId) {
        Intent intent = new Intent(packageContext, ReviewAddActivity.class);
        intent.putExtra(TENDER_ID_KEY, tenderId);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_review_add);
        if (fragment == null) {
            fragment = ReviewAddFragment.getInstance(getIntent().getIntExtra(USER_ID_KEY, -1),
                    getIntent().getIntExtra(TENDER_ID_KEY, -1));
            fm.beginTransaction().add(R.id.container_review_add, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT, FragmentRetainer.FRAGMENT_COORDINATION);
        intent.putExtra(ChatFragment.EXTRA_TENDER_ID,getIntent().getIntExtra(TENDER_ID_KEY, -1));

        //
        Tender tender = new Select().from(Tender.class).where("tenderId = ?",
                getIntent().getIntExtra(TENDER_ID_KEY, -1)).<Tender>executeSingle();

        Tasks task = new Select().from(Tasks.class).where("taskId = ?",
                tender.getTaskId()).<Tasks>executeSingle();

        boolean isClient;

        if(task.getUserId() == DataStore.getUserId(this.getApplicationContext()))
            isClient = true;
        else
            isClient = false;
        //

        intent.putExtra(ChatFragment.EXTRA_IS_CLIENT,isClient);
        return intent;
    }
}
