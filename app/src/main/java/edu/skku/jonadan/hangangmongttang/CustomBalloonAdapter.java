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

    private View mBalloon;
    private Context context;

    public CustomBalloonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        Log.d("Object ID", "" + mapPOIItem.getTag());
        if (mapPOIItem.getTag() > SeoulApiProvider.SERVICE_CODE.PARK.ordinal() * SeoulApiProvider.SERVICE_PAD
                && mapPOIItem.getTag() < (SeoulApiProvider.SERVICE_CODE.PARK.ordinal()+1) * SeoulApiProvider.SERVICE_PAD
                && mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD % 1000 != 0) {
            mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon_park, null);
            TextView title = mBalloon.findViewById(R.id.balloon_park_title);
            TextView state = mBalloon.findViewById(R.id.balloon_park_state);

            mBalloon.setVisibility(View.VISIBLE);
            title.setText(mapPOIItem.getItemName());

            int parkId = mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD / 1000;
            int parkingLotId = mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD % 1000 - 1;
            String capacity = new Integer(ConstantPark.PARK_LIST.get(parkId)
                            .getParkingLots().get(parkingLotId)
                            .getCapacity()).toString();
            String curState = "0";
            state.setText(curState + " / " + capacity);
        } else {
            mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon, null);
            TextView title = mBalloon.findViewById(R.id.balloon_title);

            mBalloon.setVisibility(View.VISIBLE);
            if (mapPOIItem.getItemName().equals("")) {
                mBalloon.setVisibility(View.INVISIBLE);
            } else {
                title.setText(mapPOIItem.getItemName());
            }
        }

        return mBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}
