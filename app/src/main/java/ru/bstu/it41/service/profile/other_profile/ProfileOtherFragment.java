package ru.bstu.it41.service.profile.other_profile;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.about.ProfileAboutActivity;
import ru.bstu.it41.service.profile.about.ProfileAboutFragment;
import ru.bstu.it41.service.profile.ProfileFragmentState;
import ru.bstu.it41.service.profile.review.ReviewFragment;
import ru.bstu.it41.service.profile.review.ReviewsActivity;


public class ProfileOtherFragment extends ReampFragment<ProfileOtherFragmentPresenter, ProfileOtherFragmentState> {

    //private int currentTitle = R.string.title_profile;
    private static String USERINFO_ID_KEY = "ru.bstu.it41.service.profile_userinfo";

    private TextView mTextFIO;
    private TextView mTextBirthday;
    private TextView mTextMale;
    private TextView mTextAddress;
    private RatingBar mTextRaitingExecutor;
    private RatingBar mTextRaitingClient;
    private TextView mTextAbout;
    private TextView mTextReviews;
    private TextView mTextCountYears;
    private Bitmap bitmap;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView mImagePhoto;
    //
    ProfileFragmentState stateModel = null;

    @Override
    public void onStateChanged(ProfileOtherFragmentState stateModel) {
        getActivity().setTitle(stateModel.getUserInfo().getFirstname() + " " + stateModel.getUserInfo().getLastname());
        //Цвет будет красным там, где ошибка, все элементарно.
        mTextFIO.setText(
                (stateModel.getUserInfo().getFirstname() == null ? "" : stateModel.getUserInfo().getFirstname()) +
                        " " + (stateModel.getUserInfo().getLastname() == null ? "" : stateModel.getUserInfo().getLastname()) +
                        " " + (stateModel.getUserInfo().getPatronymic() == null ? "" : stateModel.getUserInfo().getPatronymic()));

        /*mTextBirthday.setText(stateModel.getUserInfo().getBirthday() != null ?
                getPresenter().mFormatForDate.format(stateModel.getUserInfo().getBirthday().getTime()) : "не указано");*/
        mTextMale.setText(stateModel.getUserInfo().getMale() == 1 ? "Мужской" : "Женский");
        mTextAddress.setText(stateModel.getUserInfo().getAddress());

        mTextRaitingClient.setRating(stateModel.getUserInfo().getRaitingClient()/2);
        mTextRaitingExecutor.setRating(stateModel.getUserInfo().getRaitingExecutor()/2);

        if (stateModel.getUserInfo().getPathToPhoto() != null &&
                !stateModel.getUserInfo().getPathToPhoto().trim().equals("")) {
            Picasso.with(getView().getContext()) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + stateModel.getUserInfo().getPathToPhoto())
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }


        if(stateModel.getUserInfo().getBirthday() != null) {
            Resources res = getResources();
            Calendar today = Calendar.getInstance(Locale.getDefault());
            int count = today.get(Calendar.YEAR) - stateModel.getUserInfo().getBirthday().get(Calendar.YEAR);
            String years = res.getQuantityString(R.plurals.years_counts, count, count);
            mTextCountYears.setText(years);
        }
    }

    public ProfileOtherFragment() {
        // Required empty public constructor
    }

    @Override
    public ReampPresenter<ProfileOtherFragmentState> onCreatePresenter() {
        return new ProfileOtherFragmentPresenter();
    }

    @Override
    public ProfileOtherFragmentState onCreateStateModel() {
        ProfileOtherFragmentState state = new ProfileOtherFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(USERINFO_ID_KEY)) {
            state.setUserInfo(new Select().from(Userinfo.class).where("userId = ?",
                    getArguments().getInt(USERINFO_ID_KEY)).<Userinfo>executeSingle());
        }
        return state;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_other_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mTextFIO = view.findViewById(R.id.text_fio);
        mTextBirthday = view.findViewById(R.id.text_birthday);
        mTextMale = view.findViewById(R.id.text_male);
        mTextAddress = view.findViewById(R.id.text_address);
        mTextAbout = view.findViewById(R.id.text_about);
        //mTextSettings = view.findViewById(R.id.text_settings);
        mTextReviews = view.findViewById(R.id.text_reviews);
        mImagePhoto = view.findViewById(R.id.image_photo);
        mTextCountYears = view.findViewById(R.id.text_count_year);

        mTextReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ReviewsActivity.getIntent(getContext(),getPresenter().getStateModel().getUserInfo().getUserId());
                startActivity(intent);
            }
        });
        mTextAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileAboutActivity.getIntent(getContext(),getPresenter().getStateModel().getUserInfo().getUserId());
                startActivity(intent);
            }
        });

        mTextRaitingClient = view.findViewById(R.id.text_raitingClient);
        mTextRaitingExecutor = view.findViewById(R.id.text_raitingExecutor);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
    }

    public static ProfileOtherFragment getInstance(int userId) {
        ProfileOtherFragment fragment = new ProfileOtherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USERINFO_ID_KEY, userId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
