package ru.bstu.it41.service.offers.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.Locale;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.FragmentRetainer;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.OtherTender;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.tenders.tenderView.TenderViewFragment;

import static java.lang.Integer.parseInt;

/**
 * Created by Герман on 18.10.2017.
 */

public class OffersAddFragment extends ReampFragment<OfferAddFragmentPresenter, OfferAddFragmentState> {
    private int currentTitle = R.string.title_offers_add;
    private int inProgressTitle = R.string.title_offers_add_in_progress;

    private static String TENDER_ID_KEY = "ru.bstu.it41.service.offerAdd_tender_id";
    private static String TENDER_INFO_KEY = "ru.bstu.it41.service.offerAdd_task_info";
    private static String TENDER_OTHER_TEMP_KEY = "ru.bstu.it41.service.offerAdd_othertender_info";
    //Элементы управления
    private TextView mTextTaskDeadline;
    private TextView mTextTaskPrice;
    private TextView mTextTaskDescription;

    private Button mButtonOfferDeadline;
    private EditText mEditOfferPrice;
    private EditText mEditOfferDescription;

    private Button mButtonAddOffer;

    //private ProgressBar mProgressBar;

    //Переменные
    Calendar mTemporaryDeadline;
    //
    TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mTemporaryDeadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mTemporaryDeadline.set(Calendar.MINUTE, minute);

            mTemporaryDeadline.set(Calendar.SECOND, 0);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.SECOND, 0);

            if (mTemporaryDeadline.getTime().getTime() > today.getTime().getTime()) {
                getPresenter().getStateModel().getTempOffer().setDeadline(mTemporaryDeadline.getTime());
                getPresenter().sendStateModel();
            } else
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

            if (getPresenter().getStateModel().getTempOffer().getCalendarDeadline() != null)
                new TimePickerDialog(getContext(), mOnTimeSetListener,
                        getPresenter().getStateModel().getTempOffer().getCalendarDeadline().get(Calendar.HOUR),
                        getPresenter().getStateModel().getTempOffer().getCalendarDeadline().get(Calendar.MINUTE),
                        true)
                        .show();
            else {
                new TimePickerDialog(getContext(), mOnTimeSetListener,
                        12,
                        0,
                        true)
                        .show();
            }

        }
    };

    @Override
    public void onStateChanged(OfferAddFragmentState stateModel) {
        getActivity().setTitle(currentTitle);

        mTextTaskPrice.setText(String.valueOf(stateModel.getCurrentTask().getPrice()));
        mTextTaskDescription.setText(stateModel.getCurrentTask().getDescription());
        mTextTaskDeadline.setText(getPresenter().mFormatForDate.format(stateModel.getCurrentTask().getDeadline()));

        mButtonOfferDeadline.setText((stateModel.getTempOffer().getDeadline() == null) ? "Не указано" : getPresenter().mFormatForDate.format(stateModel.getTempOffer().getDeadline()));
        mEditOfferDescription.setText(stateModel.getTempOffer().getDescription());

        mButtonAddOffer.setEnabled(stateModel.allowAddOffer());
    }

    @Override
    public ReampPresenter<OfferAddFragmentState> onCreatePresenter() {
        return new OfferAddFragmentPresenter();
    }

    @Override
    public OfferAddFragmentState onCreateStateModel() {
        OfferAddFragmentState state = new OfferAddFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(TENDER_ID_KEY)) {
            Tender tender = new Select().from(Tender.class).where("tenderId = ?",
                    getArguments().getInt(TENDER_ID_KEY)).executeSingle();

            state.setTempOffer(new Offer());

            state.getTempOffer().setTenderId(tender.getTenderId());

            state.setCurrentTask(new Select().from(Tasks.class).where("taskId = ?", tender.getTaskId())
                    .<Tasks>executeSingle());
        }

        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextTaskPrice = view.findViewById(R.id.text_task_price);
        mTextTaskDeadline = view.findViewById(R.id.text_task_deadline);
        mTextTaskDescription = view.findViewById(R.id.text_task_description);

        mEditOfferPrice = view.findViewById(R.id.edit_offer_price);
        mButtonOfferDeadline = view.findViewById(R.id.button_offer_deadline);
        mEditOfferDescription = view.findViewById(R.id.edit_offer_description);

        mButtonAddOffer = view.findViewById(R.id.button_add_offer);

        mButtonAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().addOffer();
            }
        });

        mEditOfferDescription.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getTempOffer().setDescription(charSequence.toString());
            }
        });

        InputFilter customFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5) {
                if (!isNumber(arg0.toString()))
                    return "";
                return null;
            }
        };

        mEditOfferPrice.setFilters(new InputFilter[]{customFilter});

        mEditOfferPrice.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isNumber(charSequence.toString())) {
                    getPresenter().getStateModel().getTempOffer().setPrice(parseInt(charSequence.toString()));
                } else
                    getPresenter().getStateModel().getTempOffer().setPrice(0);

                getPresenter().sendStateModel();
            }
        });

        mButtonOfferDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPresenter().getStateModel().getTempOffer().getCalendarDeadline() != null)
                    new DatePickerDialog(getContext(), mOnDateSetListener,
                            getPresenter().getStateModel().getTempOffer().getCalendarDeadline().get(Calendar.YEAR),
                            getPresenter().getStateModel().getTempOffer().getCalendarDeadline().get(Calendar.MONTH),
                            getPresenter().getStateModel().getTempOffer().getCalendarDeadline().get(Calendar.DAY_OF_MONTH))
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
    }

    private boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static OffersAddFragment getInstance(int tenderId) {
        OffersAddFragment fragment = new OffersAddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TENDER_ID_KEY, tenderId);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void toOffers(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(FragmentRetainer.EXTRA_FRAGMENT,FragmentRetainer.FRAGMENT_OFFERS);
        startActivity(intent);
        getActivity().finish();
    }
}
