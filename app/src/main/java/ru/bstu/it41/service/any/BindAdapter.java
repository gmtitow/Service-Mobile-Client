package ru.bstu.it41.service.any;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.Button;

/**
 * Created by Герман on 14.05.2018.
 */

public class BindAdapter {
    @BindingAdapter("android:visibility")
    public static void bindViewVisible(View view, boolean show) {
        view.setVisibility(show?View.VISIBLE:View.GONE);
    }
}
