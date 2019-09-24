package edu.skku.jonadan.hangangmongttang;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ParkInfoDialog extends DialogFragment {
    ImageButton closeBtn;
    Button mapBtn;

    ImageView iconIv;
    ImageView parkIv;
    TextView nameTv;
    TextView locationTv;
    TextView numberTv;
    TextView attractionTv;
    TextView facilityTv;

    public ParkInfoDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_park_info, container, false);

        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closeBtn = view.findViewById(R.id.btn_close);
        mapBtn = view.findViewById(R.id.btn_map);

        iconIv = view.findViewById(R.id.park_icon);
        parkIv = view.findViewById(R.id.park_img);
        nameTv = view.findViewById(R.id.park_name);
        locationTv = view.findViewById(R.id.location);
        numberTv = view.findViewById(R.id.number);
        attractionTv = view.findViewById(R.id.attraction);
        facilityTv = view.findViewById(R.id.facility);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
