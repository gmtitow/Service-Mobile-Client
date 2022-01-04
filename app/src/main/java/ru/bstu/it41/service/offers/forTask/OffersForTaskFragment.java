package ru.bstu.it41.service.offers.forTask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.OffersWithUserinfo;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskFragment extends ReampFragment<OffersForTaskFragmentPresenter, OffersForTaskFragmentState> {
    private int currentTitle = R.string.title_offers_for_task;

    private static String TENDER_ID_KEY = "ru.bstu.it41.service.offersForTask_tender_id";
    //Элементы управления

    private TextView mTextListEmpty;
    private RecyclerView mRecycleOffers;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OffersAdapter mOffersAdapter;

    @Override
    public void onStateChanged(OffersForTaskFragmentState stateModel) {
        getActivity().setTitle(currentTitle);
        if(!stateModel.isDownloaded()) {
            stateModel.setDownloaded(true);
            mSwipeRefreshLayout.setRefreshing(true);
            getPresenter().downloadOffers();
        }

        if(stateModel.getOffersWithUser().size() == 0)
            mTextListEmpty.setVisibility(View.VISIBLE);
        else
            mTextListEmpty.setVisibility(View.GONE);

        mOffersAdapter.notifyDataSetChanged();

        if(stateModel.isRefreshingEnd()){
            stateModel.setRefreshingEnd(false);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public ReampPresenter<OffersForTaskFragmentState> onCreatePresenter() {
            return new OffersForTaskFragmentPresenter();
    }

    @Override
    public OffersForTaskFragmentState onCreateStateModel() {
        OffersForTaskFragmentState state = new OffersForTaskFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(TENDER_ID_KEY)) {
            state.setCurrentTenderId(getArguments().getInt(TENDER_ID_KEY));
            List<OfferWithUser> offersWithUser = new ArrayList<>();
            List<Offer> offers = new Select().from(Offer.class).where("tenderId = ?",
                    state.getCurrentTenderId()).execute();
            OfferWithUser offerWithUser;
            Userinfo info;
            for (Offer offer: offers) {
                offerWithUser = new OfferWithUser();
                offerWithUser.setOffer(offer);
                info = new Select().from(Userinfo.class).where("userId = ?",offer.getUserId()).executeSingle();
                offerWithUser.setUserinfo(info);
                offersWithUser.add(offerWithUser);
            }
            state.setOffersWithUser(offersWithUser);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers_for_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextListEmpty = view.findViewById(R.id.text_list_empty);
        mRecycleOffers = view.findViewById(R.id.recycle_view_offers);

        mRecycleOffers.setLayoutManager(new LinearLayoutManager(getContext()));
        mOffersAdapter = new OffersAdapter(getPresenter(),getContext());
        mRecycleOffers.setAdapter(mOffersAdapter);


        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().downloadOffers();
            }
        });
    }

    public static OffersForTaskFragment getInstance(int tenderId){
        OffersForTaskFragment fragment = new OffersForTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TENDER_ID_KEY,tenderId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
