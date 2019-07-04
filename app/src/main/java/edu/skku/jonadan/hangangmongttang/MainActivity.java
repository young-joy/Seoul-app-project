package edu.skku.jonadan.hangangmongttang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    final String api_key = "d0c498afb7199ff9bf703f95c14e007a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather(35,139);
            }
        });
    }

    private void getWeather(double lat, double lng){
        String url = "http://api.openweathermap.org/data/2.5/forecast?cnt=5&q=London&appid="+api_key;

        ReceiveWeatherTask receiveUseTask = new ReceiveWeatherTask();
        receiveUseTask.execute(url);
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject>{
        OkHttpClient client = new OkHttpClient();

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
            if(result!=null){
                Log.d("get_weather",result.toString());
            }
        }
    }
}
