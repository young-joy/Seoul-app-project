package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class InfoPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Integer> imageList;

    public InfoPagerAdapter(Context context, ArrayList<Integer> list) {
        super();
        this.context = context;
        this.imageList = list;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        if(context != null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_image, container, false);

            ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
            imageView.setImageResource(imageList.get(position));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
