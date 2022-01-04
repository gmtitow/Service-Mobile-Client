package ru.bstu.it41.service.tasks.change;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.tasks.TaskFragmentPresenter;
import ru.bstu.it41.service.tasks.TaskFragmentState;
import ru.bstu.it41.service.tasks.map.ChooseLocationActivity;
import ru.bstu.it41.service.tasks.map.ChooseLocationFragment;
import ru.bstu.it41.service.tasks.view.TaskViewFragment;

import static android.app.Activity.RESULT_OK;
import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class TaskChangeFragment extends ReampFragment<TaskChangeFragmentPresenter, TaskChangeFragmentState> {

    private static final int REQUEST_CHOICE_LOCATION = 188;
    private int currentTitle = R.string.title_task_change;
    //Элементы управления
    private static String TASK_ID_KEY = "ru.bstu.it41.service.tasks_task_id";

    private EditText mEditName;
    private EditText mEditDescription;
    private Button mEditDeadline;
    private Spinner mSpinnerCategory;
    private EditText mEditPrice;
    private Button mButtonPlace;
    private EditText mEditAddress;
    private Button mButtonFurther;


    //Переменные
    private Calendar mTemporaryDeadline;
    private ArrayAdapter<Category> adapter;
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
                getPresenter().getStateModel().getCurrentTask().setDeadline(mTemporaryDeadline.getTime());

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

            if (getPresenter().getStateModel().getCurrentTask().getDeadline() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getPresenter().getStateModel().getCurrentTask().getDeadline());

                new TimePickerDialog(getContext(), mOnTimeSetListener,
                        calendar.get(Calendar.HOUR),
                        calendar.get(Calendar.MINUTE),
                        true)
                        .show();
            }
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
    public void onStateChanged(TaskChangeFragmentState stateModel) {
        getActivity().setTitle(currentTitle);
        mEditDeadline.setText(stateModel.getCurrentTask().getDeadline() != null ?
                getPresenter().mFormatForDate.format(stateModel.getCurrentTask().getDeadline())
                : "не указано");

        mButtonFurther.setEnabled(stateModel.allowNext());
    }

    @Override
    public ReampPresenter<TaskChangeFragmentState> onCreatePresenter() {
            return new TaskChangeFragmentPresenter();
    }

    @Override
    public TaskChangeFragmentState onCreateStateModel() {
        TaskChangeFragmentState state = new TaskChangeFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(TASK_ID_KEY)) {
            state.setCurrentTask(new Select().from(Tasks.class).where("taskId = ?",
                    getArguments().getInt(TASK_ID_KEY)).<Tasks>executeSingle());
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_change, container, false);
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
                getPresenter().changeTask();
            }
        });


        adapter = new ArrayAdapter<Category>(getContext(), android.R.layout.simple_spinner_item,
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

                getPresenter().getStateModel().getCurrentTask().setCategoryId(category.getCategoryId());
                getPresenter().sendStateModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEditName.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getCurrentTask().setName(charSequence.toString());
                getPresenter().sendStateModel();
            }
        });
        mEditDescription.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getCurrentTask().setDescription(charSequence.toString());
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
                    getPresenter().getStateModel().getCurrentTask().setPrice(parseInt(charSequence.toString()));
                }
                else
                    getPresenter().getStateModel().getCurrentTask().setPrice(0);

                getPresenter().sendStateModel();
            }
        });
        mEditAddress.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getCurrentTask().setAddress(charSequence.toString());
            }
        });

        mEditDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getStateModel().getCurrentTask().getDeadline() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(getPresenter().getStateModel().getCurrentTask().getDeadline());
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
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

        //Инициализация
        mEditDeadline.setText(getPresenter().getStateModel().getCurrentTask().getDeadline() != null ?
                getPresenter().mFormatForDate.format(getPresenter().getStateModel().getCurrentTask().getDeadline())
                : "не указано");

        //mEditName.setText(stateModel.getCurrentName());
        mButtonFurther.setEnabled(getPresenter().getStateModel().allowNext());

        mEditName.setText(getPresenter().getStateModel().getCurrentTask().getName());
        mEditDescription.setText(getPresenter().getStateModel().getCurrentTask().getDescription());
        mEditDeadline.setText(getPresenter().mFormatForDate.format(getPresenter().getStateModel().getCurrentTask().getDeadline()));

        Category category = new Select().from(Category.class).where("categoryId = ?",
                getPresenter().getStateModel().getCurrentTask().getCategoryId()).executeSingle();
        mSpinnerCategory.setSelection(adapter.getPosition(category));

        mEditPrice.setText(String.valueOf(getPresenter().getStateModel().getCurrentTask().getPrice()));
        mEditAddress.setText(String.valueOf(getPresenter().getStateModel().getCurrentTask().getAddress()));


        mButtonPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChooseLocationActivity.getIntent(getContext(),
                        getPresenter().getStateModel().getCurrentTask().getLatitude(),
                        getPresenter().getStateModel().getCurrentTask().getLongitude(),
                        getPresenter().getStateModel().getCurrentTask().getAddress());

                startActivityForResult(intent,REQUEST_CHOICE_LOCATION);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOICE_LOCATION) {
            if (resultCode == RESULT_OK) {

                Double lat = data.getDoubleExtra(ChooseLocationFragment.EXTRA_LATITUDE, 0);
                Double lon = data.getDoubleExtra(ChooseLocationFragment.EXTRA_LONGITUDE, 0);

                getPresenter().getStateModel().getCurrentTask().setLatitude(lat);
                getPresenter().getStateModel().getCurrentTask().setLongitude(lon);

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
    }

    public void toViewTask(){
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    public static TaskChangeFragment getInstance(int taskId){
        TaskChangeFragment fragment = new TaskChangeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TASK_ID_KEY,taskId);

        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException  e){
            return false;
        }
    }
}
