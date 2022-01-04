package ru.bstu.it41.service.offers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.databinding.OfferItemBinding;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.Tasks;

/**
 * Created by Герман on 16.11.2017.
 */

public class OfferHolder extends RecyclerView.ViewHolder {
    private OfferItemBinding mBinding;

    public OfferHolder(OfferItemBinding binding)
    {
        super(binding.getRoot());
        mBinding = binding;
        /*mTextFIO = itemView.findViewById(R.id.text_fio);
        mTextCountYear = itemView.findViewById(R.id.text_count_year);
        mTextDeadline = itemView.findViewById(R.id.text_deadline);
        mTextPrice = itemView.findViewById(R.id.text_price);
        mImagePhoto = itemView.findViewById(R.id.image_photo);*/
    }

    public void bindGood(OfferWithTask offer, SimpleDateFormat mFormatForDate, final Context mContext)
    {
        Category category = new Select().from(Category.class)
                .where("categoryId = ?",offer.getTasks().getCategoryId())
                .executeSingle();

        mBinding.textCategory.setText(category.getCategoryName());

        if(offer.getUserinfo().getPathToPhoto()!=null &&
                !offer.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(mContext) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + offer.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mBinding.imagePhoto);
        }
        mBinding.textFio.setText(offer.getUserinfo().getFIO());

        mBinding.textCountYear.setText(offer.getUserinfo().getCountYear(mContext));


        mBinding.textDeadline.setText(mFormatForDate.format(offer.getOffer().getDeadline()));
        mBinding.textTaskName.setText(offer.getTasks().getName());
        mBinding.textTenderEnd.setText(mFormatForDate.format(offer.getTender().getDateEnd()));
        mBinding.textPrice.setText(String.valueOf(offer.getOffer().getPrice()));

        String offerStatus;
        if(offer.getOffer().getSelected()==1){
            offerStatus = offer.getTasks().getStatus();
        }
        else{
            if(!offer.getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH))
                offerStatus = Offer.Status.STATUS_NONSELECTED;
            else
                offerStatus = Offer.Status.STATUS_WAITING;
        }
        mBinding.textOfferStatus.setText(offerStatus);

        /*mTextFIO.setText(
                (mOfferWithUser.getUserinfo().getFirstname()==null?"":mOfferWithUser.getUserinfo().getFirstname()) +
                        " " + (mOfferWithUser.getUserinfo().getLastname()==null?"":mOfferWithUser.getUserinfo().getLastname()) +
                        " " + (mOfferWithUser.getUserinfo().getPatronymic()==null?"":mOfferWithUser.getUserinfo().getPatronymic()));*/
        /*mTextFIO.setText(mContext.getString(R.string.fio,mOfferWithUser.getUserinfo().getFirstname()==null?"":mOfferWithUser.getUserinfo().getFirstname(),
                mOfferWithUser.getUserinfo().getLastname()==null?"":mOfferWithUser.getUserinfo().getLastname(),
                mOfferWithUser.getUserinfo().getPatronymic()==null?"":mOfferWithUser.getUserinfo().getPatronymic()));

        Calendar today = Calendar.getInstance(Locale.getDefault());

        if(mOfferWithUser.getUserinfo().getBirthday() != null)
            mTextCountYear.setText(String.valueOf(today.get(Calendar.YEAR) - mOfferWithUser.getUserinfo().getBirthday().get(Calendar.YEAR)) +
                    " лет");

        mTextDeadline.setText(mFormatForDate.format(mOfferWithUser.getOffer().getDeadline()));
        mTextPrice.setText(String.valueOf(mOfferWithUser.getOffer().getPrice()));

        if(mOfferWithUser.getUserinfo().getPathToPhoto()!=null &&
                !mOfferWithUser.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(mContext) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + mOfferWithUser.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }*/
    }
}
