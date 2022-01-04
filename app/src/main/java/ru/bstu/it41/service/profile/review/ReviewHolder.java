package ru.bstu.it41.service.profile.review;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.TaskAndTender;

/**
 * Created by Герман on 16.11.2017.
 */

public class ReviewHolder extends RecyclerView.ViewHolder {

    private static String TAG = "TASK_HOLDER";
    private TextView mTextFIO;
    private TextView mTextDate;
    private TextView mTextAsExecutor;
    private TextView mTextReview;
    private RatingBar mRatingBar;

    private ReviewAndUserinfo mReview;

    public ReviewHolder(View itemView , final ReviewFragmentPresenter presenter)
    {
        super(itemView);

        mTextFIO = itemView.findViewById(R.id.text_fio);
        mTextDate = itemView.findViewById(R.id.text_date);
        mTextAsExecutor = itemView.findViewById(R.id.text_asExecutor);
        mTextReview = itemView.findViewById(R.id.text_review);
        mRatingBar = itemView.findViewById(R.id.rating);
    }

    public void bindGood(ReviewAndUserinfo review,SimpleDateFormat mFormatForDate)
    {
        mReview=review;
        mTextFIO.setText((mReview.getUserinfo().getFirstname()==null?"":mReview.getUserinfo().getFirstname()) +
                " " + (mReview.getUserinfo().getLastname()==null?"":mReview.getUserinfo().getLastname()) +
                " " + (mReview.getUserinfo().getPatronymic()==null?"":mReview.getUserinfo().getPatronymic()));
        mTextDate.setText(mReview.getReviews().getReviewDate() != null ?
                mFormatForDate.format(mReview.getReviews().getReviewDate()) : "не указано");

        mTextAsExecutor.setText(mReview.getReviews().getExecutor()==0?"Как о клиенте":"Как об исполнителе");
        mTextReview.setText(mReview.getReviews().getTextReview());

        mRatingBar.setRating(mReview.getReviews().getRaiting());
    }

}
