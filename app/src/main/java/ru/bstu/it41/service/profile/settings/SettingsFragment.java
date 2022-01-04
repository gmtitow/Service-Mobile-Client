package ru.bstu.it41.service.profile.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.activeandroid.query.Select;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.models.Settings;
import ru.bstu.it41.service.models.User;


public class SettingsFragment extends ReampFragment<SettingsFragmentPresenter, SettingsFragmentState>{

    private int currentTitle = R.string.title_settings;
    private int changingTitle = R.string.title_settings_changing;


    private Switch mSwitchEmail;
    private Switch mSwitchSms;
    private Switch mSwitchPush;
    private TextView mTextEmail;
    private TextView mTextPhone;
    private Button mButtonChangeSettings;

    @Override
    public void onStateChanged(SettingsFragmentState stateModel) {


        mButtonChangeSettings.setVisibility(stateModel.isSettingsChanged()?View.VISIBLE:View.GONE);
        if(stateModel.isProgressChange()){
            mButtonChangeSettings.setEnabled(false);
            getActivity().setTitle(changingTitle);
        }
        else {
            mButtonChangeSettings.setEnabled(true);
            getActivity().setTitle(currentTitle);
        }
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public ReampPresenter<SettingsFragmentState> onCreatePresenter() {
            return new SettingsFragmentPresenter();
    }

    @Override
    public SettingsFragmentState onCreateStateModel() {
        SettingsFragmentState state = new SettingsFragmentState();

        state.setTempSettings(new Select().from(Settings.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).<Settings>executeSingle());
        state.setUser(new Select().from(User.class).where("userId = ?",
                DataStore.getUserId(getContext().getApplicationContext())).<User>executeSingle());

        state.setSettings(new Settings());
        state.getSettings().setNotificationEmail(state.getTempSettings().getNotificationEmail());
        state.getSettings().setNotificationSms(state.getTempSettings().getNotificationSms());
        state.getSettings().setNotificationPush(state.getTempSettings().getNotificationPush());

        return state;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSwitchEmail = view.findViewById(R.id.switch_email);
        mSwitchSms = view.findViewById(R.id.switch_sms);
        mSwitchPush = view.findViewById(R.id.switch_push);
        mTextEmail = view.findViewById(R.id.text_email);
        mTextPhone = view.findViewById(R.id.text_phone);

        mSwitchEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPresenter().getStateModel().getTempSettings().setNotificationEmail((byte)(isChecked?1:0));
                getPresenter().sendStateModel();
            }
        });

        mSwitchSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPresenter().getStateModel().getTempSettings().setNotificationSms((byte)(isChecked?1:0));
                getPresenter().sendStateModel();
            }
        });

        mSwitchPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getPresenter().getStateModel().getTempSettings().setNotificationPush((byte)(isChecked?1:0));
                getPresenter().sendStateModel();
            }
        });
        mButtonChangeSettings = view.findViewById(R.id.buttonChangeSettings);

        mButtonChangeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().changeSettings();
            }
        });

        //Инициализация
        mSwitchEmail.setChecked(getPresenter().getStateModel().getTempSettings().getNotificationEmail()==1);
        mSwitchSms.setChecked(getPresenter().getStateModel().getTempSettings().getNotificationSms()==1);
        mSwitchPush.setChecked(getPresenter().getStateModel().getTempSettings().getNotificationPush()==1);

        mTextEmail.setText(getPresenter().getStateModel().getUser().getEmail());
        mTextPhone.setText(getPresenter().getStateModel().getUser().getPhone());
    }

}
