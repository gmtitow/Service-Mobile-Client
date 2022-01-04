package ru.bstu.it41.service.offers.forTask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.offers.forTask.view.OfferForTaskViewActivity;

/**
 * Created by Герман on 16.11.2017.
 */

public class OffersAdapter extends RecyclerView.Adapter<OfferHolder> {

    private List<OfferWithUser> mOffersWithUser;
    private OffersForTaskFragmentPresenter mPresenter;
    private Context mContext;

    public OffersAdapter(OffersForTaskFragmentPresenter presenter, Context context)
    {
        this.mOffersWithUser = presenter.getStateModel().getOffersWithUser();
        mPresenter = presenter;
        mContext=context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater li=(LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );//????
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(R.layout.other_offer_item, parent,false);
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferHolder holder, final int position) {

        OfferWithUser offer =mOffersWithUser.get(position);
        holder.bindGood(offer,mPresenter.mFormatForDate,mContext);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OfferForTaskViewActivity.getIntent(mContext,mOffersWithUser.get(position).getOffer().getOfferId());
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
        return mOffersWithUser.size();
    }
}
