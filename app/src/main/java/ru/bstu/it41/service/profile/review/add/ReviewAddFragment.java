package ru.bstu.it41.service.profile.review.add;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.activeandroid.query.Select;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.any.DataStore;
import ru.bstu.it41.service.databinding.FragmentReviewAddBinding;
import ru.bstu.it41.service.models.ReviewAndUserinfo;
import ru.bstu.it41.service.models.Reviews;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.review.ReviewFragmentPresenter;
import ru.bstu.it41.service.profile.review.ReviewFragmentState;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewAddFragment extends ReampFragment<ReviewAddFragmentPresenter, ReviewAddFragmentState> {
    //Элементы управления

    private static String USER_ID_KEY = "ru.bstu.it41.service.profile.review.add.object_user_id";

    private static String TENDER_ID_KEY = "ru.bstu.it41.service.profile.review.add.tender_id";

    //private ProgressBar mProgressBar;

    private FragmentReviewAddBinding mBinding;

    @Override
    public void onStateChanged(ReviewAddFragmentState stateModel) {

        getActivity().setTitle("Ваш отзыв");
        String executorStr = stateModel.isObjectClient()?"заказчика":"исполнителя";

        mBinding.textRating.setText(getString(R.string.please_put_rating,executorStr));

        mBinding.addReview.setEnabled(stateModel.getReview().getRaiting()!=-1 &&
                !stateModel.getReview().getTextReview().trim().equals(""));
    }

    @Override
    public ReampPresenter<ReviewAddFragmentState> onCreatePresenter() {
        return new ReviewAddFragmentPresenter();
    }

    @Override
    public ReviewAddFragmentState onCreateStateModel() {
        ReviewAddFragmentState state = new ReviewAddFragmentState();
        if (this.getArguments() != null && this.getArguments().containsKey(USER_ID_KEY)
                && this.getArguments().containsKey(TENDER_ID_KEY)) {

            state.setTenderId(getArguments().getInt(TENDER_ID_KEY));

            state.setObject(new Select().from(Userinfo.class).where("userId = ?",
                    getArguments().getInt(USER_ID_KEY)).executeSingle());

            Tender tender = new Select().from(Tender.class).where("tenderId = ?",
                    getArguments().getInt(TENDER_ID_KEY)).executeSingle();

            Tasks task = new Select().from(Tasks.class).where("taskId = ?",
                    tender.getTaskId()).<Tasks>executeSingle();

            if(task.getUserId() == DataStore.getUserId(getContext().getApplicationContext())){
                state.setObjectClient(false);
            }
            else{
                state.setObjectClient(true);
            }

            state.setReview(new Reviews());
            state.getReview().setRaiting(-1);
            state.getReview().setExecutor(state.isObjectClient()?0:1);
            state.getReview().setTextReview("");
        }
        return state;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_add, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rating.setIsIndicator(false);

        mBinding.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    getPresenter().getStateModel().getReview().setRaiting(Math.round(rating)*2);
                    //getPresenter().sendStateModel();

            }
        });

        mBinding.addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().addReview();
            }
        });

        mBinding.textReview.addTextChangedListener(new MainActivity.SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPresenter().getStateModel().getReview().setTextReview(charSequence.toString());
                getPresenter().sendStateModel();
            }
        });
    }

    public static ReviewAddFragment getInstance(int userId, int tenderId){
        ReviewAddFragment fragment = new ReviewAddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID_KEY,userId);
        bundle.putInt(TENDER_ID_KEY,tenderId);

        fragment.setArguments(bundle);
        return fragment;
    }
}
