package ru.bstu.it41.service.tasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.offers.forTask.OffersForTaskActivity;
import ru.bstu.it41.service.tasks.DialogTaskDeleteFragment;
import ru.bstu.it41.service.tasks.addTender.TaskAddTenderActivity;
import ru.bstu.it41.service.tasks.change.TaskChangeActivity;
import ru.bstu.it41.service.tasks.map.ChooseLocationActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskViewFragment extends ReampFragment<TaskViewFragmentPresenter, TaskViewFragmentState> {
    private int currentTitle = R.string.title_tasks;
    private int tenderDeletingTitle = R.string.title_tender_deleting;
    private int taskDeletingTitle = R.string.title_task_deleting;
    //Элементы управления
    private static String TASK_ID_KEY = "ru.bstu.it41.service.tasks_task_id";
    private static final int REQUEST_DELETE_TASK = 103433;
    private static final int REQUEST_CHANGE_TASK = 9234;
    private static final String DIALOG_DELETE_TASK = "DialogDeleteTask";

    //private ProgressBar mProgressBar;
    private TextView mTextName;
    private TextView mTextCategory;
    private TextView mTextDeadline;
    private TextView mTextPrice;
    private TextView mTextDescription;
    private TextView mTextStatus;
    private TextView mTextTenderDuration;
    private Button mButtonOffers;
    private LinearLayout mLinearTenderIsntCreated;
    private LinearLayout mLinearTenderCreated;
    private Button mButtonCreateTender;
    private Button mButtonChangeTask;
    private Button mButtonDeleteTender;
    private Button mButtonGoToChat;
    private TextView mTextAddress;

    private Button mButtonToMap;

    private TextView mTextTender;

    MenuItem mButtonDeleteTask;

    TaskViewFragment mThisFragment = this;

    @Override
    public void onStateChanged(TaskViewFragmentState stateModel) {

        updateTask();

        /*if(stateModel.getCurrentTaskAndTender().getTender()==null){
            Tender tender = new Select().from(Tender.class).
                    where("taskId = ?",stateModel.getCurrentTaskAndTender().getTasks().getTaskId()).
                    executeSingle();

            if(tender!=null){
                stateModel.getCurrentTaskAndTender().setTender(tender);
            }
        }*/

        if(stateModel.isTaskDelete())
            getActivity().setTitle(taskDeletingTitle);
        else if(stateModel.isTenderDelete())
            getActivity().setTitle(tenderDeletingTitle);
        else
            getActivity().setTitle(currentTitle);
        mTextName.setText(stateModel.getCurrentTaskAndTender().getTasks().getName());

        Category category = new Select().from(Category.class)
                .where("categoryId = ?",stateModel.getCurrentTaskAndTender().getTasks().getCategoryId())
                .executeSingle();

        if(category!= null)
            mTextCategory.setText(category.getCategoryName());

        mTextStatus.setText(stateModel.getCurrentTaskAndTender().getTasks().getStatus());
        mTextDescription.setText(stateModel.getCurrentTaskAndTender().getTasks().getDescription());
        mTextAddress.setText(stateModel.getCurrentTaskAndTender().getTasks().getAddress());

        if(stateModel.getCurrentTaskAndTender().getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH)) {

            mTextPrice.setText(String.valueOf(stateModel.getCurrentTaskAndTender().getTasks().getPrice()));
            mTextDeadline.setText(getPresenter().mFormatForDate.format(stateModel.getCurrentTaskAndTender().getTasks().getDeadline()));

            if (stateModel.getCurrentTaskAndTender().getTender() != null) {
                mLinearTenderCreated.setVisibility(View.VISIBLE);
                mLinearTenderIsntCreated.setVisibility(View.GONE);
                mTextTenderDuration.setText(((stateModel.getCurrentTaskAndTender().getTender().getDateStart() != null ?
                        getPresenter().mFormatForDate.format(stateModel.getCurrentTaskAndTender().getTender().getDateStart()) : "null") +
                        " - " + (stateModel.getCurrentTaskAndTender().getTender().getDateEnd() != null ?
                        getPresenter().mFormatForDate.format(stateModel.getCurrentTaskAndTender().getTender().getDateEnd()) : "null")));
                mButtonOffers.setText(getString(R.string.task_offers, String.valueOf(stateModel.getCurrentTaskAndTender().getOffersCount())));

                if (stateModel.getCurrentTaskAndTender().getOffersCount() == 0)
                    mButtonOffers.setEnabled(false);
            } else {
                mLinearTenderCreated.setVisibility(View.GONE);
                mLinearTenderIsntCreated.setVisibility(View.VISIBLE);
            }

            mButtonDeleteTender.setVisibility(stateModel.getCurrentTaskAndTender().getOffersCount() == 0 ? View.VISIBLE : View.GONE);
            mButtonChangeTask.setEnabled(stateModel.getCurrentTaskAndTender().getOffersCount() == 0);

            mButtonDeleteTender.setEnabled(!stateModel.isTenderDelete() && !stateModel.isTaskDelete());
            mButtonCreateTender.setEnabled(!stateModel.isTenderDelete() && !stateModel.isTaskDelete());

            mButtonChangeTask.setEnabled(!stateModel.isTaskDelete());

            mButtonGoToChat.setVisibility(View.GONE);
            mTextTender.setVisibility(View.VISIBLE);

            //mButtonDeleteTask.setVisible(true);
        } else{

            if(stateModel.getSelectedOffer()!=null){
                mTextPrice.setText(String.valueOf(stateModel.getSelectedOffer().getPrice()));
                mTextDeadline.setText(getPresenter().mFormatForDate.format(stateModel.getSelectedOffer().getDeadline()));
            }else {
                mTextPrice.setText(String.valueOf(stateModel.getCurrentTaskAndTender().getTasks().getPrice()));
                mTextDeadline.setText(getPresenter().mFormatForDate.format(stateModel.getCurrentTaskAndTender().getTasks().getDeadline()));
            }
            mLinearTenderCreated.setVisibility(View.GONE);
            mLinearTenderIsntCreated.setVisibility(View.GONE);
            mButtonChangeTask.setVisibility(View.GONE);
            mButtonOffers.setVisibility(View.GONE);
            mButtonGoToChat.setVisibility(View.VISIBLE);
            mTextTender.setVisibility(View.GONE);
            //mButtonDeleteTask.setVisible(false);
        }

        if(mButtonDeleteTask!=null) {
            if (getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH)
                    && getPresenter().getStateModel().getCurrentTaskAndTender().getTender() == null) {
                mButtonDeleteTask.setVisible(true);
            } else {
                mButtonDeleteTask.setVisible(false);
            }
        }
    }
    @Override
    public ReampPresenter<TaskViewFragmentState> onCreatePresenter() {
        return new TaskViewFragmentPresenter();
    }

    @Override
    public TaskViewFragmentState onCreateStateModel() {
        return new TaskViewFragmentState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (this.getArguments() != null && this.getArguments().containsKey(TASK_ID_KEY))
        {
            Tasks task = new Select().from(Tasks.class).where("taskId  = ?",getArguments().getInt(TASK_ID_KEY))
                    .executeSingle();
            Tender tender = new Select().from(Tender.class).where("taskId = ?",task.getTaskId()).executeSingle();

            getPresenter().getStateModel().getCurrentTaskAndTender().setTasks(task);
            getPresenter().getStateModel().getCurrentTaskAndTender().setTender(tender);

            Offer offer = new Select().from(Offer.class).where("tenderId = ? and selected = 1",tender.getTenderId())
                    .executeSingle();

            if(offer!=null){
                getPresenter().getStateModel().setSelectedOffer(offer);
            }

            if(tender!=null) {
                getPresenter().getStateModel().getCurrentTaskAndTender().setOffersCount(
                        new Select().from(Offer.class).where("tenderId = ?", tender.getTenderId()).count()
                );
            }else{
                getPresenter().getStateModel().getCurrentTaskAndTender().setOffersCount(0);
            }
        }
        return inflater.inflate(R.layout.fragment_task_view, container, false);
    }

    private void updateTask(){
        Tasks task = new Select().from(Tasks.class).where("taskId  = ?",getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getTaskId())
                .executeSingle();
        Tender tender = new Select().from(Tender.class).where("taskId = ?",task.getTaskId()).executeSingle();

        getPresenter().getStateModel().getCurrentTaskAndTender().setTasks(task);
        getPresenter().getStateModel().getCurrentTaskAndTender().setTender(tender);

        if(tender!=null) {
            getPresenter().getStateModel().getCurrentTaskAndTender().setOffersCount(
                    new Select().from(Offer.class).where("tenderId = ?", tender.getTenderId()).count()
            );
        }else{
            getPresenter().getStateModel().getCurrentTaskAndTender().setOffersCount(0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextName = view.findViewById(R.id.text_name);
        mTextCategory = view.findViewById(R.id.text_category);
        mTextDeadline = view.findViewById(R.id.text_deadline);
        mTextPrice = view.findViewById(R.id.text_price);
        mTextDescription = view.findViewById(R.id.text_description);
        mTextStatus = view.findViewById(R.id.text_status);
        mLinearTenderCreated = view.findViewById(R.id.layout_tender_created);
        mTextTenderDuration = view.findViewById(R.id.text_tender_duration);
        mButtonOffers = view.findViewById(R.id.button_offers);
        mLinearTenderIsntCreated = view.findViewById(R.id.layout_tender_isnt_created);
        mButtonCreateTender = view.findViewById(R.id.button_add_tender);
        mButtonChangeTask = view.findViewById(R.id.button_change_task);
        mButtonDeleteTender = view.findViewById(R.id.button_delete_tender);
        mTextAddress = view.findViewById(R.id.text_tender_address);

        mButtonToMap = view.findViewById(R.id.button_to_map);

        mTextTender = view.findViewById(R.id.text_tender);

        mButtonGoToChat = view.findViewById(R.id.button_go_to_chat);

        mButtonGoToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_COORDINATION);
                intent.putExtra(ChatFragment.EXTRA_TENDER_ID,getPresenter().getStateModel().getCurrentTaskAndTender().getTender().getTenderId());
                intent.putExtra(ChatFragment.EXTRA_IS_CLIENT,true);

                startActivity(intent);
                getActivity().finish();
            }
        });

        mButtonDeleteTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().deleteTender();
            }
        });

        mButtonChangeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChangeTask(getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getTaskId());
            }
        });

        mButtonOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOffers(getPresenter().getStateModel().getCurrentTaskAndTender().getTender().getTenderId());
            }
        });

        mButtonCreateTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddTender(getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getTaskId());
            }
        });

        mButtonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMap();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DELETE_TASK) {
            if(resultCode == RESULT_OK){
                getPresenter().deleteTask();
            }
        } else if (requestCode == REQUEST_CHANGE_TASK) {
            if(resultCode == RESULT_OK){
                getPresenter().getStateModel().getCurrentTaskAndTender().setTasks(
                        new Select().from(Tasks.class).where("taskId = ?",
                                getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getTaskId()).<Tasks>executeSingle()
                );
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_view, menu);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mButtonDeleteTask = menu.findItem(R.id.menu_item_delete);
        if(getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH)
                && getPresenter().getStateModel().getCurrentTaskAndTender().getTender()==null){
            mButtonDeleteTask.setVisible(true);
        }
        else{
            mButtonDeleteTask.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                FragmentManager manager = getFragmentManager();
                DialogTaskDeleteFragment dialog = new DialogTaskDeleteFragment();
                dialog.setTargetFragment(mThisFragment, REQUEST_DELETE_TASK);
                dialog.show(manager, DIALOG_DELETE_TASK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static TaskViewFragment getInstance(int taskId){
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TASK_ID_KEY,taskId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void toChangeTask(int taskId){
        Intent intent = TaskChangeActivity.getIntent(getContext(),taskId);

        startActivityForResult(intent,REQUEST_CHANGE_TASK);
    }

    public void toOffers(int tenderId){
        Intent intent = OffersForTaskActivity.getIntent(getContext(),tenderId);
        startActivity(intent);
    }

    public void toAddTender(int taskId){
        Intent intent = TaskAddTenderActivity.getIntent(getContext(),taskId);
        startActivity(intent);
    }

    public void toMap(){
        Intent intent = ChooseLocationActivity.getIntent(getContext()
                ,getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getLatitude(),
                getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getLongitude(),
                getPresenter().getStateModel().getCurrentTaskAndTender().getTasks().getAddress(),
                true);
        startActivity(intent);
    }
}
