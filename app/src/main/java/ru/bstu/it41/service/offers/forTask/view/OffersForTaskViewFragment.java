package ru.bstu.it41.service.offers.forTask.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.offers.forTask.OffersForTaskFragmentPresenter;
import ru.bstu.it41.service.offers.forTask.OffersForTaskFragmentState;
import ru.bstu.it41.service.profile.other_profile.OtherProfileActivity;
import ru.bstu.it41.service.profile.other_profile.ProfileOtherFragment;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersForTaskViewFragment extends ReampFragment<OffersForTaskViewFragmentPresenter, OffersForTaskViewFragmentState> {
    private int currentTitle = R.string.title_offer_for_task;
    private int progressTitle = R.string.title_offer_selection_in_progress;


    private static String OFFER_ID_KEY = "ru.bstu.it41.service.offersForTask_offer_id";
    //Элементы управления

    private TextView mTextFIO;
    private TextView mTextCountYear;
    private TextView mTextDeadline;
    private TextView mTextPrice;
    private ImageView mImagePhoto;
    private TextView mTextDescription;
    private ConstraintLayout mLayoutUser;
    private Button mButtonSelectExecutor;
    //private ProgressBar mProgressBar;


    @Override
    public void onStateChanged(OffersForTaskViewFragmentState stateModel) {
        if(stateModel.isProgress()){
            getActivity().setTitle(progressTitle);
        }else {
            getActivity().setTitle(currentTitle);
        }
        OfferWithUser mOfferWithUser = stateModel.getCurrentOffer();

        mTextFIO.setText(
                (mOfferWithUser.getUserinfo().getFirstname()==null?"":mOfferWithUser.getUserinfo().getFirstname()) +
                        " " + (mOfferWithUser.getUserinfo().getLastname()==null?"":mOfferWithUser.getUserinfo().getLastname()) +
                        " " + (mOfferWithUser.getUserinfo().getPatronymic()==null?"":mOfferWithUser.getUserinfo().getPatronymic()));

       //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(mOfferWithUser.getUserinfo().getBirthday() != null) {
            Calendar today = Calendar.getInstance(Locale.getDefault());
            Resources res = getResources();
            int count = today.get(Calendar.YEAR) - stateModel.getCurrentOffer().getUserinfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);

            mTextCountYear.setText(years);
        }

        mTextDeadline.setText(getPresenter().mFormatForDate.format(mOfferWithUser.getOffer().getDeadline()));
        mTextPrice.setText(String.valueOf(mOfferWithUser.getOffer().getPrice()));

        if(mOfferWithUser.getUserinfo().getPathToPhoto()!=null &&
                !mOfferWithUser.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(getContext()) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + mOfferWithUser.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }

        mTextDescription.setText(mOfferWithUser.getOffer().getDescription());
    }

    @Override
    public ReampPresenter<OffersForTaskViewFragmentState> onCreatePresenter() {
        return new OffersForTaskViewFragmentPresenter();
    }

    @Override
    public OffersForTaskViewFragmentState onCreateStateModel() {
        OffersForTaskViewFragmentState state = new OffersForTaskViewFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(OFFER_ID_KEY)){
            OfferWithUser offerWithUser = new OfferWithUser();
            offerWithUser.setOffer(new Select().from(Offer.class).where("offerId = ?",
                    getArguments().getInt(OFFER_ID_KEY)).<Offer>executeSingle());
            offerWithUser.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                    offerWithUser.getOffer().getUserId()).<Userinfo>executeSingle());
            state.setCurrentOffer(offerWithUser);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer_for_task_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextFIO = view.findViewById(R.id.text_fio);
        mTextCountYear = view.findViewById(R.id.text_count_year);
        mTextDeadline = view.findViewById(R.id.text_deadline);
        mTextPrice = view.findViewById(R.id.text_price);
        mImagePhoto = view.findViewById(R.id.image_photo);
        mTextDescription = view.findViewById(R.id.text_description);
        mButtonSelectExecutor = view.findViewById(R.id.button_select_executor);

        mLayoutUser = view.findViewById(R.id.layout_user);

        mLayoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOtherUserProfile(getPresenter().getStateModel().getCurrentOffer().getUserinfo());
            }
        });

        mButtonSelectExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().selectOffer();
            }
        });
    }

    public static OffersForTaskViewFragment getInstance(int offerId){
        OffersForTaskViewFragment fragment = new OffersForTaskViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(OFFER_ID_KEY,offerId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void toOtherUserProfile(Userinfo userinfo){
        Intent intent = OtherProfileActivity.getIntent(getContext(),userinfo.getUserId());
        startActivity(intent);
    }
}
