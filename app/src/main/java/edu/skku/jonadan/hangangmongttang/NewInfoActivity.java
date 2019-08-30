package edu.skku.jonadan.hangangmongttang;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewInfoActivity extends AppCompatActivity {
    @BindView(R.id.tab_container)
    TabLayout tabLayout;

    InfoFragment infoFragment;
    ReviewFragment reviewFragment;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_new);
        ButterKnife.bind(this);

        // initialize fragments
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, reviewFragment, "2").hide(reviewFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container,infoFragment, "1").commit();
        activeFragment = infoFragment;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        infoFragment.removeMapView();
    }

    private void changView(int index) {

        switch (index) {
            case 0 :
                fragmentManager.beginTransaction().hide(activeFragment).show(infoFragment).commit();
                activeFragment = infoFragment;
                break ;
            case 1 :
                fragmentManager.beginTransaction().hide(activeFragment).show(reviewFragment).commit();
                activeFragment = reviewFragment;
                break ;
        }
    }
}
