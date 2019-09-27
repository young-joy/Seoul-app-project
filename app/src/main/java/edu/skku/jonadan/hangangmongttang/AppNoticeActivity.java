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
        noticeList.add(new Notice(0, "[NAVER] 대학(원)생 기술 창업 공모전",
                "2019-09-25",
                "■신청링크\n" +
                        "\n" +
                        "http://m.site.naver.com/0tyc0 \n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "■참가대상\n" +
                        "\n" +
                        "인공지능, 헬스케어 VR/AR, 비디오, 오디오, 자율주행, 로보틱스, 보안 등 전체 기술분야 등\n" +
                        "\n" +
                        "대학원(생) 기술 창업팀이라면 누구나 신청할 수 있습니다.\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "■지원혜택\n" +
                        "\n" +
                        " -기술 개발 자금\n" +
                        "\n" +
                        " -GPU등 클라우드 인프라\n" +
                        "\n" +
                        " -강남 최고의 업무공간\n" +
                        "\n" +
                        " -네이버 엔지니어 및 기술 창업가 네트워크\n" +
                        "\n" +
                        " -우수팀은 네이버 투자 및 지원 연계"));
        noticeList.add(new Notice(1, "2019 LG이노텍 학사산학장학생 INNO SCHOLARSHIP 10기 모집(~10/15)",
                "2019-09-25",
                "2019 LG이노텍 학사산학장학생 INNO SCHOLARSHIP 10기 모집\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "■ 모집부문 : Software Track, 해외마케팅 Track\n" +
                        "\n" +
                        "■ 지원기간 : 10/1(화) ~ 10/15(화) 17시까지\n" +
                        "\n" +
                        "■ 지원방법 : 온라인 지원을 통한 접수(careers.lg.com)" +
                        "■ 지원자격\n" +
                        "\n" +
                        "- 21년 2월 이공계열 학부 졸업예정자(現 3학년 학부 재학생)\n" +
                        "\n" +
                        "- 해외여행에 결격 사유가 없는 자로 남자의 경우 병역필 또는 면제\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "■ 채용전형\n" +
                        "\n" +
                        "- 서류전형  →  인/적성 검사(10/27)  →  면접전형(1차,2차) →  건강검진 →  최종합격\n" +
                        "\n" +
                        "   \n" +
                        "\n" +
                        "■ 문의하기\n" +
                        "\n" +
                        "- LG커리어스 →  Apply LG →  채용문의 →  1:1문의"));

        noticeListAdapter = new AppNoticeListAdapter(noticeList);
        noticeListView.setAdapter(noticeListAdapter);
        noticeListView.setLayoutManager(new LinearLayoutManager(this));
    }
}
