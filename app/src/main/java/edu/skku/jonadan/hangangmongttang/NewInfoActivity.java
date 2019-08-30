package edu.skku.jonadan.hangangmongttang;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewInfoActivity extends AppCompatActivity {
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.tab_container)
    TabLayout tabLayout;
    @BindView(R.id.container)
    FrameLayout fragmentContainer;
    @BindView(R.id.text_state)
    TextView stateText;
    @BindView(R.id.info_back_btn)
    ImageButton backBtn;

    InfoFragment infoFragment;
    ReviewFragment reviewFragment;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private ConstraintSet reviseConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_new);
        ButterKnife.bind(this);

        reviseConstraintSet.clone(constraintLayout);
        resetConstraintSet.clone(constraintLayout);

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        infoFragment.removeMapView();
    }

    private void changView(int index) {
        AutoTransition transition = new AutoTransition();

        switch (index) {
            case 0 :
                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(constraintLayout, transition);
                resetConstraintSet.applyTo(constraintLayout);

                fragmentManager.beginTransaction().hide(activeFragment).show(infoFragment).commit();
                activeFragment = infoFragment;

                stateText.setText(getString(R.string.info_text));
                break ;
            case 1 :
                reviseConstraintSet.setMargin(R.id.container, ConstraintSet.TOP, 10);
                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(constraintLayout, transition);
                reviseConstraintSet.applyTo(constraintLayout);

                fragmentManager.beginTransaction().hide(activeFragment).show(reviewFragment).commit();
                activeFragment = reviewFragment;

                stateText.setText(getString(R.string.review_text));
                break ;
        }
    }
}
