package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class MapParkListAdapter extends RecyclerView.Adapter {

    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private Park curPark;
    private ArrayList<Park> parkList = new ArrayList<>();
    private Boolean isOpen = false;
    private ParkClickListener parkClickListener;

    public interface ParkClickListener {
        void movePark(Park park);
        void focusParkList();
    }

    public MapParkListAdapter(Park curPark, ParkClickListener parkClickListener) {
        this.curPark = curPark;
        this.parkList.add(curPark);
        this.parkClickListener = parkClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (type) {
            case HEADER:
                view = inflater.inflate(R.layout.item_park_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = inflater.inflate(R.layout.item_park_child, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Park park = parkList.get(position);
        if (position == 0) {
            final ListHeaderViewHolder headerHolder = (ListHeaderViewHolder) holder;
            headerHolder.parkText.setText(park.getName());
            if (isOpen) {
                headerHolder.openParkListBtn.setImageResource(R.drawable.ic_close_list);
            } else {
                headerHolder.openParkListBtn.setImageResource(R.drawable.ic_open_list);
            }

            headerHolder.openParkListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Close list
                    if (isOpen) {
                        isOpen = false;
                        parkList.clear();
                        parkList.add(curPark);
                        notifyDataSetChanged();
                    // Open list
                    } else {
                        isOpen = true;
                        for (Park p: ConstantPark.PARK_LIST) {
                            parkList.add(p);
                        }
                        parkClickListener.focusParkList();
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            final ListChildViewHolder childHolder = (ListChildViewHolder) holder;
            childHolder.parkText.setText(park.getName());
            if (park.getName().equals(curPark.getName())) {
                childHolder.parkContainer.setBackgroundResource(R.color.colorPrimary);
                childHolder.parkText.setTextColor(Color.WHITE);
            } else {
                childHolder.parkContainer.setBackgroundResource(R.color.colorBackgroundWhite);
                childHolder.parkText.setTextColor(Color.DKGRAY);
            }
            childHolder.parkContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isOpen = false;
                    curPark = park;
                    parkList.clear();
                    parkList.add(curPark);
                    notifyDataSetChanged();
                    parkClickListener.movePark(curPark);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return CHILD;
        }
    }


    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView parkText;
        public ImageButton openParkListBtn;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            parkText = itemView.findViewById(R.id.item_cur_park);
            openParkListBtn = itemView.findViewById(R.id.item_park_open_btn);
        }
    }

    public static class ListChildViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout parkContainer;
        public TextView parkText;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            parkContainer = itemView.findViewById(R.id.item_park_container);
            parkText = itemView.findViewById(R.id.item_park_name);
        }
    }
}
