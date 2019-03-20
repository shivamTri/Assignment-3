package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanagementsystem.CommunicationFragment;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.StudentAddUpdateFragment;
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundTask;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements CommunicationFragment {


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
        fragmentTransaction.add(R.id.frag_container,studentAddUpdateFragment,"HEloo");
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
