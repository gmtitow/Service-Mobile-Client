package ru.bstu.it41.service.tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.bstu.it41.service.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Герман on 29.04.2018.
 */

public class DialogTaskDeleteFragment extends DialogFragment {

    public static final String EXTRA_DELETE = "ru.bstu.it41.service.profile.delete_task";
    public static final int DELETE_OK = 1;
    public static final int DELETE_CANCEL = 2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView mTextView = new TextView(getContext());
        mTextView.setText(R.string.task_dialog_delete_text);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.task_dialog_delete_title)
                .setView(mTextView)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_DELETE, DELETE_OK);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK,intent);
                        dismiss();
                    }
                }).setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_DELETE, DELETE_CANCEL);
                        getTargetFragment().onActivityResult(getTargetRequestCode(),RESULT_CANCELED,intent);
                        dismiss();
                    }
                }).create();
    }
}
