package ru.bstu.it41.service.tasks.addTender;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.Calendar;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.tasks.TaskFragment;
import ru.bstu.it41.service.tasks.TaskFragmentPresenter;
import ru.bstu.it41.service.tasks.TaskFragmentState;
import ru.bstu.it41.service.tasks.view.TaskViewFragment;

import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddTenderFragment extends ReampFragment<TaskAddTenderFragmentPresenter, TaskAddTenderFragmentState> {

    private int currentTitle = R.string.title_tender_add;
    private int creatingTitle = R.string.title_tender_creating;
    //Элементы управления
    private static String TASK_ID_KEY = "ru.bstu.it41.service.tasks_task_id";

    private Button mButtonDateEnd;
    private Button mButtonAddTender;


    //Переменные
    Calendar mTemporaryDateEnd;
    //
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTemporaryDateEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTemporaryDateEnd.set(Calendar.MINUTE, minute);

            mTemporaryDateEnd.set(Calendar.SECOND, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.SECOND, 0);

            if (mTemporaryDateEnd.getTime().getTime() > today.getTime().getTime()) {
                if (mTemporaryDateEnd.getTime().getTime() < getPresenter().getStateModel().getCurrentTask().getDeadline()
                        .getTime()) {

                    getPresenter().getStateModel().setCurrentDateEndTender(mTemporaryDateEnd);
                    getPresenter().sendStateModel();
                } else
                    Toast.makeText(getContext(), R.string.task_date_end_error_deadline, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), R.string.task_date_end_error_today, Toast.LENGTH_SHORT).show();
        }
    };

    DatePickerDialog.OnDateSetListener mOnDateEndSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mTemporaryDateEnd = Calendar.getInstance();
            mTemporaryDateEnd.set(Calendar.YEAR, year);
            mTemporaryDateEnd.set(Calendar.MONTH, month);
            mTemporaryDateEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (getPresenter().getStateModel().getCurrentDateEndTender() != null)
                new TimePickerDialog(getContext(), mOnTimeSetListener,
                        getPresenter().getStateModel().getCurrentDateEndTender().get(Calendar.HOUR),
                        getPresenter().getStateModel().getCurrentDateEndTender().get(Calendar.MINUTE),
                        true)
                        .show();
            else {
                new TimePickerDialog(getContext(), mOnTimeSetListener,
                        12,
                        0,
                        true)
                        .show();
            }
        }

    };

    @Override
    public void onStateChanged(TaskAddTenderFragmentState stateModel) {
        if (stateModel.isCreatingInProgress()) {
            getActivity().setTitle(creatingTitle);
        } else
            getActivity().setTitle(currentTitle);
        mButtonDateEnd.setText(stateModel.getCurrentDateEndTender() != null ?
                getPresenter().mFormatForDate.format(stateModel.getCurrentDateEndTender().getTime())
                : "не указано");

        mButtonAddTender.setEnabled(stateModel.allowCreate());
    }

    @Override
    public ReampPresenter<TaskAddTenderFragmentState> onCreatePresenter() {
        return new TaskAddTenderFragmentPresenter();
    }

    @Override
    public TaskAddTenderFragmentState onCreateStateModel() {
        TaskAddTenderFragmentState state = new TaskAddTenderFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(TASK_ID_KEY)) {
            Tasks task = new Select().from(Tasks.class).where("taskId = ?",
                    getArguments().getInt(TASK_ID_KEY)).executeSingle();
            state.setCurrentTask(task);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_add_tender, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButtonAddTender = view.findViewById(R.id.button_add_task);

        mButtonDateEnd = view.findViewById(R.id.button_dateEnd);

        mButtonDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getStateModel().getCurrentDateEndTender() != null)
                    new DatePickerDialog(getContext(), mOnDateEndSetListener,
                            getPresenter().getStateModel().getCurrentDateEndTender().get(Calendar.YEAR),
                            getPresenter().getStateModel().getCurrentDateEndTender().get(Calendar.MONTH),
                            getPresenter().getStateModel().getCurrentDateEndTender().get(Calendar.DAY_OF_MONTH))
                            .show();
                else {
                    Calendar calendar = Calendar.getInstance();
                    new DatePickerDialog(getContext(), mOnDateEndSetListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            }
        });

        mButtonAddTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().addTender();
            }
        });
    }

    public void toTaskFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", getPresenter().getStateModel());
        bundle.putSerializable("presenter", getPresenter());

        ((MainActivity) getActivity()).replaceFragment(TaskFragment.class, bundle,
                ((MainActivity) getActivity()).STACK_LOCAL, R.id.container, true);
    }

    public void toTaskViewFragment(int taskId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", getPresenter().getStateModel());
        bundle.putSerializable("presenter", getPresenter());

        TaskViewFragment fragment = TaskViewFragment.getInstance(taskId);

        ((MainActivity) getActivity()).replaceFragment(fragment, bundle,
                ((MainActivity) getActivity()).STACK_LOCAL, R.id.container, true);
    }

    public static TaskAddTenderFragment getInstance(int taskId) {
        TaskAddTenderFragment fragment = new TaskAddTenderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TASK_ID_KEY, taskId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
