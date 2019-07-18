package com.parsclass.android.alltolearn.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.parsclass.android.alltolearn.R;

public class SelectFilterDialog extends DialogFragment {

    private RadioGroup radioGroupCategory, radioGroupDuration;
    private RadioButton radioButtonPrice, radioButtonCategory, radioButtonDuration;
    private Button btnApply, btnReset;
    private String category, duration;
    private boolean price;
    private FilterDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.select_filter_dialog, null);
        radioButtonPrice = view.findViewById(R.id.radioBtnPrice);
        radioGroupCategory = view.findViewById(R.id.radioGroupCategory);
        radioGroupDuration = view.findViewById(R.id.radioGroupDuration);
        btnApply = view.findViewById(R.id.btnApply);
        btnReset = view.findViewById(R.id.btnReset);
        builder.setView(view);

        radioButtonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButtonPrice.isSelected()) {
                    radioButtonPrice.setChecked(true);
                    radioButtonPrice.setSelected(true);
                } else {
                    radioButtonPrice.setChecked(false);
                    radioButtonPrice.setSelected(false);
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = radioButtonPrice.isChecked();
                int categoryId = radioGroupCategory.getCheckedRadioButtonId();
                if (categoryId != -1) {
                    radioButtonCategory = view.findViewById(categoryId);
                    category = radioButtonCategory.getText().toString();
                }
                int durationId = radioGroupDuration.getCheckedRadioButtonId();
                if (durationId != -1) {
                    radioButtonDuration = view.findViewById(durationId);
                    duration = radioButtonDuration.getText().toString();
                }
                listener.onFilterChanged(price, category, duration);

                dismiss();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonPrice.setChecked(false);
                radioGroupCategory.clearCheck();
                radioGroupDuration.clearCheck();

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    public interface FilterDialogListener {
        void onFilterChanged(boolean price, String category, String duration);
    }

    public void addRadioButtons(int number) {
        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(getContext());
                rdbtn.setId(View.generateViewId());
                rdbtn.setText("Radio " + rdbtn.getId());
                ll.addView(rdbtn);
            }
            //((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
        }
    }
}

