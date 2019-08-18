package edu.skku.jonadan.hangangmongttang;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkInfoCrawler {
    private static String htmlUrl = "https://hangang.seoul.go.kr/";
    private static ArrayList<HashMap<String, String>> eventList = new ArrayList<>();
    private static HashMap<String, String> weatherInfo = new HashMap<>();
    private static int events_cnt = 0;

    public static HashMap<String, String> getWeatherInfo() {
        return weatherInfo;
    }

    public static ArrayList<HashMap<String, String>> getEventList(){
        return eventList;
    }

    public static void start(){
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private static class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                events_cnt = 0;
                Document doc = Jsoup.connect(htmlUrl).get();

                //get event_info
                Elements events = doc.select("div.left h5");
                Elements events_place = doc.select("div.left span.place");
                Elements events_date = doc.select("p.right span.date");
                Elements events_time = doc.select("p.right span.time");

                for(Element e: events){
                    HashMap<String, String> eventInfo = new HashMap<>();
                    eventInfo.put("NAME", e.text().trim());
                    eventInfo.put("PLACE", events_place.get(events_cnt).text().trim());
                    eventInfo.put("DATE", events_date.get(events_cnt).text().trim());
                    eventInfo.put("TIME", events_time.get(events_cnt).text().trim());

                    Log.d("jsoup_parser",eventInfo.get("NAME"));
                    eventList.add(eventInfo);
                    events_cnt++;
                }

                //get weather_info
                Element weather_today = doc.selectFirst("div.weather span");
                Element weather_temp = doc.selectFirst("div.weather p.weather_txt");
                Elements weather_dust = doc.select("div.weather span.air_grade");

                weatherInfo.put("TODAY", weather_today.text().trim());
                weatherInfo.put("TEMP", weather_temp.text().trim());
                weatherInfo.put("DUST-G1", weather_dust.get(0).text().trim());
                weatherInfo.put("DUST-G2", weather_dust.get(1).text().trim());

                Log.d("jsoup_parser_weather",weatherInfo.get("TODAY"));
                Log.d("jsoup_parser_weather",weatherInfo.get("TEMP"));
                Log.d("jsoup_parser_weather",weatherInfo.get("DUST-G1"));
                Log.d("jsoup_parser_weather",weatherInfo.get("DUST-G2"));
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
