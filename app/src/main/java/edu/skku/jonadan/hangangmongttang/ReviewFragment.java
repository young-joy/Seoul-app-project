package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    ListView reviewListView;
    Button reviewBtn;

    private ArrayList<ReviewListItem> reviewList = new ArrayList<>();
    private ReviewListAdapter reviewListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        reviewBtn = rootView.findViewById(R.id.btn_review);
        reviewListView = rootView.findViewById(R.id.review_list);

        for(int i=0;i<3;i++){
            ReviewListItem item = new ReviewListItem();
            reviewList.add(item);
        }
        reviewListAdapter = new ReviewListAdapter(reviewList);
        reviewListView.setAdapter(reviewListAdapter);

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReviewFragment addReviewFragment = new AddReviewFragment();
                addReviewFragment.setStyle( DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light );
                addReviewFragment.show(getActivity().getSupportFragmentManager(),"tag");
            }
        });
        return rootView;
    }
}
