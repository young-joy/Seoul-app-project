package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.LicensesDialogFragment;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;


public class AppInfoActivity extends AppCompatActivity {
    @BindView(R.id.appinfo_back_btn) ImageButton backBtn;
    @BindView(R.id.appinfo_icon) ImageView iconView;
    //@BindView(R.id.appinfo_btn1) Button listBtn1;
    //@BindView(R.id.appinfo_btn2) Button listBtn2;
    //@BindView(R.id.appinfo_btn3) Button listBtn3;

    private ArrayList<AppInfoItem> mArrayList;
    private AppInfoAdapter mAdapter;

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

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.appinfo_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new AppInfoAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mArrayList.add(new AppInfoItem("공지사항"));
        mArrayList.add(new AppInfoItem("피드백 보내기"));
        mArrayList.add(new AppInfoItem("오픈소스 라이센스"));

        /*listBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new LicensesDialog.Builder(AppInfoActivity.this)
                        .setNotices(R.raw.notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
            }
        });*/
    }
}
