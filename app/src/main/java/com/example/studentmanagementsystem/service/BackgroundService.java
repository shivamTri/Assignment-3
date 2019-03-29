package com.example.studentmanagementsystem.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Switch;
import android.widget.Toast;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;

public class BackgroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * in this method data is being updated and added into database
     * @param intent
     * @param flags
     * @param startId
     * @return START_NOT_STICKY so that service doesnt get started again.
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StudentDataBaseHelper studentDataBaseHelper=new StudentDataBaseHelper(this);

        String action=intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        switch(action){
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD:
                studentDataBaseHelper.addData(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL),intent.getStringExtra(Constants.POSITION_STUDENT_NAME));
                intent.setAction(Constants.FILTER_ACTION_KEY);
                break;
            case  Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT:
                studentDataBaseHelper.update_name(intent.getStringExtra(Constants.POSITION_STUDENT_NAME),intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
                intent.setAction(Constants.FILTER_ACTION_KEY);
                break;
            case  Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_DELETE:
                studentDataBaseHelper.deleteContact(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
                intent.setAction(Constants.FILTER_KEY_DELETE);
                break;

        }

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        stopSelf();

        return START_NOT_STICKY;

        /*if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD)){
            studentDataBaseHelper.addData(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL),intent.getStringExtra(Constants.POSITION_STUDENT_NAME));
        }
        else if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT)){
            studentDataBaseHelper.update_name(intent.getStringExtra(Constants.POSITION_STUDENT_NAME),intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
        }
        else if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_DELETE)){
            studentDataBaseHelper.deleteContact(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
        }*/

    }
}
