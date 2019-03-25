package com.example.studentmanagementsystem.model;

import android.app.IntentService;
import android.content.Intent;

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
            studentDataBaseHelper.addData(intent.getStringExtra(Constants.ROLL_NO),intent.getStringExtra(Constants.NAME));
        }else if(intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY).equals(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT)){
            studentDataBaseHelper.update_name(intent.getStringExtra(Constants.NAME),intent.getStringExtra(Constants.ROLL_NO));
        }
    }
}
