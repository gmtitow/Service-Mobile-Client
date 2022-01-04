package ru.bstu.it41.service.profile.review;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.profile.review.view.ReviewViewActivity;
import ru.bstu.it41.service.profile.review.view.ReviewViewFragment;

/**
 * Created by Герман on 16.11.2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private List<ReviewAndUserinfo> mReviewAndUserinfos;
    private ReviewFragmentPresenter mPresenter;
    private Context mContext;

    public ReviewAdapter(ReviewFragmentPresenter presenter, Context context)
    {
        this.mReviewAndUserinfos = presenter.getStateModel().getReviewAndUserinfos();
        mPresenter = presenter;
        mContext=context;
    }

    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater li=(LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );//????
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(R.layout.review_item, parent,false);
        return new ReviewHolder(view,mPresenter);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, final int position) {

        ReviewAndUserinfo review =mReviewAndUserinfos.get(position);
        holder.bindGood(review,mPresenter.mFormatForDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ReviewViewActivity.getIntent(mContext,mReviewAndUserinfos.get(position).getReviews().getIdReview());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mReviewAndUserinfos.size();
    }
}
