package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import butterknife.BindAnim;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    @BindView(R.id.map_view) MapView mapView;

    @BindView(R.id.map_container) ConstraintLayout mapLayout;
    @BindView(R.id.map_fab_toilet) FloatingActionButton menuToiletBtn;
    @BindView(R.id.map_fab_shop) FloatingActionButton menuShopBtn;
    @BindView(R.id.map_fab_water) FloatingActionButton menuWaterBtn;
    @BindView(R.id.map_fab_restaurant) FloatingActionButton menuRestaurantBtn;
    @BindView(R.id.map_fab_entertain) FloatingActionButton menuEntertainBtn;
    @BindView(R.id.map_fab_athletic) FloatingActionButton menuAthleticBtn;
    @BindView(R.id.map_fab_menu) FloatingActionButton menuBtn;

    @BindAnim(R.anim.fab_open) Animation fabOpen;
    @BindAnim(R.anim.fab_close) Animation fabClose;

    @BindDimen(R.dimen.fab_margin) int fabMargin;

    private ArrayList<FloatingActionButton> fabList;
    private ConstraintSet constraintSet;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        initFabs();

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFabOpen) {
                    for (FloatingActionButton fab : fabList) {
                        fab.startAnimation(fabClose);
                        constraintSet.connect(
                                fab.getId(), ConstraintSet.BOTTOM,
                                menuBtn.getId(), ConstraintSet.BOTTOM);
                        constraintSet.setMargin(fab.getId(), ConstraintSet.BOTTOM, 0);
                        fab.setClickable(false);
                    }
                } else {
                    for (int i = fabList.size()-1; i >= 0; i--) {
                        fabList.get(i).startAnimation(fabOpen);
                        if (i == fabList.size()-1) {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    menuBtn.getId(), ConstraintSet.TOP);
                        } else {
                            constraintSet.connect(
                                    fabList.get(i).getId(), ConstraintSet.BOTTOM,
                                    fabList.get(i + 1).getId(), ConstraintSet.TOP);
                        }
                        constraintSet.setMargin(
                                fabList.get(i).getId(), ConstraintSet.BOTTOM, fabMargin);
                        fabList.get(i).setClickable(true);
                    }
                }
                AutoTransition transition = new AutoTransition();
                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(mapLayout, transition);
                constraintSet.applyTo(mapLayout);

                isFabOpen = !isFabOpen;
            }
        });
    }

    private void initFabs() {
        fabList = new ArrayList<>();
        constraintSet = new ConstraintSet();

        fabList.add(menuToiletBtn);
        fabList.add(menuShopBtn);
        fabList.add(menuWaterBtn);
        fabList.add(menuRestaurantBtn);
        fabList.add(menuEntertainBtn);
        fabList.add(menuAthleticBtn);

        constraintSet.clone(mapLayout);
        for (FloatingActionButton fab : fabList) {
            constraintSet.connect(
                    fab.getId(), ConstraintSet.BOTTOM, menuBtn.getId(), ConstraintSet.BOTTOM);
        }
    }
}
