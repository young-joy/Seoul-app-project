package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomBalloonAdapter implements CalloutBalloonAdapter {

    @BindView(R.id.balloon_title)
    TextView title;

    private final View mBalloon;

    public CustomBalloonAdapter(Context context) {
        mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon, null);
        ButterKnife.bind(this, mBalloon);
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        mBalloon.setVisibility(View.VISIBLE);
        if (mapPOIItem.getItemName().equals("")) {
            mBalloon.setVisibility(View.INVISIBLE);
        }
        title.setText(mapPOIItem.getItemName());
        return mBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        mBalloon.setVisibility(View.VISIBLE);
        if (mapPOIItem.getItemName().equals("")) {
            mBalloon.setVisibility(View.INVISIBLE);
        }
        title.setText(mapPOIItem.getItemName());
        return mBalloon;
    }
}
