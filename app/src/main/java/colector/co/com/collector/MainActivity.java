package colector.co.com.collector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import colector.co.com.collector.fragments.SurveyAvailable;
import colector.co.com.collector.settings.AppSettings;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        buildTabs();
    }

    private void buildTabs(){


        mTabHost.addTab(
                mTabHost.newTabSpec(AppSettings.TAB_ID_AVAILABLE_SURVEY).setIndicator("Tab 1", null),
                SurveyAvailable.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec(AppSettings.TAB_ID_DONE_SURVEY).setIndicator("Tab 2", null),
                SurveyAvailable.class, null);
    }





}
