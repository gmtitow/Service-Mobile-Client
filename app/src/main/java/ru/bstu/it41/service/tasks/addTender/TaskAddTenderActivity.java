package ru.bstu.it41.service.tasks.addTender;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.tasks.addTask.TaskAddActivity;
import ru.bstu.it41.service.tasks.view.TaskViewActivity;

public class TaskAddTenderActivity extends AppCompatActivity {

    private static String TASK_ID_KEY = "ru.bstu.it41.service.activity_view_task_id";

    public static Intent getIntent(Context packageContext, int taskId) {
        Intent intent = new Intent(packageContext, TaskAddTenderActivity.class);
        intent.putExtra(TASK_ID_KEY, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add_tender);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_task_add_tender);
        if (fragment == null) {
            if(getParent() instanceof TaskAddActivity) {
                setResult(RESULT_OK);
            }
            fragment = TaskAddTenderFragment.getInstance(getIntent().getIntExtra(TASK_ID_KEY, 0));
            fm.beginTransaction().add(R.id.container_task_add_tender, fragment).commit();
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
