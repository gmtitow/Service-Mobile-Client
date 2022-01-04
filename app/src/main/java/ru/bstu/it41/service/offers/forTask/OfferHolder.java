package ru.bstu.it41.service.offers.forTask;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.OfferWithUser;

/**
 * Created by Герман on 16.11.2017.
 */

public class OfferHolder extends RecyclerView.ViewHolder {
    private TextView mTextFIO;
    private TextView mTextCountYear;
    private TextView mTextDeadline;
    private TextView mTextDescription;
    private TextView mTextPrice;
    private ImageView mImagePhoto;

    private OfferWithUser mOfferWithUser;

    public OfferHolder(View itemView)
    {
        super(itemView);

        mTextFIO = itemView.findViewById(R.id.text_fio);
        mTextCountYear = itemView.findViewById(R.id.text_count_year);
        mTextDeadline = itemView.findViewById(R.id.text_deadline);
        mTextPrice = itemView.findViewById(R.id.text_price);
        mImagePhoto = itemView.findViewById(R.id.image_photo);
        mTextDescription = itemView.findViewById(R.id.text_description);
    }

    public void bindGood(OfferWithUser offer, SimpleDateFormat mFormatForDate, final Context mContext)
    {
        mOfferWithUser = offer;

        /*mTextFIO.setText(
                (mOfferWithUser.getUserinfo().getFirstname()==null?"":mOfferWithUser.getUserinfo().getFirstname()) +
                        " " + (mOfferWithUser.getUserinfo().getLastname()==null?"":mOfferWithUser.getUserinfo().getLastname()) +
                        " " + (mOfferWithUser.getUserinfo().getPatronymic()==null?"":mOfferWithUser.getUserinfo().getPatronymic()));*/
        mTextFIO.setText(mContext.getString(R.string.fio,mOfferWithUser.getUserinfo().getFirstname()==null?"":mOfferWithUser.getUserinfo().getFirstname(),
                mOfferWithUser.getUserinfo().getLastname()==null?"":mOfferWithUser.getUserinfo().getLastname(),
                mOfferWithUser.getUserinfo().getPatronymic()==null?"":mOfferWithUser.getUserinfo().getPatronymic()));

        //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(mOfferWithUser.getUserinfo().getBirthday() != null) {
            Resources res = mContext.getResources();
            Calendar today = Calendar.getInstance(Locale.getDefault());
            int count = today.get(Calendar.YEAR) - offer.getUserinfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);
            /*mTextCountYear.setText(String.valueOf(today.get(Calendar.YEAR) - mOfferWithUser.getUserinfo().getBirthday().get(Calendar.YEAR)) +
                    " лет");*/
            mTextCountYear.setText(years);
        } else{
            mTextCountYear.setText("");
        }

        mTextDeadline.setText(mFormatForDate.format(mOfferWithUser.getOffer().getDeadline()));
        mTextPrice.setText(String.valueOf(mOfferWithUser.getOffer().getPrice()));

        if(mOfferWithUser.getUserinfo().getPathToPhoto()!=null &&
                !mOfferWithUser.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(mContext) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + mOfferWithUser.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }else{
            mImagePhoto.setImageResource(R.drawable.ic_menu_camera);
        }

        mTextDescription.setText(offer.getOffer().getDescription());
    }
}
