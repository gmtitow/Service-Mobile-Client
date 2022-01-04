package ru.bstu.it41.service.tasks.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;

public class TaskViewActivity extends AppCompatActivity {

    public static String TASK_ID_KEY = "ru.bstu.it41.service.activity_view_task_id";

    public static Intent getIntent(Context packageContext, int taskId) {
        Intent intent = new Intent(packageContext, TaskViewActivity.class);
        intent.putExtra(TASK_ID_KEY, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.container_task_view);
        if (fragment == null) {
            fragment = TaskViewFragment.getInstance(getIntent().getIntExtra(TASK_ID_KEY, 0));
            fm.beginTransaction().add(R.id.container_task_view, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT, FragmentRetainer.FRAGMENT_TASKS);
        intent.putExtra(TASK_ID_KEY,getIntent().getIntExtra(TASK_ID_KEY,-1));
        return intent;
    }
}
