package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {
    ConstraintLayout mapLayout;
    ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_info, container, false);

        mapLayout = rootView.findViewById(R.id.map_container);
        scrollView = rootView.findViewById(R.id.scroll_view);

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        scrollView.setNestedScrollingEnabled(false); 
        return rootView;
    }

    public void removeMapView(){
        mapLayout.removeAllViews();
    }
}
