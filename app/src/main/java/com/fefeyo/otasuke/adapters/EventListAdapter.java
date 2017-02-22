package com.fefeyo.otasuke.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fefeyo.otasuke.R;
import com.fefeyo.otasuke.items.Event;
import com.fefeyo.otasuke.items.EventType;

/**
 * Created by fefe on 2017/02/21.
 */

public class EventListAdapter extends ArrayAdapter<Event>{

    private LayoutInflater mInflater;

    public EventListAdapter(Context context) {
        super(context, 0);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) convertView = mInflater.inflate(R.layout.event_list_row, null);

        Event event = getItem(position);
        TextView eventNameText = (TextView)convertView.findViewById(R.id.event_name);

        ImageView early = (ImageView)convertView.findViewById(R.id.early);
        ImageView release = (ImageView)convertView.findViewById(R.id.release);
        ImageView fanmeeting = (ImageView)convertView.findViewById(R.id.fanmeeting);
        ImageView live = (ImageView)convertView.findViewById(R.id.live);
        ImageView date = (ImageView)convertView.findViewById(R.id.date);
        ImageView others = (ImageView)convertView.findViewById(R.id.others);
        ImageView[] icons = {
                early,
                release,
                fanmeeting,
                live,
                date,
                others
        };

        for(ImageView imageView : icons) {
            imageView.setVisibility(View.INVISIBLE);
        }

        eventNameText.setText(event.getEventName());
        for(int i = 0; i < event.getEventDetails().size(); i++) {
            switch (EventType.getType(event.getEventDetails().get(i).getEventType())) {
                case DATE:
                    date.setVisibility(View.VISIBLE);
                    break;
                case RELEASE:
                    release.setVisibility(View.VISIBLE);
                    break;
                case EARLY:
                    early.setVisibility(View.VISIBLE);
                    break;
                case FANMEETING:
                    fanmeeting.setVisibility(View.VISIBLE);
                    break;
                case LIVE:
                    live.setVisibility(View.VISIBLE);
                    break;
                case OTHERS:
                    others.setVisibility(View.VISIBLE);
                    break;
            }
        }

        return convertView;
    }
}
