package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    ListView reviewListView;

    private ArrayList<ReviewListItem> reviewList = new ArrayList<>();
    private ReviewListAdapter reviewListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        reviewListView = rootView.findViewById(R.id.review_list);

        for(int i=0;i<3;i++){
            ReviewListItem item = new ReviewListItem();
            reviewList.add(item);
        }
        reviewListAdapter = new ReviewListAdapter(reviewList);
        reviewListView.setAdapter(reviewListAdapter);

        return rootView;
    }
}
