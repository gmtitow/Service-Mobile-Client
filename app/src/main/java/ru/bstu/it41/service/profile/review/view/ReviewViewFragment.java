package ru.bstu.it41.service.profile.review.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.activeandroid.query.Select;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.review.ReviewFragmentPresenter;
import ru.bstu.it41.service.profile.review.ReviewFragmentState;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewViewFragment extends ReampFragment<ReviewViewFragmentPresenter, ReviewViewFragmentState> {
    //Элементы управления

    private static String REVIEW_ID_KEY = "ru.bstu.it41.service.profile.review.view.review_id";

    //private ProgressBar mProgressBar;

    private TextView mTextFIO;
    private TextView mTextDate;
    private TextView mTextAsExecutor;
    private TextView mTextReview;
    private RatingBar mRatingBar;

    @Override
    public void onStateChanged(ReviewViewFragmentState stateModel) {
        ReviewAndUserinfo mReview = stateModel.getReviewAndUserinfo();
        mTextFIO.setText((mReview.getUserinfo().getFirstname() == null ? "" : mReview.getUserinfo().getFirstname()) +
                " " + (mReview.getUserinfo().getLastname() == null ? "" : mReview.getUserinfo().getLastname()) +
                " " + (mReview.getUserinfo().getPatronymic() == null ? "" : mReview.getUserinfo().getPatronymic()));
        mTextDate.setText(mReview.getReviews().getReviewDate() != null ?
                getPresenter().mFormatForDate.format(mReview.getReviews().getReviewDate()) : "не указано");

        mTextAsExecutor.setText(mReview.getReviews().getExecutor() == 0 ? "Как о клиенте" : "Как об исполнителе");
        mTextReview.setText(mReview.getReviews().getTextReview());
        mRatingBar.setRating(mReview.getReviews().getRaiting());
    }

    @Override
    public ReampPresenter<ReviewViewFragmentState> onCreatePresenter() {
        return new ReviewViewFragmentPresenter();
    }

    @Override
    public ReviewViewFragmentState onCreateStateModel() {
        ReviewViewFragmentState state = new ReviewViewFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(REVIEW_ID_KEY)) {
            ReviewAndUserinfo reviewAndInfo = new ReviewAndUserinfo();

            reviewAndInfo.setReviews(new Select().from(Reviews.class).where("idReview = ?",
                    getArguments().getInt(REVIEW_ID_KEY)).executeSingle());

            reviewAndInfo.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                    reviewAndInfo.getReviews().getUserIdSubject()).executeSingle());
            state.setReviewAndUserinfo(reviewAndInfo);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextFIO = view.findViewById(R.id.text_fio);
        mTextDate = view.findViewById(R.id.text_date);
        mTextAsExecutor = view.findViewById(R.id.text_asExecutor);
        mTextReview = view.findViewById(R.id.text_review);
        mRatingBar = view.findViewById(R.id.rating);
    }

    public static ReviewViewFragment getInstance(int reviewId){
        ReviewViewFragment fragment = new ReviewViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REVIEW_ID_KEY,reviewId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
