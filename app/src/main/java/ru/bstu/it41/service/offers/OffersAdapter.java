package ru.bstu.it41.service.offers;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.databinding.OfferItemBinding;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.offers.forTask.view.OfferForTaskViewActivity;
import ru.bstu.it41.service.offers.view.OfferViewActivity;

/**
 * Created by Герман on 16.11.2017.
 */

public class OffersAdapter extends RecyclerView.Adapter<OfferHolder> {

    private List<OfferWithTask> mOffersWithUser;
    private OffersFragmentPresenter mPresenter;
    private Context mContext;

    public OffersAdapter(OffersFragmentPresenter presenter, Context context)
    {
        this.mOffersWithUser = presenter.getStateModel().getOffersWithTask();
        mPresenter = presenter;
        mContext=context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        OfferItemBinding binding = null;
        try {
            binding = DataBindingUtil.inflate(inflater, R.layout.offer_item, parent, false);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new OfferHolder(binding);
    }

    @Override
    public void onBindViewHolder(OfferHolder holder, final int position) {

        OfferWithTask offer = mOffersWithUser.get(position);
        holder.bindGood(offer,mPresenter.mFormatForDate,mContext);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OfferViewActivity.getIntent(mContext,mOffersWithUser.get(position).getOffer().getOfferId());
                try {
                    mContext.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
