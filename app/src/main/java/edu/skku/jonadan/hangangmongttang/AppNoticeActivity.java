package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        getNotice();

        noticeListAdapter = new AppNoticeListAdapter(noticeList);
        noticeListView.setAdapter(noticeListAdapter);
        noticeListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getNotice() {
        noticeList.clear();
        JSONObject sqlNotice = new SQLSender().
                sendSQL("SELECT * FROM notice");
        try {
            if (!sqlNotice.getBoolean("isError")) {
                JSONArray noticeResult = sqlNotice.getJSONArray("result");
                for (int i = 0; i < noticeResult.length(); i++) {
                    JSONObject noticeJson = noticeResult.getJSONObject(i);
                    noticeList.add(new Notice(noticeJson.getInt("nid"),
                            noticeJson.getString("title"),
                            noticeJson.getString("date"),
                            noticeJson.getString("content")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
