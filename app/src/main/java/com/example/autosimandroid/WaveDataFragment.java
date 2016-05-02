package com.example.autosimandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class WaveDataFragment extends Fragment{
    private WaveView waveView;
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
        waveView.setWave("ch1",data);
        return view;
    }
}
