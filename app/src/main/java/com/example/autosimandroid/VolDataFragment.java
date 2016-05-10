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
    final static String wsuri = "ws://10.0.2.2:8000/service/volPush.py";

    private ListView listView;
    private final ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>> ();
    private SimpleAdapter adapter;
    private final WebSocketConnection mConnection = new WebSocketConnection();

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
        for(int i = 0; i < 32; i++) {
            HashMap<String,Object> map = new HashMap<String,Object>();
            int n1 = i < vol1.length?vol1[i]:0;
            int n2 = i < vol2.length?vol2[i]:0;

            map.put("item_text1","故障"+i);
            map.put("item_text2",n1/100.0);
            map.put("item_text3",n2/100.0);
            listItem.add(map);
        }
        adapter.notifyDataSetChanged();
    }

}
