package ru.bstu.it41.service.register;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.CustomTypefaceSpan;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;

import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class RegisterFragment extends ReampFragment<RegisterFragmentPresenter, RegisterFragmentState> {
    //Элементы управления
    Button buttonLogin;
    EditText editFirstname;
    EditText editLastname;
    EditText editEmail;
    EditText editTelephone;
    //EditText editAddress_1;
    //EditText editCity;
    //Spinner listCountry;
    //Spinner listZone;
    Spinner listMale;
    EditText editPassword;
    EditText editConfirm;
    //CheckBox checkAgree;
    //view-шки
    TextView message;
    TextView textSuccessLogin;
    TextView textLostConnection;
    TextView textFailedLogin;
    TextView textFailedLocal;
    TextView textAlreadyExists;

    MainActivity parent;
    //
    ProgressBar progressBar;

    @Override
    public void onStateChanged(RegisterFragmentState stateModel) {
        //Цвет будет красным там, где ошибка, все элементарно.

        //editConfirm.setBackgroundColor(stateModel.isFail_confirm()? Color.RED:Color.WHITE);

        //textFailedLocal.setVisibility(!stateModel.allOK()?View.VISIBLE:View.INVISIBLE);
        textFailedLocal.setVisibility(stateModel.isFail_confirm()?View.VISIBLE:View.INVISIBLE);
        textAlreadyExists.setVisibility(stateModel.showAlreadyExists()?View.VISIBLE:View.INVISIBLE);

        progressBar.setVisibility(stateModel.getShowProgress()?View.VISIBLE:View.INVISIBLE);
        textFailedLogin.setVisibility(stateModel.showFailedLogin()?View.VISIBLE:View.INVISIBLE);
        textSuccessLogin.setVisibility(stateModel.showSuccessLogin()?View.VISIBLE:View.INVISIBLE);

        parent.getPresenter().onAuthorizedChanged(stateModel.showSuccessLogin());

        listMale.setSelection(stateModel.getMale());

        textLostConnection.setVisibility(stateModel.getLostConnection()?View.VISIBLE:View.INVISIBLE);

        //Мегакрутая отладка
        message.setText(stateModel.message);
    }
    @Override
    public ReampPresenter<RegisterFragmentState> onCreatePresenter() {
        return new RegisterFragmentPresenter();
    }

    @Override
    public RegisterFragmentState onCreateStateModel() {
        return new RegisterFragmentState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editFirstname = (EditText)view.findViewById(R.id.edit_firstname);
        editLastname = (EditText)view.findViewById(R.id.edit_lastname);
        editEmail = (EditText)view.findViewById(R.id.edit_email);
        editTelephone = (EditText)view.findViewById(R.id.edit_telephone);
        //editAddress_1 = (EditText)view.findViewById(R.id.edit_address_1);
       // editCity = (EditText)view.findViewById(R.id.edit_city);

        listMale = view.findViewById(R.id.edit_male);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,new String[]{
                "Женский","Мужской"
        });
        listMale.setAdapter(adapter);

        parent = (MainActivity) getActivity();

        editPassword = (EditText)view.findViewById(R.id.edit_password);
        editConfirm = (EditText)view.findViewById(R.id.edit_confirm);

        textFailedLogin = (TextView)view.findViewById(R.id.text_login_error);
        textSuccessLogin = (TextView)view.findViewById(R.id.text_login_success);
        textLostConnection = (TextView)view.findViewById(R.id.text_lost_connection);
        textFailedLocal = view.findViewById(R.id.text_local_error);
        textAlreadyExists= view.findViewById(R.id.text_already_exists);

        buttonLogin = (Button)view.findViewById(R.id.buttonLogin);

        //checkAgree = (CheckBox)view.findViewById(R.id.checkBox_agree);

        message = (TextView)view.findViewById(R.id.text_message);

        progressBar = (ProgressBar)view.findViewById(R.id.progress);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().register();
            }
        });

        //Куча однотипных обработчиков. Как же это достало...
        editFirstname.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().firstnameChanged(charSequence.toString());
            }
        });
        editLastname.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().lastnameChanged(charSequence.toString());
            }
        });
        editEmail.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().emailChanged(charSequence.toString());
            }
        });
        editTelephone.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().telephoneChanged(charSequence.toString());
            }
        });

        editPassword.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().passwordChanged(charSequence.toString());
            }
        });
        editConfirm.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().confirmChanged(charSequence.toString());
            }
        });

        listMale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPresenter().maleChanged((byte)i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }
}
