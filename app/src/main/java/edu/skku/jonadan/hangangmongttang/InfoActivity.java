package edu.skku.jonadan.hangangmongttang;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    @BindView(R.id.info_back_btn)
    ImageButton backBtn;

    @BindView(R.id.tab_container)
    TabLayout tabLayout;

    @BindView(R.id.text1)
    TextView textView1;
    @BindView(R.id.text2)
    TextView textView2;

    //park info
    private String parkName;
    private String parkImg_src;
    private String parkNumber;
    private String parkLocation;
    private String parkAttraction;
    private String parkFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        initParkInfo();

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

    private void changView(int index) {

        switch (index) {
            case 0 :
                textView1.setVisibility(View.VISIBLE) ;
                textView2.setVisibility(View.INVISIBLE) ;
                break ;
            case 1 :
                textView1.setVisibility(View.INVISIBLE) ;
                textView2.setVisibility(View.VISIBLE) ;
                break ;

        }
    }

    private void initParkInfo(){
        //store park info
        parkName = SelectedParkInfo.getName();
        parkImg_src = SelectedParkInfo.getImg_src();
        parkLocation = SelectedParkInfo.getLocation();
        parkNumber = SelectedParkInfo.getNumber();
        parkAttraction = SelectedParkInfo.getAttraction();
        parkFacility = SelectedParkInfo.getFacility();
    }
}
