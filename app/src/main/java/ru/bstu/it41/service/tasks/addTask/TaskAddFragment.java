package ru.bstu.it41.service.tasks.addTask;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Category;
import ru.bstu.it41.service.tasks.addTender.TaskAddTenderActivity;
import ru.bstu.it41.service.tasks.map.ChooseLocationActivity;
import ru.bstu.it41.service.tasks.map.ChooseLocationFragment;

import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskAddFragment extends ReampFragment<TaskAddFragmentPresenter, TaskAddFragmentState> {

    private static final int REQUEST_CHOICE_LOCATION = 188;
    private static final int REQUEST_ADD_TENDER= 134;
    private static final String DIALOG_CHOICE = "DialogChoiceLocation";

    private int currentTitle = R.string.title_task_add;
    private int creatingTitle = R.string.title_task_creating;
    //Элементы управления

    private EditText mEditName;
    private EditText mEditDescription;
    private Button mEditDeadline;
    private Spinner mSpinnerCategory;
    private EditText mEditPrice;
    private EditText mEditAddress;
    private Button mButtonPlace;
    private Button mButtonFurther;


    //Переменные
    Calendar mTemporaryDeadline;
    private Fragment mThisFragment = this;
    //
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTemporaryDeadline.set(Calendar.HOUR_OF_DAY,hourOfDay);
            mTemporaryDeadline.set(Calendar.MINUTE,minute);

            mTemporaryDeadline.set(Calendar.SECOND, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.SECOND,0);

            if(mTemporaryDeadline.getTime().getTime() > today.getTime().getTime())
            {
                getPresenter().getStateModel().setCurrentDeadline(mTemporaryDeadline);

                getPresenter().sendStateModel();
            }
            else
                Toast.makeText(getContext(), R.string.task_deadline_error, Toast.LENGTH_SHORT).show();
        }
    };

    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mTemporaryDeadline = Calendar.getInstance(Locale.getDefault());
            mTemporaryDeadline.set(Calendar.YEAR, year);
            mTemporaryDeadline.set(Calendar.MONTH, month);
            mTemporaryDeadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (getPresenter().getStateModel().getCurrentDeadline() != null)
                new TimePickerDialog(getContext(),mOnTimeSetListener,
                        getPresenter().getStateModel().getCurrentDeadline().get(Calendar.HOUR),
                        getPresenter().getStateModel().getCurrentDeadline().get(Calendar.MINUTE),
                        true)
                        .show();
            else {
                new TimePickerDialog(getContext(),mOnTimeSetListener,
                        12,
                        0,
                        true)
                        .show();
            }

        }
    };

    @Override
    public void onStateChanged(TaskAddFragmentState stateModel) {
        if(stateModel.isCreatingInProgress())
            getActivity().setTitle(creatingTitle);
        else
            getActivity().setTitle(currentTitle);
        mEditDeadline.setText(stateModel.getCurrentDeadline() != null ?
                getPresenter().mFormatForDate.format(stateModel.getCurrentDeadline().getTime())
                : "не указано");


        mButtonFurther.setEnabled(stateModel.allowNext());
    }

    @Override
    public ReampPresenter<TaskAddFragmentState> onCreatePresenter() {
            return new TaskAddFragmentPresenter();
    }

    @Override
    public TaskAddFragmentState onCreateStateModel() {
            return new TaskAddFragmentState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditName = view.findViewById(R.id.edit_name);
        mEditDescription = view.findViewById(R.id.edit_description);
        mEditDeadline = view.findViewById(R.id.edit_deadline);
        mSpinnerCategory = view.findViewById(R.id.spinner_category);

        mEditPrice = view.findViewById(R.id.edit_price);
        mEditAddress = view.findViewById(R.id.edit_address);
        mButtonFurther = view.findViewById(R.id.button_further);
        mButtonPlace = view.findViewById(R.id.button_place);

        mButtonFurther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPresenter().addTask();
            }
        });


       final ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getContext(), android.R.layout.simple_spinner_item,
                new Select().all().from(Category.class).<Category>execute()) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Category category = (Category) getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(android.R.layout.simple_list_item_1, null);
                }

                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(category.getCategoryName());

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Category category = (Category) getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(android.R.layout.simple_list_item_1, null);
                }

                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(category.getCategoryName());


                return convertView;
            }
        };

        mSpinnerCategory.setAdapter(adapter);

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getItemAtPosition(i);

                getPresenter().getStateModel().setCurrentCategory(category.getCategoryId());
                getPresenter().sendStateModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEditName.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().setCurrentName(charSequence.toString());
                getPresenter().sendStateModel();
            }
        });
        mEditDescription.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().setCurrentDescription(charSequence.toString());
                getPresenter().sendStateModel();
            }
        });

        mEditAddress.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().setCurrentAddress(charSequence.toString());
                getPresenter().sendStateModel();
            }
        });

        InputFilter customFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5){
                if(!isNumber(arg0.toString()))
                    return "";
                return null;
            }
        };

        mEditPrice.setFilters(new InputFilter[]{customFilter});

        mEditPrice.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isNumber(charSequence.toString())) {
                    getPresenter().getStateModel().setCurrentPrice(parseInt(charSequence.toString()));
                }
                else
                    getPresenter().getStateModel().setCurrentPrice(0);

                getPresenter().sendStateModel();
            }
        });

        mEditDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getStateModel().getCurrentDeadline() != null)
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            getPresenter().getStateModel().getCurrentDeadline().get(Calendar.YEAR),
                            getPresenter().getStateModel().getCurrentDeadline().get(Calendar.MONTH),
                            getPresenter().getStateModel().getCurrentDeadline().get(Calendar.DAY_OF_MONTH))
                            .show();
                else {
                    Calendar calendar = Calendar.getInstance();
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            }
        });

        mButtonPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChooseLocationActivity.getIntent(getContext(),
                        getPresenter().getStateModel().getCurrentLatitude(),
                        getPresenter().getStateModel().getCurrentLongitude(),
                        getPresenter().getStateModel().getCurrentAddress());

                startActivityForResult(intent,REQUEST_CHOICE_LOCATION);
            }
        });
    }

    public void toAddTender(int taskId){
        Intent intent = TaskAddTenderActivity.getIntent(getContext(),taskId);
        startActivityForResult(intent,REQUEST_ADD_TENDER);
    }

    private boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException  e){
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOICE_LOCATION) {
            if (resultCode == RESULT_OK) {

                Double lat = data.getDoubleExtra(ChooseLocationFragment.EXTRA_LATITUDE, 0);
                Double lon = data.getDoubleExtra(ChooseLocationFragment.EXTRA_LONGITUDE, 0);

                getPresenter().getStateModel().setCurrentLatitude(lat);
                getPresenter().getStateModel().setCurrentLongitude(lon);

                List<Address> addresses = null;
                Geocoder mGeocoder = new Geocoder(getContext().getApplicationContext());

                try {
                    addresses = mGeocoder.getFromLocation(lat, lon, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(addresses!=null && addresses.size()!=0){
                    Address addr = addresses.get(0);

                    /*String addressStr = addr.getAdminArea() + ", " + addr.getLocality() + ", " +
                            addr.getThoroughfare() + ", " + addr.getFeatureName();*/
                    String addressStr = addr.getAddressLine(0);
                    mEditAddress.setText(addressStr);
                }
                else {
                    mEditAddress.setText("");
                }
            }
        }
        else if(requestCode == REQUEST_ADD_TENDER){
                getActivity().finish();
        }
    }
}
