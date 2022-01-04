package ru.bstu.it41.service.tasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.TaskAndTender;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 16.11.2017.
 */

public class TaskHolder extends RecyclerView.ViewHolder {

    private static String TAG = "TASK_HOLDER";
    private TextView mTextName;
    private TextView mTextCategory;
    private TextView mTextStatus;
    private TextView mTextTenderDuration;
    private LinearLayout mLayoutTenderDate;
    private ImageView mImageStatus;

    private TaskAndTender mTask;

    public TaskHolder(View itemView , final TaskFragmentPresenter presenter)
    {
        super(itemView);
        mTextName = itemView.findViewById(R.id.text_name);
        mTextCategory = itemView.findViewById(R.id.text_category);
        mTextStatus = itemView.findViewById(R.id.text_status);
        mTextTenderDuration = itemView.findViewById(R.id.text_tender_duration);

        mLayoutTenderDate = itemView.findViewById(R.id.layout_tender_date);
        mImageStatus = itemView.findViewById(R.id.image_status);
    }

    public void bindGood(TaskAndTender task,SimpleDateFormat mFormatForDate, final MainActivity activity)
    {
        mTask=task;
        mTextName.setText(mTask.getTasks().getName());

        Category category = new Select().from(Category.class)
                .where("categoryId = ?",mTask.getTasks().getCategoryId())
                .executeSingle();
        if(category!= null)
            mTextCategory.setText(category.getCategoryName());
        else
            mTextCategory.setText("Категория не найдена");

        mTextStatus.setText(mTask.getTasks().getStatus());

        if(mTask.getTender()!=null) {
            /*mTextTenderDuration.setText((mTask.getTender().getDateStart() != null ?
                    mFormatForDate.format(mTask.getTender().getDateStart()) : "null") +
                    " - " + (mTask.getTender().getDateEnd() != null ?
                    mFormatForDate.format(mTask.getTender().getDateEnd()) : "null"));*/
            mTextTenderDuration.setText(activity.getString(R.string.tender_duration,
                    mFormatForDate.format(mTask.getTender().getDateStart()),
                    mFormatForDate.format(mTask.getTender().getDateEnd())));
            //mTextDescription.setText(mPlace.getDescription());
            mLayoutTenderDate.setVisibility(View.VISIBLE);
        }
        else {
            mLayoutTenderDate.setVisibility(View.GONE);
            mTextStatus.setText("Тендер не создан");
        }

        if(mTask.getTasks().getStatus().equals(Tasks.Status.STATUS_EXECUTED)){
            mImageStatus.setImageResource(R.drawable.ic_check);
            mImageStatus.setVisibility(View.VISIBLE);
        }
        else if(mTask.getTasks().getStatus().equals(Tasks.Status.STATUS_NONEXECUTED)){
            mImageStatus.setImageResource(R.drawable.ic_close);
            mImageStatus.setVisibility(View.VISIBLE);
        } else{
            mImageStatus.setVisibility(View.GONE);
        }
    }

}
