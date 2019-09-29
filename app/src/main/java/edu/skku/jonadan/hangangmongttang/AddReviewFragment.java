package edu.skku.jonadan.hangangmongttang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReviewFragment extends DialogFragment {

    public static final int REVIEW_CREATE = 0;
    public static final int REVIEW_UPDATE = 1;

    private TextView nameTv;
    private TextView locationTv;
    private TextView reviewLenTv;

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
    private String date;

    private int requestCode;

    private View emptyDialogView;

    public static boolean endFragment = false;

    private int reviewLength = 0;
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

        requestCode = getArguments().getInt("request_code");

        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        nameTv = view.findViewById(R.id.facility_name);
        locationTv = view.findViewById(R.id.facility_location);
        reviewLenTv = view.findViewById(R.id.review_len);

        reviewEdit = view.findViewById(R.id.edit_review);
        userEdit = view.findViewById(R.id.edit_user);
        passwordEdit = view.findViewById(R.id.edit_pw);
        ratingEdit = view.findViewById(R.id.ratingBar);

        addBtn = view.findViewById(R.id.btn_add);
        backBtn = view.findViewById(R.id.btn_back);

        reviewEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                reviewLength = reviewEdit.length();
                reviewLenTv.setText(new Integer(reviewLength).toString());
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewEdit.getText().length()!=0 || userEdit.getText().length()!=0
                || passwordEdit.getText().length()!=0 || ratingEdit.getRating()!=1){
                    ReviewCancelDialog cancelDialog = new ReviewCancelDialog();
                    cancelDialog.show(getFragmentManager(), "PARK_INFO_DIALOG");
                    getFragmentManager().executePendingTransactions();
                    cancelDialog.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(endFragment)
                                dismiss();
                        }
                    });
                }else{
                    dismiss();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (requestCode) {
                    case REVIEW_CREATE:
                        rating = ratingEdit.getRating();
                        review = reviewEdit.getText().toString();
                        user = userEdit.getText().toString();
                        password = passwordEdit.getText().toString();
                        date = getCurDate();

                        if(review.length()==0 || user.length()==0 || password.length()==0){
                            Toast.makeText(getContext(), R.string.empty_review,Toast.LENGTH_SHORT).show();
                        }else{
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
                            ReviewFragment.getReview();
                            dismiss();
                        }
                        break;

                    case REVIEW_UPDATE:
                        int rid = getArguments().getInt("rid");
                        rating = ratingEdit.getRating();
                        review = reviewEdit.getText().toString();
                        user = userEdit.getText().toString();
                        password = passwordEdit.getText().toString();
                        date = getCurDate();

                        if(review.length()==0 || user.length()==0 || password.length()==0){
                            Toast.makeText(getContext(), R.string.empty_review,Toast.LENGTH_SHORT).show();
                        }else{
                            JSONObject updateReview = new SQLSender().sendSQL(
                                    "UPDATE review SET " +
                                            "user = '" + user + "', " +
                                            "password = '" + password + "', " +
                                            "date = '" + date + "', " +
                                            "rate = " + rating + ", " +
                                            "content = '" + review + "' " +
                                            "WHERE rid = " + rid
                            );
                            try{
                                if(!updateReview.getBoolean("isError")){
                                    Log.d("db_conn", updateReview.toString());
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            ReviewFragment.getReview();
                            dismiss();
                        }
                        break;
                }
            }
        });

        switch (requestCode) {
            case REVIEW_CREATE:
                break;
            case REVIEW_UPDATE:
                int rid = getArguments().getInt("rid");
                getReview(rid);
                break;
        }

        return view;
    }

    private String getCurDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(date);
    }

    private void getReview(int rid) {
        JSONObject sqlReview = new SQLSender().sendSQL(
                "SELECT * FROM review WHERE rid = " + rid
        );
        try{
            if(!sqlReview.getBoolean("isError")){
                JSONArray reviewResult = sqlReview.getJSONArray("result");
                for (int i = 0; i < reviewResult.length(); i++) {
                    JSONObject reviewJson = reviewResult.getJSONObject(i);
                    ratingEdit.setRating(reviewJson.getInt("rate"));
                    reviewEdit.setText(reviewJson.getString("content"));
                    userEdit.setText(reviewJson.getString("user"));
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
