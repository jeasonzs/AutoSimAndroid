package com.example.autosimandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public class VolDataFragment extends Fragment{
    private static ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vol_data, null);

        listView = (ListView) view.findViewById(R.id.listViewVolData);
        ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>> ();
        for(int i = 0; i < 32; i++) {
            HashMap<String,Object> map = new HashMap<String,Object>();
            int n1 = (int)(Math.random()*10000);
            int n2 = (int)(Math.random()*10000);

            map.put("item_text1","故障"+i);
            map.put("item_text2",n1/100.0);
            map.put("item_text3",n2/100.0);
            listItem.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(view.getContext(),
                listItem,
                R.layout.list_item_3,
                new String[] {"item_text1","item_text2","item_text3"},
                new int[] {R.id.item_text1,R.id.item_text2,R.id.item_text3});
        listView.setAdapter(adapter);


        return view;
    }
}
