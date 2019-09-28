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

import java.util.ArrayList;

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

    private ArrayList<Integer> imageList;

    public static int facilityId;
    public static String facilityName = "";
    public static String facilityLocation = "";
    public static ArrayList<ReviewListItem> reviewList = new ArrayList();

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
        imageList = new ArrayList<>();
        imageList.add(R.drawable.turtle);
        imageList.add(R.drawable.turtle);
        imageList.add(R.drawable.turtle);

        InfoPagerAdapter infoPagerAdapter = new InfoPagerAdapter(InfoActivity.this, imageList);
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
        getReview();

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

    private void getReview(){
        int review_num;

        int rid;
        String user;
        String password;
        String content;
        String date;
        float rate;

        JSONObject get_review = new SQLSender().sendSQL("SELECT * from review where fid="+new Integer(facilityId).toString()+";");
        try{
            if(!get_review.getBoolean("isError")){
                Log.d("db_conn",get_review.toString());

                JSONArray reviews = get_review.getJSONArray("result");
                JSONObject review;
                ReviewListItem reviewItem;
                review_num = reviews.length();

                for(int i=0;i<review_num;i++){
                    review = reviews.getJSONObject(i);
                    rid = review.getInt("rid");
                    user = review.getString("user");
                    password = review.getString("password");
                    date = review.getString("date");
                    rate = review.getInt("rate");
                    content = review.getString("content");

                    reviewItem = new ReviewListItem(rid, user, password, date, rate, content);
                    reviewList.add(reviewItem);
                }
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
