package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_park) ImageButton parkBtn;
    @BindView(R.id.button) Button button;

    final String api_key = "d0c498afb7199ff9bf703f95c14e007a";
    final int cnt = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /// TODO: 2019-07-05 날씨 가져올 좌표값 설정하기 + 앱 켤떄마다 새로고침 
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather(35,139);
            }
        });

        // TODO: 2019-07-05 이미지 바꾸기
        parkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getWeather(double lat, double lng){
        String url = "http://api.openweathermap.org/data/2.5/forecast?cnt="+new Integer(cnt).toString()+"&q=London&appid="+api_key;

        ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
        receiveUseTask.execute(url);
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... datas) {
            int code =0 ;
            try{
                URL url = new URL(datas[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d("get_weather",datas[0]);

                code = connection.getResponseCode();
                if(code == HttpURLConnection.HTTP_OK){
                    Log.d("get_weather","code : 200");
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader is_reader = new InputStreamReader(inputStream);
                    BufferedReader bf_reader = new BufferedReader(is_reader);

                    String data;
                    while((data=bf_reader.readLine())!=null){
                        JSONObject object = new JSONObject(data);
                        return object;
                    }
                }else{
                    Log.d("get_weather","failed_no response code");
                    return null;
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("get_weather","failed - response code: "+new Integer(code).toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            String[] temp = new String[5];
            String[] weather = new String[5];
            String[] time = new String[5];

            if(result!=null){
                Log.d("get_weather",result.toString());
                for(int i=0;i<cnt;i++){
                    try {
                        temp[i] = result.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp");
                        weather[i] = result.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                        time[i] = result.getJSONArray("list").getJSONObject(i).getString("dt_txt");

                        Log.d("get_weather","temp : "+temp[i]+" / weather : "+weather[i]+" / time : "+time[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
