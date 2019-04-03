package com.example.studentmanagementsystem.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.studentmanagementsystem.adapter.ViewPagerAdapter;
import com.example.studentmanagementsystem.model.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.StudentAddUpdateFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;
import com.example.studentmanagementsystem.adapter.StudentAdaptor;
//import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CommunicationFragmentInterface {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("StudentActivity");
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);


    }

    public void replaceFrag() {
        if(viewPager.getCurrentItem()==0) {
            viewPager.setCurrentItem(1);
        }else
            viewPager.setCurrentItem(0);
    }



    @Override
    public void communication(Bundle bundle) {
        if(viewPager.getCurrentItem()==0) {
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 1;
            StudentAddUpdateFragment studentAddUpdateFragment = (StudentAddUpdateFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentAddUpdateFragment.update(bundle);
        }else if(viewPager.getCurrentItem()==1){
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 0;
            StudentListFragment studentListFragment = (StudentListFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentListFragment.update(bundle);
        }
        replaceFrag();
    }


    }



