package com.example.autosimandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class VolDataFragment extends Fragment{
    private static final String TAG = "autoSim";
    //    private final static String host = "10.0.2.2";
    private static final String host = "localhost";
    final static String wsuri = "ws://"+host+":8000/service/volPush.py";

    private ListView listView;
    private final ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>> ();
    private SimpleAdapter adapter;
    private final WebSocketConnection mConnection = new WebSocketConnection();

    private static final String[] strs = {"质量空气流量加热电源线",
            "质量空气流量加热信号线",
            "进气温度传感器信号线",
            "加热型氧传感器2信号线",
            "加热型氧传感器1信号线",
            "进气凸轮轴位置传感器信号线",
            "排气凸轮轴位置传感器信号线",
            "油门踏板位置传感器1电源线",
            "油门踏板位置传感器2电源线",
            "加热型氧传感器2加热丝电源线",
            "加热型氧传感器1加热丝电源线",
            "进气凸轮轴位置传感器电源线",
            "进气凸轮轴位置传感器接地线",
            "排气凸轮轴位置传感器电源线",
            "排气凸轮轴位置传感器接地线",
            "质量空气流量加热接地线",
            "进气温度传感器接地线",
            "蒸发排放碳罐吹洗电磁阀电源线",
            "蒸发排放碳罐吹洗电磁阀接地线",
            "故障19"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_vol_data, null);
        listView = (ListView) view.findViewById(R.id.listViewVolData);
        adapter = new SimpleAdapter(view.getContext(),
                listItem,
                R.layout.list_item_3,
                new String[] {"item_text1","item_text2","item_text3"},
                new int[] {R.id.item_text1,R.id.item_text2,R.id.item_text3});
        listView.setAdapter(adapter);
        startGetVol();
        setVolData(null,null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopGetVol();
    }


    private void startGetVol() {
        try {
            mConnection.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                }

                @Override
                public void onTextMessage(String payload) {
                    JSONTokener jsonParse = new JSONTokener(payload);
                    try {
                        JSONObject json = (JSONObject) jsonParse.nextValue();
                        JSONArray vols = json.getJSONArray("vol");
                        JSONArray vol1 = vols.getJSONArray(0);
                        JSONArray vol2 = vols.getJSONArray(1);
                        int[] volArray1 = utils.jsonGetIntArray(vol1);
                        int[] volArray2 = utils.jsonGetIntArray(vol2);
                        setVolData(volArray1, volArray2);
                    } catch (JSONException e) {
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
        }
    }

    private void stopGetVol() {
        mConnection.disconnect();
    }

    private void setVolData(int[] vol1,int[] vol2) {
        listItem.clear();
        for(int i = 0; i < strs.length; i++) {
            HashMap<String,Object> map = new HashMap<String,Object>();
            int n1 = (vol1 == null )?0:(i < vol1.length?vol1[i]:0);
            int n2 = (vol2 == null )?0:(i < vol2.length?vol2[i]:0);

            map.put("item_text1",strs[i]);
            map.put("item_text2",n1/100.0);
            map.put("item_text3",n2/100.0);
            listItem.add(map);
        }
        adapter.notifyDataSetChanged();
    }

}
