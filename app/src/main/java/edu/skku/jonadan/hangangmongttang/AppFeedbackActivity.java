package edu.skku.jonadan.hangangmongttang;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppFeedbackActivity extends AppCompatActivity {

    @BindView(R.id.feedback_back_btn) ImageButton backBtn;
    @BindView(R.id.feedback_check_btn) ImageButton checkBtn;
    @BindView(R.id.feedback_text) EditText feedbackText;

    private String feedback;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_feedback);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = feedbackText.getText().toString();
                date = getCurDate();

                if(feedback.length() == 0) {
                    Toast.makeText(AppFeedbackActivity.this, R.string.empty_review,Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject insert_feedback = new SQLSender().sendSQL("INSERT INTO feedback (content, date) VALUES ('"+feedback+"', '"+date+"');");
                    try{
                        if(!insert_feedback.getBoolean("isError")){
                            //insert_review is success
                            Log.d("db_conn",insert_feedback.toString());
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String getCurDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(date);
    }
}
