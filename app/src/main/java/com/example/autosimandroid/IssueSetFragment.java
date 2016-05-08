package com.example.autosimandroid;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class IssueSetFragment extends Fragment{
    private static String[] strs = new String[32];
    private static final String[] issueTypeStrs = new String[]{"清除故障","断路","对地断路","串联电阻",
            "并联电阻","间隙性断路","间隙性对地断路","间隙性串联电阻","间隙性并联电阻"};
    private static ListView listView;
    private static ListView listViewIssueType;
    private SmartAdapter<String> adpIssueNum;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        for(int i = 0;i<32; i++) {
            strs[i] = "故障"+String.valueOf(i);
        }
        View view = inflater.inflate(R.layout.fragment_issue_set, null);

        listView = (ListView) view.findViewById(R.id.listViewIssueNum);
        adpIssueNum = new SmartAdapter<String>(view.getContext(), R.layout.list_item, strs);
        listView.setAdapter(adpIssueNum);

        listViewIssueType = (ListView) view.findViewById(R.id.listViewIssueType);
        listViewIssueType.setAdapter(new SmartAdapter<String>(view.getContext(), R.layout.list_item, issueTypeStrs));


        Log.v("autoSim","select=" + listView.getSelectedItemPosition());
        ImageButton btn = (ImageButton) view.findViewById(R.id.btn_del_all_issue);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SmartAdapter<String>) parent.getAdapter()).setSelection(position);
                ((SmartAdapter<String>) parent.getAdapter()).notifyDataSetChanged();

                ((SmartAdapter<String>) listViewIssueType.getAdapter()).setSelection(0);
                ((SmartAdapter<String>) listViewIssueType.getAdapter()).notifyDataSetChanged();
                Log.v("autoSim", "click=" + position + ",select=" + parent.getSelectedItemPosition());
            }
        });

        listViewIssueType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SmartAdapter<String>)parent.getAdapter()).setSelection(position);
                ((SmartAdapter<String>)parent.getAdapter()).notifyDataSetChanged();
                Log.v("autoSim", "click=" + position + ",select=" + parent.getSelectedItemPosition());
            }
        });
        return view;
    }

    public class SmartAdapter<T> extends ArrayAdapter {
        private int position = -1;
        public SmartAdapter(Context context, @LayoutRes int resource, @NonNull T[] objects) {
            super(context,resource,objects);
        }

        public void setSelection(int position) {
            this.position = position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);
            if(this.position == position) {
                view.setBackgroundResource(R.color.light_orange);
            }
            else {
                view.setBackgroundResource(R.color.light_gray);
            }
            return view;
        }
    }





}
