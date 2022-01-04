package ru.bstu.it41.service.tasks.addTask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;

public class TaskAddActivity extends AppCompatActivity {

    public static Intent getIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TaskAddActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_task_add);
        if (fragment == null) {
            fragment = new TaskAddFragment();
            fm.beginTransaction().add(R.id.container_task_add, fragment).commit();
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = super.getSupportParentActivityIntent();
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_TASKS);
        return intent;
    }
}
