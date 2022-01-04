package ru.bstu.it41.service.offers;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.tasks.TaskAdapter;
import ru.bstu.it41.service.tasks.TaskFragmentPresenter;
import ru.bstu.it41.service.tasks.TaskFragmentState;
import ru.bstu.it41.service.tasks.addTask.TaskAddActivity;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersFragment extends ReampFragment<OffersFragmentPresenter, OffersFragmentState> {
    //Элементы управления
    private int currentTitle = R.string.title_offers;

    private static String EXTRA_DOWNLOAD_FROM_DB = "ru.bstu.it41.service.tasks_download_from_db";
    private static String EXTRA_SELECTED = "ru.bstu.it41.service.offers_selected";

    //private ProgressBar mProgressBar;
    private TextView mTextListEmpty;
    private RecyclerView mRecycleTasks;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OffersAdapter mOffersAdapter;

    @Override
    public void onStateChanged(OffersFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        if (stateModel.isNeedDownload()) {
            stateModel.setNeedDownload(false);
            getPresenter().downloadOffers();

        }

        mSwipeRefreshLayout.setRefreshing(stateModel.isDownloadInProgress() &&
                stateModel.getOffersWithTask().size() == 0);


        if (stateModel.getOffersWithTask().size() == 0)
            mTextListEmpty.setVisibility(View.VISIBLE);
        else
            mTextListEmpty.setVisibility(View.GONE);

        //mTaskAdapter.notifyDataSetChanged();
        mOffersAdapter.notifyDataSetChanged();

        /*if (stateModel.isRefreshingEnd()) {
            stateModel.setRefreshingEnd(false);
            mSwipeRefreshLayout.setRefreshing(false);
        }*/
    }

    @Override
    public ReampPresenter<OffersFragmentState> onCreatePresenter() {
        OffersFragmentPresenter presenter = new OffersFragmentPresenter();
        return new OffersFragmentPresenter();
    }

    @Override
    public OffersFragmentState onCreateStateModel() {
        OffersFragmentState state = new OffersFragmentState();

        if (getArguments() != null && getArguments().containsKey(EXTRA_SELECTED)) {
            state.setSelected(getArguments().getBoolean(EXTRA_SELECTED));
        }

        if (state.isSelected()) {
            List<Offer> offers = new Select().from(Offer.class).where("userId = ? and selected = 1",
                    DataStore.getUserId(getContext().getApplicationContext())).orderBy("deadline DESC").execute();

            OfferWithTask offerWithTask = new OfferWithTask();
            state.setOffersWithTask(new ArrayList<OfferWithTask>());

            for (Offer offer : offers) {
                offerWithTask.setOffer(offer);
                offerWithTask.setTender(new Select().from(Tender.class).where("tenderId = ?",
                        offer.getTenderId()).<Tender>executeSingle());
                offerWithTask.setTasks(new Select().from(Tasks.class).where("taskId = ?",
                        offerWithTask.getTender().getTaskId()).<Tasks>executeSingle());
                offerWithTask.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        offerWithTask.getTasks().getUserId()).<Userinfo>executeSingle());

                state.getOffersWithTask().add(offerWithTask);
            }
        } else {
            List<Offer> offers = new Select().from(Offer.class).where("userId = ? and selected = 0",
                    DataStore.getUserId(getContext().getApplicationContext())).orderBy("deadline").execute();

            OfferWithTask offerWithTask = new OfferWithTask();
            state.setOffersWithTask(new ArrayList<OfferWithTask>());

            for (Offer offer : offers) {
                offerWithTask.setOffer(offer);
                offerWithTask.setTender(new Select().from(Tender.class).where("tenderId = ?",
                        offer.getTenderId()).<Tender>executeSingle());
                offerWithTask.setTasks(new Select().from(Tasks.class).where("taskId = ?",
                        offerWithTask.getTender().getTaskId()).<Tasks>executeSingle());
                offerWithTask.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        offerWithTask.getTasks().getUserId()).<Userinfo>executeSingle());

                state.getOffersWithTask().add(offerWithTask);
            }
        }
        state.setNeedDownload(true);

        return state;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().updateOffers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mProgressBar = view.findViewById(R.id.progress);
        mTextListEmpty = view.findViewById(R.id.text_list_empty);
        mRecycleTasks = view.findViewById(R.id.recycle_offers);

        mRecycleTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        mOffersAdapter = new OffersAdapter(getPresenter(), getContext());
        mRecycleTasks.setAdapter(mOffersAdapter);


        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().downloadOffers();
            }
        });
    }

    static public OffersFragment getInstanceExecuted() {
        OffersFragment offersFragment = new OffersFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_SELECTED, true);
        offersFragment.setArguments(bundle);
        return offersFragment;
    }
}
