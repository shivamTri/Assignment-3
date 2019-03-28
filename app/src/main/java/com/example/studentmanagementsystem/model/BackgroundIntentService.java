package com.example.studentmanagementsystem.model;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;

public class BackgroundIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BackgroundIntentService() {
        super("BackgroundIntentService");

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param intent
     * this method is used to add data and update data in database using intent service.
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        StudentDataBaseHelper studentDataBaseHelper=new StudentDataBaseHelper(this);
        if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD)){
            studentDataBaseHelper.addData(intent.getStringExtra(Constants.POSITION_STUDENT_ROLL),intent.getStringExtra(Constants.POSITION_STUDENT_NAME));
        }else if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT)){
            studentDataBaseHelper.update_name(intent.getStringExtra(Constants.POSITION_STUDENT_NAME),intent.getStringExtra(Constants.POSITION_STUDENT_ROLL));
        }
        intent.setAction(Constants.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
