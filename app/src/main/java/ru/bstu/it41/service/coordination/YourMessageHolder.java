package ru.bstu.it41.service.coordination;

import android.support.v7.widget.RecyclerView;

import java.text.SimpleDateFormat;

import ru.bstu.it41.service.databinding.YourMessageItemBinding;
import ru.bstu.it41.service.models.Message;
import ru.bstu.it41.service.models.TaskAndTender;

/**
 * Created by Герман on 16.11.2017.
 */

public class YourMessageHolder extends RecyclerView.ViewHolder {

    private YourMessageItemBinding mBinding;
    private TaskAndTender mTask;

    public YourMessageHolder(YourMessageItemBinding binding)
    {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindGood(Message mMessage, SimpleDateFormat mFormatForDate)
    {
        mBinding.textMessage.setText(mMessage.getMessage());
        mBinding.textDate.setText(mFormatForDate.format(mMessage.getDate()));
    }

}
