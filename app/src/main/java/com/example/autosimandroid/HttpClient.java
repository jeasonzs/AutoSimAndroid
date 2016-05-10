package com.example.autosimandroid;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpClient {
    private static String getFromUrl(String strUrl) {
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultData;
    }


    public static void get(final String url, final Handler handler) {
        new Thread(){
            @Override
            public void run()
            {
                String str = HttpClient.getFromUrl(url);
                if (str != null) {
                    Message msg = new Message();
                    JSONTokener jsonParse = new JSONTokener(str);
                    try {
                        JSONObject json = (JSONObject) jsonParse.nextValue();
                        msg.obj = json;
                        handler.sendMessage(msg);
                    }catch (JSONException e) {

                    }
                }
            }
        }.start();
    }
}
