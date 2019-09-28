package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
/// TODO: 2019-08-19 modify layout(drawer:linear->constraint), add event info  
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.app_info_btn)
    ImageButton infoBtn;

    @BindView(R.id.main_layout)
    ConstraintLayout mainLayout;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;

    @BindView(R.id.map_image_view)
    PinView mapImageView;

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

    @BindView(R.id.event_container)
    ListView event_container;
    @BindView(R.id.weather_info)
    LinearLayout weather_container;
    @BindView(R.id.text_error)
    TextView text_error;
    @BindView(R.id.date_info)
    TextView date_info;

    private static SlidingDrawer bottomDrawer;

    private final int PARK_NUM = 11;

    private HashMap<String, String> weatherInfo = new HashMap<>();
    private ArrayList<EventListItem> eventList = new ArrayList<>();
    public static ArrayList<ParkListItem> parkList = new ArrayList<>();

    private Bitmap mapImg;

    private EventListAdapter eventListAdapter;

    private static boolean park_layout_opened = false;
    private static boolean drawer_opened = false;
    final String api_key = "d0c498afb7199ff9bf703f95c14e007a";
    final int cnt = 5;

    private static ParkInfoDialog park_info_dialog;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomDrawer = findViewById(R.id.bottom_drawer);

        //check connection - temp
        JSONObject get_park_info = new SQLSender().sendSQL("SELECT * from park;");
        try{
            if(!get_park_info.getBoolean("isError")){
                JSONObject park_info;
                ParkListItem item;
                int park_id;
                String park_name;
                String park_location;
                String park_number;
                String park_attraction;
                String park_facility;
                String park_img_src;

                for(int i=0;i<PARK_NUM;i++){
                    park_info = get_park_info.getJSONArray("result").getJSONObject(i);
                    park_id = park_info.getInt("pid");
                    park_name = park_info.getString("name");
                    park_location = park_info.getString("location");
                    park_number = park_info.getString("number");
                    park_attraction = park_info.getString("attraction");
                    park_facility = park_info.getString("facility");
                    park_img_src = park_info.getString("img_src");

                    item = new ParkListItem(park_id, park_name, park_img_src, park_location, park_number, park_attraction,
                            park_facility);
                    parkList.add(item);
                    Log.d("db_conn", parkList.get(i).getName());
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        //
        park_info_dialog = new ParkInfoDialog();

        ParkInfoCrawler.setMainContext(MainActivity.this);
        ParkInfoCrawler.start();

        mapImg = getBitmapFromVectorDrawable(MainActivity.this, R.drawable.map_image);

        mapImageView.setFragmentManager(getSupportFragmentManager());
        mapImageView.setImage(ImageSource.bitmap(mapImg));
        mapImageView.setZoomEnabled(false);
        mapImageView.setPanEnabled(true);
        mapImageView.setScaleX(1.3f);
        mapImageView.setScaleY(1.3f);

        ArrayList<MapPin> MapPins = new ArrayList();
        //dp-val pins
        MapPins.add(new MapPin(1190.2885f, 1103.7115f,1)); // 광나루
        MapPins.add(new MapPin(1100.8882f, 1184.4038f,2)); // 잠실
        MapPins.add(new MapPin(1026.4001f, 1044.5962f,3)); // 뚝섬
        MapPins.add(new MapPin(890.91345f, 1144.7885f,4)); // 잠원
        MapPins.add(new MapPin(764.24396f, 1224.6924f,5)); // 반포
        MapPins.add(new MapPin(680.4531f, 1094.8655f,6)); // 이촌
        MapPins.add(new MapPin(545.14905f, 1124.25f,7)); // 여의도
        MapPins.add(new MapPin(530.7223f, 991.6923f,8)); // 망원
        MapPins.add(new MapPin(443.0769f, 962.3654f,9)); // 난지
        MapPins.add(new MapPin(290.43268f, 1034.7115f,10)); //강서
        MapPins.add(new MapPin(386.0156f, 1086.8846f,11)); // 양화

        mapImageView.setPins(MapPins);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
                startActivity(intent);
            }
        });

        SlidingDrawer.OnDrawerOpenListener onDrawerOpenListener = new OnSlidingDrawerOpenListener();
        SlidingDrawer.OnDrawerCloseListener onDrawerCloseListener = new OnSlidingDrawerCloseListener();

        bottomDrawer.setOnDrawerOpenListener(onDrawerOpenListener);
        bottomDrawer.setOnDrawerCloseListener(onDrawerCloseListener);
    }

    static public void setDialogOpened(){
        if(drawer_opened){
            bottomDrawer.close();
            drawer_opened = false;
        }

        park_layout_opened = true;
    }

    // TODO: 2019-08-21 progress bar 추가  
    public void setData(){
        //use parsed data
        weatherInfo = ParkInfoCrawler.getWeatherInfo();
        eventList = ParkInfoCrawler.getEventList();

        eventListAdapter = new EventListAdapter(eventList);
        event_container.setAdapter(eventListAdapter);

        date_info.setText(weatherInfo.get("TODAY"));

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

                time1.setText(time[0]+":00");
                time2.setText(time[1]+":00");
                time3.setText(time[2]+":00");
                time4.setText(time[3]+":00");
                time5.setText(time[4]+":00");
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

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
