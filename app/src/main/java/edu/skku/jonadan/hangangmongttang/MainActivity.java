package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
/// TODO: 2019-08-19 modify layout(drawer:linear->constraint), add event info  
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_park)
    ImageButton parkBtn;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.app_info_btn)
    ImageButton infoBtn;

    @BindView(R.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;

    @BindView(R.id.bottom_drawer)
    SlidingDrawer bottomDrawer;

    //weather info
    @BindView(R.id.temp1)
    TextView temp1;
    @BindView(R.id.temp2)
    TextView temp2;
    @BindView(R.id.temp3)
    TextView temp3;
    @BindView(R.id.temp4)
    TextView temp4;
    @BindView(R.id.temp5)
    TextView temp5;

    @BindView(R.id.weather1)
    ImageView weather1;
    @BindView(R.id.weather2)
    ImageView weather2;
    @BindView(R.id.weather3)
    ImageView weather3;
    @BindView(R.id.weather4)
    ImageView weather4;
    @BindView(R.id.weather5)
    ImageView weather5;

    @BindView(R.id.dust_g1)
    TextView dust_g1;
    @BindView(R.id.dust_g2)
    TextView dust_g2;

    @BindView(R.id.time1)
    TextView time1;
    @BindView(R.id.time2)
    TextView time2;
    @BindView(R.id.time3)
    TextView time3;
    @BindView(R.id.time4)
    TextView time4;
    @BindView(R.id.time5)
    TextView time5;

    @BindView(R.id.weather_container)
    LinearLayout weather_container;
    @BindView(R.id.text_error)
    TextView text_error;

    private HashMap<String, String> weatherInfo = new HashMap<>();
    private ArrayList<HashMap<String, String>> eventList = new ArrayList<>();

    private boolean park_layout_opened = false;
    private boolean drawer_opened = false;
    final String api_key = "d0c498afb7199ff9bf703f95c14e007a";
    final int cnt = 5;

    private BottomSheetDialog park_info_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ParkInfoCrawler.setMainContext(MainActivity.this);
        ParkInfoCrawler.start();

        park_info_dialog = new BottomSheetDialog(MainActivity.this);
        park_info_dialog.setContentView(R.layout.dialog_park_info);

        // TODO: 2019-07-05 이미지 바꾸기
        parkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                park_info_dialog.show();
                
                if(drawer_opened){
                    bottomDrawer.close();
                    drawer_opened = false;
                }

                ImageButton closeBtn = park_info_dialog.findViewById(R.id.close_btn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        park_info_dialog.dismiss();
                    }
                });
                park_layout_opened = true;
            }
        });

        SlidingDrawer.OnDrawerOpenListener onDrawerOpenListener = new OnSlidingDrawerOpenListener();
        SlidingDrawer.OnDrawerCloseListener onDrawerCloseListener = new OnSlidingDrawerCloseListener();

        bottomDrawer.setOnDrawerOpenListener(onDrawerOpenListener);
        bottomDrawer.setOnDrawerCloseListener(onDrawerCloseListener);
    }

    public void setData(){
        //use parsed data
        weatherInfo = ParkInfoCrawler.getWeatherInfo();
        eventList = ParkInfoCrawler.getEventList();

        dust_g1.setText(weatherInfo.get("DUST-G1"));
        dust_g2.setText(weatherInfo.get("DUST-G2"));
    }

    private void getWeather(){
        String url = "http://api.openweathermap.org/data/2.5/forecast?cnt="+new Integer(cnt).toString()+"&q=Seoul&appid="+api_key;

        ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
        receiveUseTask.execute(url);
    }

    private void setWeatherImage(String weather, ImageView imageView, String time){
        int weather_type = new Integer(weather);
        int time_int = new Integer(time);
        int img_id;
        boolean is_day = false;

        if(weather_type==800 || weather_type==801){
        }else{
            weather_type = weather_type / 100;
        }

        if(time_int>=6&&time_int<=18)
            is_day = true;

        switch (weather_type){
            case 2: //thunderstorm
                if(is_day){
                    img_id = getResources().getIdentifier("ic_weather_thunderstorm_day","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }else{
                    img_id = getResources().getIdentifier("ic_weather_thunderstorm_night","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }
                break;
            case 3: //drizzle
                img_id = getResources().getIdentifier("ic_weather_drizzle","drawable",getPackageName());
                imageView.setImageResource(img_id);
                break;
            case 5: //rain
                img_id = getResources().getIdentifier("ic_weather_rain","drawable",getPackageName());
                imageView.setImageResource(img_id);
                break;
            case 6: //snow
                img_id = getResources().getIdentifier("ic_weather_snow","drawable",getPackageName());
                imageView.setImageResource(img_id);
                break;
            case 7: //foggy
                img_id = getResources().getIdentifier("ic_weather_foggy","drawable",getPackageName());
                imageView.setImageResource(img_id);
                break;
            case 800: //clear
                if(is_day){
                    img_id = getResources().getIdentifier("ic_weather_clear_day","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }else{
                    img_id = getResources().getIdentifier("ic_weather_clear_night","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }
                break;
            case 801: //few clouds
                if(is_day){
                    img_id = getResources().getIdentifier("ic_weather_few_clouds_day","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }else{
                    img_id = getResources().getIdentifier("ic_weather_few_clouds_night","drawable",getPackageName());
                    imageView.setImageResource(img_id);
                }
                break; 
            case 8: //clouds
                img_id = getResources().getIdentifier("ic_weather_clouds","drawable",getPackageName());
                imageView.setImageResource(img_id);
                break;
        }
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

            /*
            Date cur_date = new Date();
            SimpleDateFormat get_hour = new SimpleDateFormat("hh");
            int cur_hour = new Integer(get_hour.format(cur_date));
            Log.d("get_weather",get_hour.format(cur_date));
            */

            if(result!=null){
                Log.d("get_weather",result.toString());
                for(int i=0;i<cnt;i++){
                    try {
                        int temp_int = Math.round(new Float(new Float(result.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp"))-274.15));
                        temp[i] = new Integer(temp_int).toString() + "℃";
                        weather[i] = result.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("id");
                        time[i] = (result.getJSONArray("list").getJSONObject(i).getString("dt_txt")).split(" ")[1].split(":")[0];

                        Log.d("get_weather","temp : "+temp[i]+" / weather : "+weather[i]+" / time : "+time[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        weather_container.setVisibility(View.GONE);
                        text_error.setVisibility(View.VISIBLE);
                    }
                }

                //show weather data
                temp1.setText(temp[0]);
                temp2.setText(temp[1]);
                temp3.setText(temp[2]);
                temp4.setText(temp[3]);
                temp5.setText(temp[4]);

                setWeatherImage(weather[0],weather1,time[0]);
                setWeatherImage(weather[1],weather2,time[1]);
                setWeatherImage(weather[2],weather3,time[2]);
                setWeatherImage(weather[3],weather4,time[3]);
                setWeatherImage(weather[4],weather5,time[4]);

                time1.setText(time[0]);
                time2.setText(time[1]);
                time3.setText(time[2]);
                time4.setText(time[3]);
                time5.setText(time[4]);
            }else{
                weather_container.setVisibility(View.GONE);
                text_error.setVisibility(View.VISIBLE);
            }
        }
    }

    private class OnSlidingDrawerOpenListener implements SlidingDrawer.OnDrawerOpenListener {
        @Override
        public void onDrawerOpened() {
            drawer_opened = true;
            getWeather();

            if(park_layout_opened){
                park_info_dialog.dismiss();
                park_layout_opened = false;
            }
        }
    }

    private class OnSlidingDrawerCloseListener implements SlidingDrawer.OnDrawerCloseListener {
        @Override
        public void onDrawerClosed() {
            drawer_opened = false;
        }
    }

}
