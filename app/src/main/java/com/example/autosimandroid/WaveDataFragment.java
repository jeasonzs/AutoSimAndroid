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
import java.lang.*;


public class WaveDataFragment extends Fragment{
    private WaveView waveView;
    private static int start = 0;
    private Thread thread;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.v("autoSim","timer");
            super.handleMessage(msg);
            int[] waveData = new int[1600];
            for (int i = 0; i < 1600; i++) {
                waveData[i] = (int)(Math.round(6 * 1000 * Math.sin(2 * (i+start) * Math.PI / 180)));
            }
            waveView.setWave("ch1", waveData);
            start += 2;
            waveView.invalidate();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wave_data, null);
        waveView = new WaveView(view.getContext(),14,8);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.wave_data);
        layout.addView(waveView);
        waveView.addChannel("ch1",0xffffffff);
        waveView.setGirdValueY(2000);
        int[] data = new int[600];
        for(int i = 0; i < 600; i++) {
            data[i] = i*10;
        }
        waveView.setWave("ch1", data);





        thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        return view;
    }
}
