package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

public class CustomBalloonAdapter implements CalloutBalloonAdapter {

    private View mBalloon;
    private Context context;

    public CustomBalloonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getCalloutBalloon(MapPOIItem mapPOIItem) {
        Log.d("Balloon", "" + mapPOIItem.getTag());
        int type = mapPOIItem.getTag() / SeoulApiProvider.SERVICE_PAD;
        Log.d("Balloon", "" + type);
        switch (SeoulApiProvider.SERVICE_CODE.values()[type]) {
            case USER:
                mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon, null);
                mBalloon.setVisibility(View.VISIBLE);

                LinearLayout bgUser = mBalloon.findViewById(R.id.balloon_bg);
                bgUser.setBackgroundResource(R.drawable.ic_balloon_user);

                TextView titleUser = mBalloon.findViewById(R.id.balloon_title);
                titleUser.setText(mapPOIItem.getItemName());
                break;
            case PARK:
                if (mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD % 1000 != 0) {
                    mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon_park, null);
                    mBalloon.setVisibility(View.VISIBLE);

                    TextView title = mBalloon.findViewById(R.id.balloon_park_title);
                    title.setText(mapPOIItem.getItemName());

                    TextView state = mBalloon.findViewById(R.id.balloon_park_state);
                    int parkId = mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD / 1000;
                    int parkingLotId = mapPOIItem.getTag() % SeoulApiProvider.SERVICE_PAD % 1000 - 1;
                    String capacity = new Integer(ConstantPark.PARK_LIST.get(parkId)
                            .getParkingLots().get(parkingLotId)
                            .getCapacity()).toString();
                    String curState = "0";
                    state.setText(curState + " / " + capacity);
                } else {
                    mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon, null);
                    mBalloon.setVisibility(View.VISIBLE);

                    LinearLayout bg = mBalloon.findViewById(R.id.balloon_bg);
                    bg.setBackgroundResource(R.drawable.ic_balloon_park);

                    TextView title = mBalloon.findViewById(R.id.balloon_title);
                    title.setText(mapPOIItem.getItemName());
                }
                break;
            default:
                mBalloon = LayoutInflater.from(context).inflate(R.layout.custom_balloon, null);
                mBalloon.setVisibility(View.VISIBLE);

                TextView title = mBalloon.findViewById(R.id.balloon_title);
                if (mapPOIItem.getItemName().equals("")) {
                    mBalloon.setVisibility(View.INVISIBLE);
                } else {
                    title.setText(mapPOIItem.getItemName());
                    LinearLayout bg = mBalloon.findViewById(R.id.balloon_bg);
                    bg.setBackgroundResource(R.drawable.ic_balloon);
                }
        }

        return mBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
        return null;
    }
}
