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
    private static ArrayList<HashMap<String, String>> eventList = null;

    private static int events_cnt = 0;

    public static void start(){
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    public static ArrayList<HashMap<String, String>> getEventList(){
        return eventList;
    }

    private static class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                events_cnt = 0;
                Document doc = Jsoup.connect(htmlUrl).get();
                Elements events = doc.select("div.left h5");
                Elements events_place = doc.select("div.left span.place");
                Elements events_date = doc.select("p.right span.date");
                Elements events_time = doc.select("p.right span.time");

                for(Element e: events){
                    HashMap<String, String> eventInfo = new HashMap<>();
                    Log.d("jsoup_parser", e.text());
                    Log.d("jsoup_parser", events_date.get(events_cnt).text());

                    eventInfo.put("NAME",e.text().trim());
                    eventInfo.put("PLACE",events_place.get(events_cnt).text().trim());
                    eventInfo.put("DATE", events_date.get(events_cnt).text().trim());
                    eventInfo.put("TIME", events_time.get(events_cnt).text().trim());

                    eventList.add(eventInfo);
                    events_cnt++;
                }
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
