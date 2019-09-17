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
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddReviewFragment extends DialogFragment {
    private EditText reviewEdit;
    private EditText userEdit;
    private EditText passwordEdit;
    private RatingBar ratingEdit;
    private Button addBtn;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        reviewEdit = view.findViewById(R.id.edit_review);
        userEdit = view.findViewById(R.id.edit_user);
        passwordEdit = view.findViewById(R.id.edit_pw);
        ratingEdit = view.findViewById(R.id.ratingBar);
        addBtn = view.findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = ratingEdit.getRating();
                review = reviewEdit.getText().toString();
                user = userEdit.getText().toString();
                password = passwordEdit.getText().toString();

                dismiss();
            }
        });

        return view;
    }
}
