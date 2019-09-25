package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppInfoActivity extends AppCompatActivity {
    @BindView(R.id.appinfo_back_btn) ImageButton backBtn;
    @BindView(R.id.appinfo_icon) ImageView iconView;

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
    }
}
