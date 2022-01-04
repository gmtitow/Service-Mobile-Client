package ru.bstu.it41.service.profile.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Userinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewFragment extends ReampFragment<ReviewFragmentPresenter, ReviewFragmentState> {

    private int currentTitle = R.string.title_reviews;
    //Элементы управления
    private static String USER_ID_KEY = "ru.bstu.it41.service.review_user_id";

    //private ProgressBar mProgressBar;
    private TextView mTextListEmpty;
    private RecyclerView mRecycleTasks;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ReviewAdapter mReviewAdapter;

    @Override
    public void onStateChanged(ReviewFragmentState stateModel) {
        getActivity().setTitle(currentTitle);
        if(!stateModel.isDownloaded()) {
            stateModel.setDownloaded(true);
            mSwipeRefreshLayout.setRefreshing(stateModel.getReviewAndUserinfos().size()==0);
            getPresenter().downloadReviews();
        }

        if(stateModel.isRefreshingEnd()){
            stateModel.setRefreshingEnd(false);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mReviewAdapter.notifyDataSetChanged();

        mTextListEmpty.setVisibility(stateModel.getReviewAndUserinfos().size()==0?View.VISIBLE:View.GONE);
    }
    @Override
    public ReampPresenter<ReviewFragmentState> onCreatePresenter() {
        ReviewFragmentPresenter presenter = new ReviewFragmentPresenter();
        return presenter;
    }

    @Override
    public ReviewFragmentState onCreateStateModel() {
        ReviewFragmentState state = new ReviewFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(USER_ID_KEY)) {
            state.setCurrentUserId(getArguments().getInt(USER_ID_KEY));
            List<Reviews> reviews = new Select().from(Reviews.class).where("userIdObject = ?",
                    state.getCurrentUserId()).orderBy("reviewDate DESC").execute();

            ReviewAndUserinfo reviewAndUserinfo;
            state.setReviewAndUserinfos(new ArrayList<ReviewAndUserinfo>());
            for (Reviews review: reviews) {
                try {
                    reviewAndUserinfo = new ReviewAndUserinfo();
                    reviewAndUserinfo.setReviews(review);
                    reviewAndUserinfo.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                            review.getUserIdSubject()).<Userinfo>executeSingle());
                    state.getReviewAndUserinfos().add(reviewAndUserinfo);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mProgressBar = view.findViewById(R.id.progress);
        mTextListEmpty = view.findViewById(R.id.text_list_empty);
        mRecycleTasks = view.findViewById(R.id.recycle_reviews);

        mRecycleTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        mReviewAdapter = new ReviewAdapter(getPresenter(),getContext());
        mRecycleTasks.setAdapter(mReviewAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().downloadReviews();
            }
        });
    }

    public static ReviewFragment getInstance(int userId){
        ReviewFragment fragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID_KEY,userId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
