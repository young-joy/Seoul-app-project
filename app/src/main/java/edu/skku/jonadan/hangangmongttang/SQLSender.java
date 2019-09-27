package edu.skku.jonadan.hangangmongttang;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class SQLSender extends Thread {
    private String SERVER_IP = "15.164.233.89";
    private JSONObject result;

    public SQLSender(){
        result = null;
    }

    JSONObject sendSQL(String sql) {
        Thread ct = new ConnectionTask(sql);
        try {
            ct.start();
            ct.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Exception", "InterruptedException occurred in SQLSender.java");
        }
        return result;
    }

    private class ConnectionTask extends Thread {
        String sql;

        private ConnectionTask(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            HttpURLConnection con = null;
            try {
                URL url = new URL("http://"+SERVER_IP+"/sql?query="+sql);
                con = (HttpURLConnection)url.openConnection();

                Log.d("db_conn",sql);

                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestProperty("Accept", "application/json");

                Log.d("db_conn","url : "+url.toString());
                con.setRequestMethod("GET");

                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                result = new JSONObject(sb.toString());
                Log.d("db_conn",result.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", "IOException occurred in SQLSender.java");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Exception", "JSONException occurred in SQLSender.java");
            } finally {
                if (con != null)
                    con.disconnect();
            }
        }
    }
}
