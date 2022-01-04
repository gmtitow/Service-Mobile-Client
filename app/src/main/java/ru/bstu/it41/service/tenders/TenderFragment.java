package ru.bstu.it41.service.tenders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.tenders.tendersMap.TendersMapFragment;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderFragment extends ReampFragment<TenderFragmentPresenter, TenderFragmentState> {
    //Элементы управления
    private int currentTitle = R.string.title_tenders;

    //view-шки
    private RecyclerView mRecyclerView;
    private TextView mTextEmpty;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    TextView text_message;
    //
    private TenderAdapter mAdapter;

    @Override
    public void onStateChanged(TenderFragmentState stateModel) {
        getActivity().setTitle(currentTitle);
        if (!stateModel.isDownloaded()) {
            stateModel.setDownloaded(true);
            mSwipeRefreshLayout.setRefreshing(stateModel.getTenders().size() == 0);
            getPresenter().getAllTendersFromServer();
        }
        if (stateModel.isDataUpdated()) {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        if (stateModel.getTenders().size() == 0)
            mTextEmpty.setVisibility(View.VISIBLE);
        else
            mTextEmpty.setVisibility(View.GONE);

        if (stateModel.isRefreshingEnd()) {
            stateModel.setRefreshingEnd(false);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public ReampPresenter<TenderFragmentState> onCreatePresenter() {
        return new TenderFragmentPresenter();
    }

    @Override
    public TenderFragmentState onCreateStateModel() {
        TenderFragmentState state = new TenderFragmentState();
        OtherTender otherTender;
        List<Tender> tenders = new Select().from(Tender.class).where("dateEnd > ?",
                Tender.mFormatFromServer.format(new Date())).orderBy("dateEnd").execute();
        state.setTenders(new ArrayList<OtherTender>());
        for (Tender tender : tenders) {
            otherTender = new OtherTender();
            otherTender.setTender(tender);
            otherTender.setTasks(new Select().from(Tasks.class).where("taskId = ?", otherTender.getTender().getTaskId())
                    .<Tasks>executeSingle());
            otherTender.setUserinfo(new Select().from(Userinfo.class).where("userId = ?", otherTender.getTasks().getUserId())
                    .<Userinfo>executeSingle());

            if (otherTender.getUserinfo().getUserId() != DataStore.getUserId(getContext().getApplicationContext())
                    && otherTender.getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH))
                state.getTenders().add(otherTender);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tender_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mTextEmpty = view.findViewById(R.id.text_list_empty);
        //getPresenter().changeTenders(new ArrayList<TenderTask>());
        mRecyclerView = view.findViewById(R.id.recycle_tenders);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new TenderAdapter(getPresenter().getStateModel().getTenders(), getContext(), getPresenter()));

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().getAllTendersFromServer();
            }
        });
        // getPresenter().getAllTendersFromServer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_tender_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_map:
                Fragment fragment = new TendersMapFragment();
                ((MainActivity)getActivity()).replaceFragment(fragment,null,null,R.id.container,false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}