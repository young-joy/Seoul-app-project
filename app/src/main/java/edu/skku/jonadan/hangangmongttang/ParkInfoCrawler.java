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
    private static ArrayList<String> eventList = null;
    private static ArrayList<HashMap<String, String>> eventInfo = null;
    private String htmlContentInStringFormat="";

    int cnt = 0;

    public static void start(){
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    public static ArrayList<String> getEvents(){
        return eventList;
    }

    private static class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(htmlUrl).get();
                Elements events = doc.select("div.left");

                for(Element e: events){
                    Log.d("jsoup_parser", e.text());
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
