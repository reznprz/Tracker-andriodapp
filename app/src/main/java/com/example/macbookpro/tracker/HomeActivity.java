package com.example.macbookpro.tracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager=findViewById(R.id.myViewPager);
        tabLayout=findViewById(R.id.myTablayout);
        createViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();
      intent =new Intent(this,MyService.class);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intent);
    }
    public void createViewPager() {
        tabLayout.setupWithViewPager(viewPager);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public class MyFragmentAdapter extends FragmentPagerAdapter {
        String[] fragmentname = {"Summary", "Map"};

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentname[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SummaryFragment summaryFragment=new SummaryFragment();
                    return summaryFragment;
                case 1:
                    MyMapFragment mapFragment=new MyMapFragment();
                    return  mapFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return fragmentname.length;
        }
    }
}


