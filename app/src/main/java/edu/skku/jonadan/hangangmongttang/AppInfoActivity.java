package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.psdev.licensesdialog.LicensesDialog;

public class AppInfoActivity extends AppCompatActivity {

    @BindView(R.id.appinfo_back_btn) ImageButton backBtn;
    @BindView(R.id.appinfo_icon) ImageView iconView;
    @BindView(R.id.appinfo_btn1) Button noticeBtn;
    @BindView(R.id.appinfo_btn2) Button feedbackBtn;
    @BindView(R.id.appinfo_btn3) Button osBtn;

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
                startActivity(intent);
            }
        });

        osBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LicensesDialog.Builder(AppInfoActivity.this)
                        .setNotices(R.raw.license)
                        .setIncludeOwnLicense(true)
                        .setThemeResourceId(R.style.dialog_license)
                        .build()
                        .show();
            }
        });
    }
}
