package com.example.studentmanagementsystem.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.studentmanagementsystem.adapter.ViewPagerAdapter;
import com.example.studentmanagementsystem.Interface.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.StudentAddUpdateFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;
//import com.example.studentmanagementsystem.database.StudentDataBaseHelper;


public class MainActivity extends AppCompatActivity implements CommunicationFragmentInterface {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    /**
     * fragments are being changed in this method as user clicks on add button
     */
    public void replaceFrag() {
        if(viewPager.getCurrentItem()==0) {
            viewPager.setCurrentItem(1);
        }else
            viewPager.setCurrentItem(0);
    }


    /**
     * this method is recieving data from fragments and changing the fragments.
     * @param bundle
     */
    @Override
    public void communication(Bundle bundle) {
        if(viewPager.getCurrentItem()==0) {
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 1;
            StudentAddUpdateFragment studentAddUpdateFragment = (StudentAddUpdateFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentAddUpdateFragment.addStudent(bundle);
        }else if(viewPager.getCurrentItem()==1){
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 0;
            StudentListFragment studentListFragment = (StudentListFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentListFragment.update(bundle);
        }
        replaceFrag();
    }

    /**
     * initialization of all views of mainactivity
     */
    private void init(){
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }


}



