package com.parsclass.android.alltolearn.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.parsclass.android.alltolearn.R;

public class SelectSortDialog extends DialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String selectedItem;
    private SortDialogListener listener;
    private String TAG="SelectSortDialog";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.select_sort_dialog, null);
        radioGroup=view.findViewById(R.id.radios);
        builder.setView(view)
        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int checkId=radioGroup.getCheckedRadioButtonId();
                if(checkId!=-1) {
                    radioButton = view.findViewById(checkId);
                    selectedItem = radioButton.getText().toString();
                }
                listener.onTypeSortChanged(selectedItem);

            }
        });



        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SortDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    public interface SortDialogListener {
        void onTypeSortChanged(String selectedItem);
    }
}
