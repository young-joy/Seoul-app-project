package edu.skku.jonadan.hangangmongttang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.aviran.cookiebar2.CookieBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppInfoActivity extends AppCompatActivity {

    @BindView(R.id.appinfo_back_btn) ImageButton backBtn;
    @BindView(R.id.appinfo_icon) ImageView iconView;
    @BindView(R.id.appinfo_btn1) Button noticeBtn;
    @BindView(R.id.appinfo_btn2) Button feedbackBtn;
    @BindView(R.id.appinfo_btn3) Button osBtn;

    final private int FEEDBACK_REQ = 100;
    final private int FEEDBACK_YES = 200;
    final private int FEEDBACK_NO = 300;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FEEDBACK_REQ && resultCode == FEEDBACK_YES){
            CookieBar.build(AppInfoActivity.this)
                    .setTitle("피드백이 전송되었습니다")
                    .setTitleColor(R.color.colorBlack)
                    .setIcon(R.drawable.ic_thanks)
                    .setMessage("소중한 의견 감사드립니다")
                    .setMessageColor(R.color.colorBlack)
                    .setBackgroundColor(R.color.colorAccent)
                    .setCookiePosition(CookieBar.TOP)
                    .setDuration(2000)
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ButterKnife.bind(this);

        iconView.setClipToOutline(true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppInfoActivity.this, AppNoticeActivity.class);
                startActivity(intent);
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppInfoActivity.this, AppFeedbackActivity.class);
                startActivityForResult(intent, FEEDBACK_REQ);
            }
        });

        osBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(AppInfoActivity.this, );
                //startActivity(intent);
            }
        });
    }
}
