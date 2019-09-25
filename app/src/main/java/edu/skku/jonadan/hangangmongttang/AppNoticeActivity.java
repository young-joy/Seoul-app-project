package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppNoticeActivity extends AppCompatActivity {

    @BindView(R.id.app_notice_back_btn)
    ImageButton noticeBackBtn;
    @BindView(R.id.app_notice_list)
    RecyclerView noticeListView;

    private AppNoticeListAdapter noticeListAdapter;
    private ArrayList<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notice);
        ButterKnife.bind(this);

        initNotice();

        noticeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initNotice() {
        noticeList = new ArrayList<>();
        noticeList.add(new Notice(0, "1", "2019-09-25", "1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n1234\n"));
        noticeList.add(new Notice(1, "2", "2019-09-25", "1234"));
        noticeList.add(new Notice(2, "3", "2019-09-25", "1234"));
        noticeList.add(new Notice(3, "4", "2019-09-25", "1234"));
        noticeList.add(new Notice(4, "5", "2019-09-25", "1234"));

        noticeListAdapter = new AppNoticeListAdapter(noticeList);
        noticeListView.setAdapter(noticeListAdapter);
        noticeListView.setLayoutManager(new LinearLayoutManager(this));
    }
}
