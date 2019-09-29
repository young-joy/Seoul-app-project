package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends Fragment {
    ConstraintLayout mapLayout;
    ScrollView scrollView;

    TextView nameTv;
    TextView locationTv;
    TextView infoTv;
    TextView infoContentTv;

    private int facilityId;
    private String facilityName;
    private String facilityLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_info, container, false);

        nameTv = rootView.findViewById(R.id.facility_name);
        locationTv = rootView.findViewById(R.id.facility_location);
        infoTv = rootView.findViewById(R.id.facility_info);
        infoContentTv = rootView.findViewById(R.id.facility_info_content);

        mapLayout = rootView.findViewById(R.id.map_container);
        scrollView = rootView.findViewById(R.id.scroll_view);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.setNestedScrollingEnabled(false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        facilityId = InfoActivity.facilityId;
        facilityName = InfoActivity.facilityName;
        facilityLocation = InfoActivity.facilityLocation;

        if(!facilityName.equals("") || !facilityLocation.equals("")){
            nameTv.setText(facilityName);
            locationTv.setText(facilityLocation);
        }else{
            infoTv.setText("시설 정보를 불러올 수 없습니다");
        }

        if(facilityId/100000<11){
            infoTv.setText(R.string.entertain_info);
            infoContentTv.setText(R.string.entertain_info_content);
        }else{
            infoTv.setText(R.string.athletic_info);
            infoContentTv.setText(R.string.athletic_info_content);
        }
    }
}
