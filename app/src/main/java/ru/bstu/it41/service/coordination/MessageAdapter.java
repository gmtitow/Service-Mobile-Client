package ru.bstu.it41.service.coordination;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.databinding.OtherMessageItemBinding;
import ru.bstu.it41.service.databinding.YourMessageItemBinding;
import ru.bstu.it41.service.models.Message;

/**
 * Created by Герман on 16.11.2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessages;
    private ChatFragmentPresenter mPresenter;
    private Context mContext;

    public MessageAdapter(ChatFragmentPresenter presenter, Context context)
    {
        this.mMessages = presenter.getStateModel().getMessages();
        mPresenter = presenter;
        mContext=context;
    }

    @Override
    public int getItemViewType(int position) {
        if((mMessages.get(position).getInput()== 0 && mPresenter.getStateModel().isClient()) ||
                (mMessages.get(position).getInput()== 1 && !mPresenter.getStateModel().isClient()) )
            return 0;
        else
            return 1;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        if(viewType == 0) {
            YourMessageItemBinding binding = null;
            try {
                binding = DataBindingUtil.inflate(inflater, R.layout.your_message_item, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new YourMessageHolder(binding);
        }
        else{
            OtherMessageItemBinding binding = null;
            try {
                binding = DataBindingUtil.inflate(inflater, R.layout.other_message_item, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new OtherMessageHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Message message =mMessages.get(position);
        if((mMessages.get(position).getInput()== 0 && mPresenter.getStateModel().isClient()) ||
                (mMessages.get(position).getInput()== 1 && !mPresenter.getStateModel().isClient()) ) {
            ((YourMessageHolder)holder).bindGood(message, mPresenter.mFormatForDate);
        }else
            ((OtherMessageHolder)holder).bindGood(message, mPresenter.mFormatForDate);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
