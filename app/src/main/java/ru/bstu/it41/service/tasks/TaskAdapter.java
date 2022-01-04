package ru.bstu.it41.service.tasks;

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
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.tasks.view.TaskViewActivity;

/**
 * Created by Герман on 16.11.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

    private List<TaskAndTender> mTaskAndTenders;
    private TaskFragmentPresenter mPresenter;
    private Context mContext;

    public TaskAdapter(TaskFragmentPresenter presenter, Context context) {
        this.mTaskAndTenders = presenter.getStateModel().getTaskAndTenders();
        mPresenter = presenter;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater li=(LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );//????
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(R.layout.task_item, parent, false);

        return new TaskHolder(view, mPresenter);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, final int position) {

        TaskAndTender task = mTaskAndTenders.get(position);


        holder.bindGood(task, mPresenter.mFormatForDate, (MainActivity) mPresenter.getView().getContext());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //bundle.putInt("review",position);
                /*Fragment fragment = TaskViewFragment.getIntent(mTaskAndTenders.get(position).getTasks().getTaskId());

                bundle.putSerializable("model",mPresenter.getStateModel());
                bundle.putSerializable("presenter",mPresenter);
                ((MainActivity)mContext).replaceFragment(fragment,bundle,
                        ((MainActivity)mContext).STACK_LOCAL,R.id.container,false);*/

                Intent intent = TaskViewActivity.getIntent(mContext, mTaskAndTenders.get(position).getTasks().getTaskId());
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
        return mTaskAndTenders.size();
    }
}
