package ru.bstu.it41.service.test;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.databinding.LoginFragmentBinding;
import ru.bstu.it41.service.profile.ProfileFragment;

/**
 * Created by Герман on 08.10.2017.
 */

public class LoginTestFragment extends ReampFragment<LoginFragmentPresenter, LoginFragmentState> {
    //Элементы управления
    EditText editLogin;
    EditText editPassword;
    Button buttonLogin;
    //view-шки
    TextView textFailedLogin;
    TextView textSuccessLogin;
    TextView textLostConnection;
    TextView message;
    MainActivity parent;
    //
    ProgressBar progressBar;

    LoginFragmentBinding mBinding;

    @Override
    public void onStateChanged(LoginFragmentState stateModel) {
        //editLogin.setText(stateModel.getLogin());
        //editPassword.setText(stateModel.getPassword());

        buttonLogin.setEnabled(stateModel.isLoginActionEnabled());

        textFailedLogin.setVisibility(stateModel.isFailedLogin() ? View.VISIBLE : View.INVISIBLE);
        textSuccessLogin.setVisibility(stateModel.isSuccessLogin() ? View.VISIBLE : View.INVISIBLE);

        textLostConnection.setVisibility(stateModel.getLostConnection() ? View.VISIBLE : View.INVISIBLE);

        // progressBar.setVisibility(stateModel.getShowProgress()?View.VISIBLE:View.INVISIBLE);

        parent.getPresenter().onAuthorizedChanged(stateModel.isSuccessLogin());
        mBinding.notifyChange();
    }

    @Override
    public ReampPresenter<LoginFragmentState> onCreatePresenter() {

        return new LoginFragmentPresenter();
    }

    @Override
    public LoginFragmentState onCreateStateModel() {
        LoginFragmentState state = new LoginFragmentState();
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        mBinding.setViewModel(getPresenter());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parent = (MainActivity) getActivity();

        editLogin = (EditText) view.findViewById(R.id.editLogin);
        editPassword = (EditText) view.findViewById(R.id.editPassword);
        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);

        textFailedLogin = (TextView) view.findViewById(R.id.text_login_error);
        textSuccessLogin = (TextView) view.findViewById(R.id.text_login_success);
        textLostConnection = (TextView) view.findViewById(R.id.text_lost_connection);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        editLogin.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().loginChanged(charSequence.toString());
            }
        });
        editPassword.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().passwordChanged(charSequence.toString());
            }
        });

        editLogin.setText(getPresenter().getStateModel().getLogin());
        editPassword.setText(getPresenter().getStateModel().getPassword());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().login();
            }
        });
    }

    public void toProfile() {
        Fragment fragmentProfile = new ProfileFragment();

        ((MainActivity) getActivity()).replaceFragment(fragmentProfile, null,
                ((MainActivity) getActivity()).STACK_MAIN, R.id.container, true, R.string.title_profile);
    }
}


