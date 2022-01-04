package ru.bstu.it41.service.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.any.UserRequest;
import ru.bstu.it41.service.models.AllForUser;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.about.ProfileAboutActivity;
import ru.bstu.it41.service.profile.about.ProfileAboutFragment;
import ru.bstu.it41.service.profile.change.ProfileChangeActivity;
import ru.bstu.it41.service.profile.review.ReviewsActivity;
import ru.bstu.it41.service.profile.settings.SettingsActivity;
import ru.bstu.it41.service.profile.settings.SettingsFragment;


public class ProfileFragment extends ReampFragment<ProfileFragmentPresenter, ProfileFragmentState> {

    private int currentTitle = R.string.title_profile;
    private static final int REQUEST_ABOUT = 1;
    private static final int REQUEST_CHANGE = 1123;

    private TextView mTextFIO;
    private TextView mTextBirthday;
    private TextView mTextMale;
    private TextView mTextAddress;
    private RatingBar mTextRaitingExecutor;
    private RatingBar mTextRaitingClient;
    private TextView mTextAbout;
    private TextView mTextSettings;
    private TextView mTextReviews;
    private Bitmap bitmap;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Button mButtonChangeProfile;
    private ImageView mImagePhoto;
    //
    //ProfileFragmentState stateModel = null;
    private ProfileFragment mThisFragment = this;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //getPresenter().changePhoto(bitmap);
            getPresenter().savePhotoToFile(bitmap, getPresenter().getStateModel().getFileName());
            getPresenter().updateView();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };



    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(mThisFragment))
            EventBus.getDefault().register(mThisFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStateChanged(ProfileFragmentState stateModel) {
        getActivity().setTitle(currentTitle);
        /*if (!stateModel.isDownloaded() && !stateModel.isShowProgress() && stateModel.isEnableDownload()) {
            getPresenter().download();
        }*/
        mSwipeRefreshLayout.setRefreshing(stateModel.isShowProgress());
        //Цвет будет красным там, где ошибка, все элементарно.
        mTextFIO.setText(
                (stateModel.getUserinfo().getFirstname() == null ? "" : stateModel.getUserinfo().getFirstname()) +
                        " " + (stateModel.getUserinfo().getLastname() == null ? "" : stateModel.getUserinfo().getLastname()) +
                        " " + (stateModel.getUserinfo().getPatronymic() == null ? "" : stateModel.getUserinfo().getPatronymic()));

        mTextBirthday.setText(stateModel.getUserinfo().getBirthday() != null ?
                getPresenter().mFormatForDate.format(stateModel.getUserinfo().getBirthday().getTime()) : "не указано");
        mTextMale.setText(stateModel.getUserinfo().getMale() == 1 ? "Мужской" : "Женский");
        mTextAddress.setText(stateModel.getUserinfo().getAddress());
        //mTextPhone.setText(stateModel.get());

        mTextRaitingClient.setRating(stateModel.getUserinfo().getRaitingClient()/2);
        mTextRaitingExecutor.setRating(stateModel.getUserinfo().getRaitingExecutor()/2);

        if (stateModel.getUserinfo().getPathToPhoto() != null &&
                !stateModel.getUserinfo().getPathToPhoto().trim().equals("")) {

            //Picasso.with(getContext()).invalidate(UserRequest.URL_SERVER + stateModel.getUserinfo().getPathToPhoto());

            Picasso.with(getContext()) //передаем контекст приложения
                    .load(UserRequest.URL_SERVER + stateModel.getUserinfo().getPathToPhoto())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mImagePhoto);
        }

    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public ReampPresenter<ProfileFragmentState> onCreatePresenter() {
        return new ProfileFragmentPresenter();
    }

    @Override
    public ProfileFragmentState onCreateStateModel() {
        ProfileFragmentState state = new ProfileFragmentState();

        AllForUser allForUser = new AllForUser();
        allForUser.setUser(new Select().from(User.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).<User>executeSingle());
        allForUser.setUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).<Userinfo>executeSingle());

        allForUser.setSettings(new Select().from(Settings.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).<Settings>executeSingle());

        state.setAllForUser(allForUser);

        return state;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mTextFIO = view.findViewById(R.id.text_fio);
        mTextBirthday = view.findViewById(R.id.text_birthday);
        mTextMale = view.findViewById(R.id.text_male);
        mTextAddress = view.findViewById(R.id.text_address);
        mTextAbout = view.findViewById(R.id.text_about);
        mTextSettings = view.findViewById(R.id.text_settings);
        mTextReviews = view.findViewById(R.id.text_reviews);
        mImagePhoto = view.findViewById(R.id.image_photo);

        mButtonChangeProfile = view.findViewById(R.id.buttonChangeProfile);

        mButtonChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileChangeActivity.getIntent(getContext());
                startActivity(intent);
            }
        });

        mTextSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SettingsActivity.getIntent(getContext());
                startActivity(intent);
            }
        });
        mTextReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ReviewsActivity.getIntent(getContext(), getPresenter().getStateModel().getUserinfo().getUserId());
                startActivity(intent);
            }
        });
        mTextAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProfileAboutActivity.getIntent(getContext(),getPresenter().getStateModel().getUserinfo().getUserId());
                startActivity(intent);
            }
        });

        mTextRaitingClient = view.findViewById(R.id.text_raitingClient);
        mTextRaitingExecutor = view.findViewById(R.id.text_raitingExecutor);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().download();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Boolean bool) {
        getPresenter().updateProfile();
    }
}
