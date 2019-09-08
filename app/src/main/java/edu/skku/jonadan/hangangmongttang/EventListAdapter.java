package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {
    private ArrayList<EventListItem> eventList = new ArrayList<>();

    public EventListAdapter(ArrayList<EventListItem> eventList) {
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_event, viewGroup, false);
        }

        TextView event_name = (TextView) view.findViewById(R.id.name);
        TextView event_place = (TextView) view.findViewById(R.id.place);
        TextView event_date = (TextView) view.findViewById(R.id.date);
        TextView event_time = (TextView) view.findViewById(R.id.time);

        EventListItem event = eventList.get(position);

        event_name.setText(event.getName());
        event_place.setText(event.getPlace());
        event_date.setText(event.getDate());
        event_time.setText(event.getTime());

        return view;
    }
}
