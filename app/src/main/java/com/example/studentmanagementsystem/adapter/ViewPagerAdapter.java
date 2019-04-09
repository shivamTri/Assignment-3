package com.example.studentmanagementsystem.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.studentmanagementsystem.fragment.StudentAddUpdateFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;

/**
 * this is adapter class for view pager.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

        private StudentAddUpdateFragment studentAddUpdateFragment;
        private StudentListFragment studentListFragment;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    if (studentListFragment == null) {
                        studentListFragment = new StudentListFragment();
                    }
                    return studentListFragment;
                case 1:
                    if (studentAddUpdateFragment == null) {
                        studentAddUpdateFragment = new StudentAddUpdateFragment();
                    }
                    return studentAddUpdateFragment;
            }
            return null;
        }

    /**
     * @return count as how many tabs are there to
     */
    @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        /**
         * setting tab name to respective tabs.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Student List";
                case 1:
                    return "Add/Update";
            }
            return null;
        }
    }
