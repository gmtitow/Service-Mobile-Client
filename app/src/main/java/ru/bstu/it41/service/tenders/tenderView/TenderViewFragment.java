package ru.bstu.it41.service.tenders.tenderView;

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
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.offers.add.OfferAddActivity;
import ru.bstu.it41.service.offers.add.OffersAddFragment;
import ru.bstu.it41.service.profile.other_profile.OtherProfileActivity;
import ru.bstu.it41.service.tenders.tendersMap.TendersMapFragment;

/**
 * Created by Герман on 16.11.2017.
 */

public class TenderViewFragment extends ReampFragment<TenderViewFragmentPresenter, TenderViewFragmentState> {
    private static String TENDER_KEY_ID = "ru.bstu.it41.service.tenders.tenderView.tender_id";


    private int currentTitle = R.string.title_tender;

    //view-шки
    private TextView mTextFIO;
    private TextView mTextCountYear;
    private TextView mTextDeadline;
    private TextView mTextPrice;
    private ImageView mImagePhoto;
    private TextView mTextDescription;
    private TextView mTextName;
    private TextView mTextCategory;
    private TextView mTextTenderStart;
    private TextView mTextTenderEnd;
    private TextView textAddress;
    private ConstraintLayout mLayoutUser;

    private Button mButtonToMap;

    private Button mButtonAddOffer;

    @Override
    public void onStateChanged(TenderViewFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        mTextFIO.setText(getContext().getString(R.string.fio, stateModel.getTender().getUserinfo().getFirstname() == null ?
                        "" : stateModel.getTender().getUserinfo().getFirstname(),
                stateModel.getTender().getUserinfo().getLastname() == null ? "" : stateModel.getTender().getUserinfo().getLastname(),
                stateModel.getTender().getUserinfo().getPatronymic() == null ? "" : stateModel.getTender().getUserinfo().getPatronymic()));

        //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(stateModel.getTender().getUserinfo().getBirthday()!=null) {
            Calendar today = Calendar.getInstance(Locale.getDefault());
            Resources res = getResources();
            int count = today.get(Calendar.YEAR) - stateModel.getTender().getUserinfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);

            mTextCountYear.setText(years);
        }

        if (stateModel.getTender().getUserinfo().getPathToPhoto() != null &&
                !stateModel.getTender().getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(getContext()) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + stateModel.getTender().getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }
        mTextName.setText(stateModel.getTender().getTasks().getName());

        Category category = new Select().from(Category.class).where("categoryId = ?",
                stateModel.getTender().getTasks().getCategoryId()).executeSingle();

        mTextCategory.setText(category.getCategoryName());

        mTextPrice.setText(String.valueOf(stateModel.getTender().getTasks().getPrice()));
        mTextDeadline.setText(getPresenter().mFormatForDate.format(stateModel.getTender().getTasks().getDeadline()));
        mTextDescription.setText(stateModel.getTender().getTasks().getDescription());
        textAddress.setText(stateModel.getTender().getTasks().getAddress()!=null?stateModel.getTender().getTasks().getAddress():"");

        mTextTenderStart.setText(getPresenter().mFormatForDate.format(stateModel.getTender().getTender().getDateStart()));
        mTextTenderEnd.setText(getPresenter().mFormatForDate.format(stateModel.getTender().getTender().getDateEnd()));

        mButtonAddOffer.setVisibility(DataStore.getUserId(getContext().getApplicationContext())!=-1
                && getPresenter().getStateModel().getTender().getOffer()==null?View.VISIBLE:View.GONE);

        mButtonToMap.setVisibility((stateModel.getTender().getTasks().getLatitude()!=null&&
                stateModel.getTender().getTasks().getLongitude()!=null)?View.VISIBLE:View.GONE);
    }

    @Override
    public ReampPresenter<TenderViewFragmentState> onCreatePresenter() {
        return new TenderViewFragmentPresenter();
    }

    @Override
    public TenderViewFragmentState onCreateStateModel() {
        TenderViewFragmentState state = new TenderViewFragmentState();
        if (getArguments() != null && getArguments().containsKey(TENDER_KEY_ID)) {
            OtherTender otherTender;
            Tender tender = new Select().from(Tender.class).where("tenderId = ?",
                    getArguments().getInt(TENDER_KEY_ID)).executeSingle();
            otherTender = new OtherTender();
            otherTender.setTender(tender);
            otherTender.setTasks(new Select().from(Tasks.class).where("taskId = ?", otherTender.getTender().getTaskId())
                    .<Tasks>executeSingle());
            otherTender.setUserinfo(new Select().from(Userinfo.class).where("userId = ?", otherTender.getTasks().getUserId())
                    .<Userinfo>executeSingle());
            otherTender.setOffer(new Select().from(Offer.class).where("tenderId = ? and userId = ?",
                    otherTender.getTender().getTenderId(),DataStore.getUserId(getContext().getApplicationContext()))
                    .<Offer>executeSingle());

            state.setTender(otherTender);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tender_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextFIO = view.findViewById(R.id.text_fio);
        mTextCountYear = view.findViewById(R.id.text_count_year);
        mImagePhoto = view.findViewById(R.id.image_photo);

        mTextName = view.findViewById(R.id.text_task_name);
        mTextDescription = view.findViewById(R.id.text_task_description);
        mTextCategory = view.findViewById(R.id.text_category);
        mTextPrice = view.findViewById(R.id.text_tender_price);
        mTextDeadline = view.findViewById(R.id.text_deadline);

        mTextTenderStart = view.findViewById(R.id.text_tenderStart);
        mTextTenderEnd = view.findViewById(R.id.text_tenderEnd);

        textAddress = view.findViewById(R.id.text_tender_address);

        mLayoutUser = view.findViewById(R.id.layout_user);

        mButtonAddOffer = view.findViewById(R.id.button_add_offer);

        mButtonToMap = view.findViewById(R.id.button_to_map);

        mButtonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAddOffer(getPresenter().getStateModel().getTender().getTender().getTenderId());
            }
        });

        mButtonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMap(getPresenter().getStateModel().getTender().getTasks().getLatitude(),
                        getPresenter().getStateModel().getTender().getTasks().getLongitude());
            }
        });

        if(DataStore.getUserId(getContext().getApplicationContext())!=-1) {
            mLayoutUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toOtherProfile(getPresenter().getStateModel().getTender().getUserinfo().getUserId());
                }
            });
        }
    }

    public static TenderViewFragment getInstance(int tenderId) {
        TenderViewFragment fragment = new TenderViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TENDER_KEY_ID, tenderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void toAddOffer(int tenderId) {
        Intent intent = OfferAddActivity.getIntent(getContext(),tenderId);
        startActivity(intent);
    }

    public void goToMap(double latitude, double longitude){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_TENDERS_MAP_TO_POINT);
        intent.putExtra(TendersMapFragment.EXTRA_LATITUDE,latitude);
        intent.putExtra(TendersMapFragment.EXTRA_LONGITUDE,longitude);
        startActivity(intent);
    }

    public void toOtherProfile(int userId){
        Intent intent = OtherProfileActivity.getIntent(getContext(),userId);
        startActivity(intent);
    }
}