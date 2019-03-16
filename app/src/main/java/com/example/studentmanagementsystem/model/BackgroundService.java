package com.example.studentmanagementsystem.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;

public class BackgroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StudentDataBaseHelper studentDataBaseHelper=new StudentDataBaseHelper(this);
        if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD)){
            studentDataBaseHelper.addData(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL),intent.getStringExtra(Constants.POSITION_STUDENT_NAME));
        }else if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT)){
            studentDataBaseHelper.update_name(intent.getStringExtra(Constants.POSITION_STUDENT_NAME),intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
        }
        stopSelf();

        return START_NOT_STICKY;
    }
}
