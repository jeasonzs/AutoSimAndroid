package com.example.autosimandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;


public class WaveDataFragment extends Fragment{
    private static final String TAG = "autoSim";
    final static String wsuri = "ws://10.0.2.2:8000/service/wavePush.py";
//    final static String wsuri = "ws://192.168.10.175:8000/service/wavePush.py";

    private WaveView waveView;
    private final WebSocketConnection mConnection = new WebSocketConnection();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wave_data, null);
        waveView = new WaveView(view.getContext(),14,8);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.wave_data);
        layout.addView(waveView);
        waveView.addChannel("ch1", 0xffff0000);
        waveView.setGirdValueY(2000);

        waveView.addChannel("ch2",0xff0000ff);
        waveView.setGirdValueY(2000);


        startGetWave();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopGetWave();
    }


    private void startGetWave() {
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
                        JSONArray waves = json.getJSONArray("wave");
                        int[] array1 = utils.jsonGetIntArray(waves);
                        waveView.setWave("ch1",array1);
                        waveView.invalidate();
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

    private void stopGetWave() {
        mConnection.disconnect();
    }
}
