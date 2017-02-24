package com.fefeyo.otasuke.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.items.EventDetail;
import com.fefeyo.otasuke.items.EventType;

/**
 * Created by fefe on 2017/02/23.
 */

public class CommitEventAdapter extends ArrayAdapter<EventDetail> {

    private LayoutInflater mInflater;

    public CommitEventAdapter(Context context) {
        super(context, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = mInflater.inflate(R.layout.commit_list_row, null);

        EventDetail item = getItem(position);
        TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
        TextView eventPlace = (TextView) convertView.findViewById(R.id.event_place);
        TextView eventDate = (TextView) convertView.findViewById(R.id.event_date);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.event_description);
        eventName.setText(EventType.getType(item.getEventType()).getName());
        eventPlace.setText(item.getPlace());
        eventDate.setText(item.getDate());
        if (item.getDescription().isEmpty()) {
            eventDescription.setVisibility(View.GONE);
        } else {
            eventDescription.setText(item.getDescription());
        }

        return convertView;
    }
}
