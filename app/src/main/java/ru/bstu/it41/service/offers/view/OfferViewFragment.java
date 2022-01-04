package ru.bstu.it41.service.offers.view;

import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ru.bstu.it41.service.databinding.FragmentOfferViewBinding;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OfferWithTask;
import ru.bstu.it41.service.models.OfferWithUser;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.other_profile.OtherProfileActivity;
import ru.bstu.it41.service.tasks.map.ChooseLocationActivity;
import ru.bstu.it41.service.tenders.tenderView.TenderViewActivity;

import static android.view.View.GONE;

/**
 * Created by Герман on 18.10.2017.
 */

public class OfferViewFragment extends ReampFragment<OfferViewFragmentPresenter, OfferViewFragmentState> {
    private int currentTitle = R.string.title_offer;
    private int deleteTitle = R.string.title_offer_withdraw;

    private static String OFFER_ID_KEY = "ru.bstu.it41.service.offers.view.offer_id";

    FragmentOfferViewBinding mBinding;

    @Override
    public void onStateChanged(OfferViewFragmentState stateModel) {

        if(stateModel.isOfferDelete()){
            getActivity().setTitle(deleteTitle);
        }else
            getActivity().setTitle(currentTitle);
        OfferWithTask mOfferWithTask = stateModel.getCurrentOffer();

        //Для пользователя
        mBinding.textFio.setText(
                (mOfferWithTask.getUserinfo().getFirstname()==null?"":mOfferWithTask.getUserinfo().getFirstname()) +
                        " " + (mOfferWithTask.getUserinfo().getLastname()==null?"":mOfferWithTask.getUserinfo().getLastname()) +
                        " " + (mOfferWithTask.getUserinfo().getPatronymic()==null?"":mOfferWithTask.getUserinfo().getPatronymic()));

        //Calendar today = Calendar.getInstance(Locale.getDefault());

        if(mOfferWithTask.getUserinfo().getBirthday() != null) {
            Calendar today = Calendar.getInstance(Locale.getDefault());

            Resources res = getResources();
            int count = today.get(Calendar.YEAR) - stateModel.getCurrentOffer().getUserinfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);

            mBinding.textCountYear.setText(years);
            /*mBinding.textCountYear.setText(String.valueOf(today.get(Calendar.YEAR) - mOfferWithTask.getUserinfo().getBirthday().get(Calendar.YEAR)) +
                    " лет");*/
        }

        if(mOfferWithTask.getUserinfo().getPathToPhoto()!=null &&
                !mOfferWithTask.getUserinfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(getContext()) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + mOfferWithTask.getUserinfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mBinding.imagePhoto);
        }

        //Для предложения
        mBinding.textOfferDeadline.setText(getPresenter().mFormatForDate.format(mOfferWithTask.getOffer().getDeadline()));
        mBinding.textOfferPrice.setText(String.valueOf(mOfferWithTask.getOffer().getPrice()));

        mBinding.textOfferDescription.setText(mOfferWithTask.getOffer().getDescription());

        //Для задания
        mBinding.textTaskName.setText(mOfferWithTask.getTasks().getName());
        mBinding.textTaskDescription.setText(mOfferWithTask.getTasks().getDescription());
        mBinding.textTaskDeadline.setText(getPresenter().mFormatForDate.format(mOfferWithTask.getTasks().getDeadline()));
        mBinding.textTaskPrice.setText(String.valueOf(mOfferWithTask.getTasks().getPrice()));
        mBinding.textTenderAddress.setText(stateModel.getCurrentOffer().getTasks().getAddress());
        //Для тендера
        mBinding.textTenderEnd.setText(getPresenter().mFormatForDate.format(mOfferWithTask.getTender().getDateEnd()));

        if(stateModel.getCurrentOffer().getOffer().getSelected() == 1){
            mBinding.buttonToDialog.setVisibility(View.VISIBLE);
            mBinding.buttonToTender.setVisibility(GONE);
            mBinding.buttonWithdraw.setVisibility(GONE);
        }
        else{
            mBinding.buttonWithdraw.setVisibility(View.VISIBLE);
            mBinding.buttonToDialog.setVisibility(GONE);
            mBinding.buttonToTender.setVisibility(
                    stateModel.getCurrentOffer().getTasks().getStatus().equals(Tasks.Status.STATUS_SEARCH)?
                            View.VISIBLE: GONE);
        }
    }

    @Override
    public ReampPresenter<OfferViewFragmentState> onCreatePresenter() {
        return new OfferViewFragmentPresenter();
    }

    @Override
    public OfferViewFragmentState onCreateStateModel() {
        OfferViewFragmentState state = new OfferViewFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(OFFER_ID_KEY)){
            OfferWithTask offerWithTask = new OfferWithTask();

            offerWithTask.setOffer(new Select().from(Offer.class).where("offerId = ?",
                    getArguments().getInt(OFFER_ID_KEY)).<Offer>executeSingle());
            offerWithTask.setOffer(offerWithTask.getOffer());
            offerWithTask.setTender(new Select().from(Tender.class).where("tenderId = ?",
                    offerWithTask.getOffer().getTenderId()).<Tender>executeSingle());
            offerWithTask.setTasks(new Select().from(Tasks.class).where("taskId = ?",
                    offerWithTask.getTender().getTaskId()).<Tasks>executeSingle());
            offerWithTask.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                    offerWithTask.getTasks().getUserId()).<Userinfo>executeSingle());

            state.setCurrentOffer(offerWithTask);
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_view, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toOtherUserProfile(getPresenter().getStateModel().getCurrentOffer().getUserinfo());
            }
        });

        mBinding.buttonToTender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTender(getPresenter().getStateModel().getCurrentOffer().getTender().getTenderId());
            }
        });

        mBinding.buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().deleteOffer();
            }
        });

        mBinding.buttonToDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_COORDINATION);
                intent.putExtra(ChatFragment.EXTRA_TENDER_ID,getPresenter().getStateModel().getCurrentOffer().getTender().getTenderId());
                intent.putExtra(ChatFragment.EXTRA_IS_CLIENT,false);

                startActivity(intent);
                getActivity().finish();
            }
        });
        mBinding.buttonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMap();
            }
        });
    }

    public static OfferViewFragment getInstance(int offerId){
        OfferViewFragment fragment = new OfferViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(OFFER_ID_KEY,offerId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void toOtherUserProfile(Userinfo userinfo){
        Intent intent = OtherProfileActivity.getIntent(getContext(),userinfo.getUserId());
        startActivity(intent);
    }

    public void toTender(int tenderId){
        Intent intent = TenderViewActivity.getIntent(getContext(),tenderId);
        startActivity(intent);
    }

    public void toMap(){
        Intent intent = ChooseLocationActivity.getIntent(getContext()
                ,getPresenter().getStateModel().getCurrentOffer().getTasks().getLatitude(),
                getPresenter().getStateModel().getCurrentOffer().getTasks().getLongitude(),
                getPresenter().getStateModel().getCurrentOffer().getTasks().getAddress(),
                true);
        startActivity(intent);
    }
}
