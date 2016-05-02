package com.example.autosimandroid;

import android.app.Fragment;
import android.os.Bundle;
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
import android.widget.TextView;


public class IssueSetFragment extends Fragment{
    private static String[] strs = new String[32];
    private static final String[] issueTypeStrs = new String[]{"清除故障","断路","对地断路","串联电阻",
            "并联电阻","间隙性断路","间隙性对地断路","间隙性串联电阻","间隙性并联电阻"};
    private static ListView listView;
    private static ListView listViewIssueType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        for(int i = 0;i<32; i++) {
            strs[i] = "item"+String.valueOf(i);
        }
        View view = inflater.inflate(R.layout.fragment_issue_set, null);

        listView = (ListView) view.findViewById(R.id.listViewIssueNum);
        listView.setAdapter(new ArrayAdapter<String>(view.getContext(),R.layout.list_item, strs));

        listViewIssueType = (ListView) view.findViewById(R.id.listViewIssueType);
        listViewIssueType.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.list_item, issueTypeStrs));

        ImageButton btn = (ImageButton) view.findViewById(R.id.btn_del_all_issue);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 32; i++) {
                    strs[i] = "故障" + String.valueOf(i);
                }
            }
        });
        return view;
    }
}
