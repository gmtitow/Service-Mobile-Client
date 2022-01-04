package ru.bstu.it41.service.tenders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.tenders.tenderView.TenderViewActivity;
import ru.bstu.it41.service.tenders.tenderView.TenderViewFragment;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderAdapter extends RecyclerView.Adapter<TenderHolder> {

    private List<OtherTender> mTenders;
    private Context mContext; //????
    public TenderFragmentPresenter presenter;

    public TenderAdapter(List<OtherTender> tenders, Context context, TenderFragmentPresenter presenter)
    {
        this.mTenders =tenders;
        mContext=context;
        this.presenter = presenter;
    }

    public TenderHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(R.layout.tender_item,parent,false);

        return new TenderHolder(view);
    }

    @Override
    public void onBindViewHolder(TenderHolder holder, final int position) {

        OtherTender tender = mTenders.get(position);

        holder.bindGood(tender,(MainActivity) mContext,presenter.mFormatForDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TenderViewActivity.getIntent(mContext,mTenders.get(position).getTender().getTenderId());
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
        return mTenders.size();
    }
}
