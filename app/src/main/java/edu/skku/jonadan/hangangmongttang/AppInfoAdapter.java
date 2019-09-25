package edu.skku.jonadan.hangangmongttang;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.CustomViewHolder> {

    private ArrayList<AppInfoItem> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView img;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.appinfo_list_title);
            this.img = (ImageView) view.findViewById(R.id.appinfo_imageView);
        }
    }

    public AppInfoAdapter(ArrayList<AppInfoItem> mList) {
        this.mList = mList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_info, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewholder.img.setImageResource(R.drawable.ic_arrow);
        viewholder.title.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
