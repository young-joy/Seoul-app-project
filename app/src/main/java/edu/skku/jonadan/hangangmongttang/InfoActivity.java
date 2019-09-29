package edu.skku.jonadan.hangangmongttang;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
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
    @BindView(R.id.image_container)
    ViewPager imageContainer;
    @BindView(R.id.tab_layout)
    TabLayout imageTab;


    InfoFragment infoFragment;
    ReviewFragment reviewFragment;

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private ConstraintSet reviseConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();

    public InfoPagerAdapter infoPagerAdapter;
    private ArrayList<String> entertainImageList;
    private ArrayList<String> athleticImageList;

    public static int facilityId;
    public static String facilityName = "";
    public static String facilityLocation = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        facilityId = intent.getExtras().getInt("facility_id");

        // set viewPager
        if(facilityId/100000<11){
            entertainImageList = new ArrayList<>();
            entertainImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_03_4.jpg");
            entertainImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_03_2.jpg");
            entertainImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_03_3.jpg");
            infoPagerAdapter = new InfoPagerAdapter(InfoActivity.this, entertainImageList);
        }else{
            athleticImageList = new ArrayList<>();
            athleticImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_03_1.jpg");
            athleticImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_01_10.jpg");
            athleticImageList.add("http://hangang.seoul.go.kr/files/2013/12/enjoy01_06_01_9.jpg");
            infoPagerAdapter = new InfoPagerAdapter(InfoActivity.this, athleticImageList);
        }
        imageContainer.setAdapter(infoPagerAdapter);
        imageTab.setupWithViewPager(imageContainer, true);

        reviseConstraintSet.clone(constraintLayout);
        resetConstraintSet.clone(constraintLayout);

        // initialize fragments
        infoFragment = new InfoFragment();
        reviewFragment = new ReviewFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, reviewFragment, "2").hide(reviewFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container,infoFragment, "1").commit();
        activeFragment = infoFragment;

        getFacilityInfo();

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

    private void getFacilityInfo(){
        JSONObject get_facility_info = new SQLSender().sendSQL("SELECT * from facility where fid="+new Integer(facilityId).toString()+";");
        try{
            if(!get_facility_info.getBoolean("isError")){
                JSONObject facility_info = get_facility_info.getJSONArray("result").getJSONObject(0);

                facilityName = facility_info.getString("name");
                facilityLocation = facility_info.getString("location");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void changView(int index) {
        AutoTransition transition = new AutoTransition();

        switch (index) {
            case 0 :
                imageTab.setupWithViewPager(imageContainer, true);

                transition.setDuration(300);
                transition.setInterpolator(new AccelerateDecelerateInterpolator());
                TransitionManager.beginDelayedTransition(constraintLayout, transition);
                resetConstraintSet.applyTo(constraintLayout);

                fragmentManager.beginTransaction().hide(activeFragment).show(infoFragment).commit();
                activeFragment = infoFragment;

                stateText.setText(getString(R.string.info_text));
                break ;
            case 1 :
                imageTab.removeAllTabs();

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
