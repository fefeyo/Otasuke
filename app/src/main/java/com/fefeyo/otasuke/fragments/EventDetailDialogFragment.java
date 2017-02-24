package com.fefeyo.otasuke.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.items.EventDetail;
import com.fefeyo.otasuke.items.EventType;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by fefe on 2017/02/22.
 */

public class EventDetailDialogFragment extends DialogFragment {

    private OnCommitListener mListener;

    public interface OnCommitListener {
        void onCommit(EventDetail eventDetail);
    }

    public void setOnCommitListener(OnCommitListener listener) {
        this.mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle arguments = getArguments();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.event_detail, null);

        EditText place = (EditText) content.findViewById(R.id.place);
        EditText description = (EditText) content.findViewById(R.id.description);
        DatePicker datePicker = (DatePicker) content.findViewById(R.id.date);
        TextView time_text = (TextView)content.findViewById(R.id.time_text);
        TimePicker timePicker = (TimePicker) content.findViewById(R.id.time);
        timePicker.setIs24HourView(true);

        place.setText(arguments.getString("place"));
        description.setText(arguments.getString("description"));

        if(arguments.getInt("year") != 0){
            datePicker.init(arguments.getInt("year"), arguments.getInt("month"), arguments.getInt("day"), null);
        }

        if(arguments.getInt("type") == EventType.DATE.getId()) {
            timePicker.setVisibility(View.GONE);
            time_text.setVisibility(View.GONE);
        }

        builder.setView(content);

        builder.setMessage(arguments.getString("title"));
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button button_comfirm = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            button_comfirm.setOnClickListener(v -> {
                EventDetail eventDetail = new EventDetail();
                String place_text = place.getText().toString();
                if (place_text.isEmpty()) {
                    place.setError("入力してください");
                    return;
                }
                eventDetail.setPlace(place.getText().toString());
                eventDetail.setDescription(description.getText().toString());
                eventDetail.setEventType(arguments.getInt("type"));
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                eventDetail.setDate(year + "/" + month + "/" + day);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    eventDetail.setTime(timePicker.getHour() + ":" + timePicker.getMinute());
                } else {
                    eventDetail.setTime(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                }
                mListener.onCommit(eventDetail);
                dialog.dismiss();
            });
        });

        return dialog;
    }
}
