package edu.skku.jonadan.hangangmongttang;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class AddReviewFragment extends DialogFragment {
    private TextView nameTv;
    private TextView locationTv;

    private EditText reviewEdit;
    private EditText userEdit;
    private EditText passwordEdit;
    private RatingBar ratingEdit;

    private Button addBtn;
    private ImageButton backBtn;

    private int facilityId;
    private String facilityName;
    private String facilityLocation;

    private float rating;
    private String user;
    private String password;
    private String review;
    public AddReviewFragment() {
        super();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_slide;

        facilityId = InfoActivity.facilityId;
        facilityName = InfoActivity.facilityName;
        facilityLocation = InfoActivity.facilityLocation;

        nameTv.setText(facilityName);
        locationTv.setText(facilityLocation);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        nameTv = view.findViewById(R.id.facility_name);
        locationTv = view.findViewById(R.id.facility_location);

        reviewEdit = view.findViewById(R.id.edit_review);
        userEdit = view.findViewById(R.id.edit_user);
        passwordEdit = view.findViewById(R.id.edit_pw);
        ratingEdit = view.findViewById(R.id.ratingBar);

        addBtn = view.findViewById(R.id.btn_add);
        backBtn = view.findViewById(R.id.btn_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = ratingEdit.getRating();
                review = reviewEdit.getText().toString();
                user = userEdit.getText().toString();
                password = passwordEdit.getText().toString();
                String date = "2019.9.28";

                JSONObject insert_review = new SQLSender().sendSQL("INSERT INTO review (fid, user, password, date, rate, content) VALUES ("+new Integer(facilityId).toString()+", '"+user+"', '"+password+"', '"
                        +date+"', "+ new Float(rating).toString()+", '"+review+"');");
                try{
                    if(!insert_review.getBoolean("isError")){
                        //insert_review is success
                        Log.d("db_conn",insert_review.toString());
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                dismiss();
            }
        });

        return view;
    }
}
