package ru.bstu.it41.service.profile.about;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.activeandroid.query.Select;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Userinfo;


public class ProfileAboutFragment extends ReampFragment<ProfileAboutFragmentPresenter, ProfileAboutFragmentState> {

    private static String USER_ID_KEY = "ru.bstu.it41.service.profile.about.user_id";
    private int currentTitle = R.string.title_profile;

    private EditText mEditAbout;
    private Button mButtonSave;
    private ProgressBar mProgress;

    private boolean programChangingEdit;

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onStateChanged(ProfileAboutFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        mButtonSave.setEnabled(!stateModel.getAbout().equals(stateModel.getTemporaryAbout()) &&
                stateModel.isSaveButtonEnabled());
        mProgress.setVisibility(stateModel.isShowProgress() ? View.VISIBLE : View.GONE);

        if (!stateModel.isMayBeChanged()) {
            disableEditText(mEditAbout);
            mButtonSave.setVisibility(View.GONE);
        } else {
            //mEditAbout.setFocusable(true);
            mButtonSave.setVisibility(View.VISIBLE);
        }
    }

    public ProfileAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public ReampPresenter<ProfileAboutFragmentState> onCreatePresenter() {
        return new ProfileAboutFragmentPresenter();
    }

    @Override
    public ProfileAboutFragmentState onCreateStateModel() {
        ProfileAboutFragmentState stateModel = new ProfileAboutFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(USER_ID_KEY)) {

            Userinfo userinfo = new Select().from(Userinfo.class).where("userId = ?",
                    getArguments().getInt(USER_ID_KEY)).executeSingle();

            stateModel.setAbout(userinfo.getAbout()!=null?userinfo.getAbout():"");
            stateModel.setTemporaryAbout(userinfo.getAbout());

            if(DataStore.getUserId(getContext().getApplicationContext()) == getArguments().getInt(USER_ID_KEY)){
                stateModel.setMayBeChanged(true);
            }
            else
                stateModel.setMayBeChanged(false);
        }

        return stateModel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mProgress = view.findViewById(R.id.progress);
        mEditAbout = view.findViewById(R.id.edit_about);
        mButtonSave = view.findViewById(R.id.buttonSaveChanges);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().saveAbout();
            }
        });

        mEditAbout.setText(getPresenter().getStateModel().getAbout());

        mEditAbout.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().setTemporaryAbout(charSequence.toString());
                getPresenter().updateView();
            }
        });
    }

    public static ProfileAboutFragment getInstance(int userId) {
        ProfileAboutFragment fragment = new ProfileAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID_KEY, userId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
