package com.example.autosimandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;


public class WaveDataFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "autoSim";
    final static String wsuri = "ws://10.0.2.2:8000/service/wavePush.py";
//    final static String wsuri = "ws://192.168.10.175:8000/service/wavePush.py";

    private WaveView waveView;
    private final WebSocketConnection mConnection = new WebSocketConnection();
    private float pos1 = 0;
    private float girdy1 = 2000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wave_data, null);
        waveView = new WaveView(view.getContext(),14,8);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.wave_data);
        layout.addView(waveView);
        waveView.addChannel("ch1", 0xffff0000);
        waveView.setGirdValueY(girdy1);
        waveView.addChannel("ch2", 0xff0000ff);
        waveView.setGirdValueY(2000);
        startGetWave();
        waveView.setPos("ch1", pos1);
        ((Button)view.findViewById(R.id.btn_wave_x_zoom_in)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_x_zoom_out)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_y_zoom_in)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_y_zoom_out)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_move_up)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_move_down)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.btn_wave_reset)).setOnClickListener(this);

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
                    } catch (JSONException e) {}
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_wave_x_zoom_in:
                break;
            case R.id.btn_wave_x_zoom_out:
                break;
            case R.id.btn_wave_y_zoom_in:
                girdy1 += 50;
                waveView.setGirdValueY(girdy1);
                break;
            case R.id.btn_wave_y_zoom_out:
                if (girdy1 > 50) {
                    girdy1 -= 50;
                }
                waveView.setGirdValueY(girdy1);
                break;
            case R.id.btn_wave_move_up:
                pos1 += 0.1;
                waveView.setPos("ch1",pos1);
                break;
            case R.id.btn_wave_move_down:
                pos1 -= 0.1;
                waveView.setPos("ch1",pos1);
                break;
            case R.id.btn_wave_reset:
                break;

        }
    }
}
