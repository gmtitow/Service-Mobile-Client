package ru.bstu.it41.service.profile.review.view;

import java.util.ArrayList;
import java.util.List;

import etr.android.reamp.mvp.SerializableStateModel;
import ru.bstu.it41.service.models.ReviewAndUserinfo;

/**
 * Created by Герман on 18.10.2017.
 */

public class ReviewViewFragmentState extends SerializableStateModel {
    private ReviewAndUserinfo mReviewAndUserinfo;

    public ReviewAndUserinfo getReviewAndUserinfo() {
        return mReviewAndUserinfo;
    }

    public void setReviewAndUserinfo(ReviewAndUserinfo reviewAndUserinfo) {
        mReviewAndUserinfo = reviewAndUserinfo;
    }
}
