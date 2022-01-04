package ru.bstu.it41.service.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ru.bstu.it41.service.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Герман on 29.04.2018.
 */

public class ChoicePhotoFragment extends DialogFragment {

    public static final String EXTRA_CHOICE = "ru.bstu.it41.service.profile.choice_photo";
    public static final int CAMERA = 1;
    public static final int GALLERY = 2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.choice_photo_dialog, null);

        TextView mTextGallery = v.findViewById(R.id.text_gallery);
        TextView mTextCamera = v.findViewById(R.id.text_camera);

        mTextGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOICE, GALLERY);
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                dismiss();
            }
        });

        mTextCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CHOICE, CAMERA);
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity()).setView(v)
                .create();
    }
}
