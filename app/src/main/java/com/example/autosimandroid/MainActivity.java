package com.example.autosimandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private IssueSetFragment issueSetFragment = new IssueSetFragment();
    private VolDataFragment volDataFragment = new VolDataFragment();
    private WaveDataFragment waveDataFragment = new WaveDataFragment();
    private SettingFragment settingFragment = new SettingFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.v("autoSim","click"+String.valueOf(checkedId)+"___"+String.valueOf(R.id.rb_1));
                android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rb_1:
                        transaction.replace(R.id.tabcontent, issueSetFragment);
                        break;
                    case R.id.rb_2:
                        transaction.replace(R.id.tabcontent, volDataFragment);
                        break;
                    case R.id.rb_3:
                        transaction.replace(R.id.tabcontent, waveDataFragment);
                        break;
                    case R.id.rb_4:
                        transaction.replace(R.id.tabcontent, settingFragment);
                        break;
                    default:
                        transaction.replace(R.id.tabcontent, issueSetFragment);
                        break;
                }
                transaction.commit();
            }
        });

        radioGroup.check(R.id.rb_1);
    }


}
