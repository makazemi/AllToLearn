package com.parsclass.android.alltolearn.Utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.parsclass.android.alltolearn.R;


public class speedPlaybackDialog extends DialogFragment {

    public static final String TAG="speedPlaybackDialog";

     String[] speeds;
    private String speed;
    NoticeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        speeds=getActivity().getResources().getStringArray(R.array.speed_playback);

//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.speed_playback_dialog, null));


        builder.setTitle(getString(R.string.title_playback_speed_dialog));
        builder.setSingleChoiceItems(R.array.speed_playback, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                speed=speeds[which];
                listener.onSpeedChange(speed);
                Log.e(TAG,"which: "+which);
                try {
                    Thread.sleep(1000);
                    dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });


        return builder.create();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(" must implement NoticeDialogListener");
        }
    }

    public interface NoticeDialogListener {
         void onSpeedChange(String speed);
    }

}
