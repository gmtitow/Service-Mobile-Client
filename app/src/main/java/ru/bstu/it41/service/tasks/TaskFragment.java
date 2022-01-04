package ru.bstu.it41.service.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.tasks.addTask.TaskAddActivity;
import ru.bstu.it41.service.tasks.addTask.TaskAddFragment;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskFragment extends ReampFragment<TaskFragmentPresenter, TaskFragmentState> {
    //Элементы управления
    private int currentTitle = R.string.title_tasks;

    private static String EXTRA_DOWNLOAD_FROM_DB = "ru.bstu.it41.service.tasks_download_from_db";

    private static String EXTRA_EXECUTED = "ru.bstu.it41.service.tasks_executed";

    //private ProgressBar mProgressBar;
    private TextView mTextListEmpty;
    private RecyclerView mRecycleTasks;
    FloatingActionButton mFabAdd;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TaskAdapter mTaskAdapter;

    @Override
    public void onStateChanged(TaskFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        if (!stateModel.isDownloaded()) {
            stateModel.setDownloaded(true);
            getPresenter().downloadTasks(stateModel.getTaskAndTenders().size() != 0);
        }

        if (stateModel.isDownloadInProgress()) {
            mSwipeRefreshLayout.setRefreshing(true);
            stateModel.setDownloadInProgress(false);
        }

        if (stateModel.isShowMessage()) {
            stateModel.setShowMessage(false);
        }

        if (stateModel.getTaskAndTenders().size() == 0)
            mTextListEmpty.setVisibility(View.VISIBLE);
        else
            mTextListEmpty.setVisibility(View.GONE);

        //mTaskAdapter.notifyDataSetChanged();
        mTaskAdapter.notifyDataSetChanged();

        if (stateModel.isRefreshingEnd()) {
            stateModel.setRefreshingEnd(false);
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }

    @Override
    public ReampPresenter<TaskFragmentState> onCreatePresenter() {
        TaskFragmentPresenter presenter = new TaskFragmentPresenter();
        return presenter;
    }

    @Override
    public TaskFragmentState onCreateStateModel() {
        TaskFragmentState state = new TaskFragmentState();
        int userId = DataStore.getUserId(getContext().getApplicationContext());

        if (getArguments() != null && getArguments().containsKey(EXTRA_EXECUTED)) {
            state.setExecuted(getArguments().getBoolean(EXTRA_EXECUTED));
        }

        if(state.isExecuted()){
            try {
                if (userId != -1) {
                    List<Tasks> tasks = new Select().from(Tasks.class).where(
                            "userId = ? and status != \"Поиск\"",
                            userId).orderBy("deadline DESC, status ASC").execute();
                    TaskAndTender taskAndTender;
                    for (Tasks task : tasks) {
                        taskAndTender = new TaskAndTender();
                        taskAndTender.setTasks(task);
                        taskAndTender.setTender((Tender) (new Select().from(Tender.class).where("taskId = ?", task.getTaskId()).executeSingle()));
                        state.getTaskAndTenders().add(taskAndTender);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            try {
                if (userId != -1) {
                    List<Tasks> tasks = new Select().from(Tasks.class).where(
                            "userId = ? and status = \"Поиск\"",
                            userId).orderBy("deadline DESC").execute();
                    TaskAndTender taskAndTender;
                    for (Tasks task : tasks) {
                        taskAndTender = new TaskAndTender();
                        taskAndTender.setTasks(task);
                        taskAndTender.setTender((Tender) (new Select().from(Tender.class).where("taskId = ?", task.getTaskId()).executeSingle()));
                        state.getTaskAndTenders().add(taskAndTender);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().updateTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mProgressBar = view.findViewById(R.id.progress);
        mTextListEmpty = view.findViewById(R.id.text_list_empty);
        mRecycleTasks = view.findViewById(R.id.recycle_view_tasks);

        mRecycleTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskAdapter = new TaskAdapter(getPresenter(), getContext());
        mRecycleTasks.setAdapter(mTaskAdapter);


        mFabAdd = view.findViewById(R.id.fab_add_task);

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TaskAddActivity.getIntent(getContext());
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().downloadTasks(false);
            }
        });
    }

    static public TaskFragment getInstance() {
        TaskFragment taskFragment = new TaskFragment();

        /*Bundle bundle = new Bundle();
        bundle.putBoolean();*/
        return taskFragment;
    }

    static public TaskFragment getInstanceExecuted() {
        TaskFragment taskFragment = new TaskFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_EXECUTED, true);
        taskFragment.setArguments(bundle);
        return taskFragment;
    }
}
