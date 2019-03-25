package com.example.studentmanagementsystem.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.studentmanagementsystem.model.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.StudentAddUpdateFragment;

public class StudentActivity extends AppCompatActivity implements CommunicationFragmentInterface {


    private Bundle bundle;
    private StudentAddUpdateFragment studentAddUpdateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        bundle=getIntent().getExtras();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        studentAddUpdateFragment=new StudentAddUpdateFragment();
        fragmentTransaction.add(R.id.frag_container,studentAddUpdateFragment,"");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentAddUpdateFragment.update(bundle);
    }

    @Override
    public void communication(Bundle bundle) {

    }
}
