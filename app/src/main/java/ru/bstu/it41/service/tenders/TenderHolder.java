package ru.bstu.it41.service.tenders;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.lang.Integer.parseInt;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.OtherTender;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderHolder extends RecyclerView.ViewHolder {

    private TextView textFIO;
    private TextView textName;
    private TextView textCategory;
    private TextView textDeadline;
    private TextView textCost;
    private TextView textAddress;
    private TextView textTenderEnd;
    private TextView mTextCountYear;
    private ImageView mImagePhoto;


    public TenderHolder(View itemView)
    {
        super(itemView);

        textFIO = itemView.findViewById(R.id.text_fio);
        textName = itemView.findViewById(R.id.text_task_name);
        textCategory = itemView.findViewById(R.id.text_category);
        textCost = itemView.findViewById(R.id.text_tender_price);
        textDeadline = itemView.findViewById(R.id.text_deadline);
        textTenderEnd = itemView.findViewById(R.id.text_tender_tenderEnd);
        textAddress = itemView.findViewById(R.id.text_tender_address);
        mTextCountYear = itemView.findViewById(R.id.text_count_year);
        mImagePhoto = itemView.findViewById(R.id.image_photo);
    }

    public void bindGood(OtherTender tender, MainActivity mContext,SimpleDateFormat mFormatForDate)
    {
        textFIO.setText(mContext.getString(R.string.fio,tender.getUserinfo().getFirstname(),
                tender.getUserinfo().getLastname(),
                tender.getUserinfo().getPatronymic()!=null?tender.getUserinfo().getPatronymic():""));
        textName.setText(tender.getTasks().getName());
        /*textCategory.setText(mContext.getPresenter().getStateModel().findCategoryById(
                tender.getTasks().getCategoryId()).getCategoryName());*/
        textCost.setText(String.valueOf(tender.getTasks().getPrice()));
        textDeadline.setText(mFormatForDate.format(tender.getTasks().getDeadline()));
        textTenderEnd.setText(mFormatForDate.format(tender.getTender().getDateEnd()));
        textAddress.setText(tender.getTasks().getAddress()!=null?tender.getTasks().getAddress():"");

        //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(tender.getUserinfo().getBirthday() != null) {
            Resources res = mContext.getResources();
            Calendar today = Calendar.getInstance(Locale.getDefault());
            int count = today.get(Calendar.YEAR) - tender.getUserinfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);
            /*mTextCountYear.setText(String.valueOf(today.get(Calendar.YEAR) - mOfferWithUser.getUserinfo().getBirthday().get(Calendar.YEAR)) +
                    " лет");*/
            mTextCountYear.setText(years);
        }

        if(tender.getUserinfo().getPathToPhoto()!=null &&
                !tender.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(mContext) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + tender.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }

        Category category = new Select().from(Category.class)
                .where("categoryId = ?",tender.getTasks().getCategoryId())
                .executeSingle();
        if(category!= null)
            textCategory.setText(category.getCategoryName());
        else
            textCategory.setText("Категория не найдена");
    }
}
