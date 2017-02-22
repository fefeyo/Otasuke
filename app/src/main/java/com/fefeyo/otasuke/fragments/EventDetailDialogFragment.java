package com.fefeyo.otasuke.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.items.EventDetail;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.event_detail, null);

        EditText place = (EditText)content.findViewById(R.id.place);
        EditText description = (EditText)content.findViewById(R.id.description);
        DatePicker datePicker = (DatePicker)content.findViewById(R.id.date);
        TimePicker timePicker = (TimePicker)content.findViewById(R.id.time);
        timePicker.setIs24HourView(true);

        builder.setView(content);

        builder.setMessage(arguments.getString("title"));
        builder.setPositiveButton("OK", ((dialogInterface, i) -> {
            EventDetail eventDetail = new EventDetail();
            eventDetail.setPlace(place.getText().toString());
            eventDetail.setDescription(description.getText().toString());
            eventDetail.setEventType(arguments.getInt("type"));
            eventDetail.setDate(datePicker.getYear() + "/" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth());
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                eventDetail.setTime(timePicker.getHour() + ":" + timePicker.getMinute());
            }else {
                eventDetail.setTime(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
            }
            mListener.onCommit(eventDetail);
        }));
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }
}
