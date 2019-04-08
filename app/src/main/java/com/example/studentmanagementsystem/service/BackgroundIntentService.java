package com.example.studentmanagementsystem.model;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

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
     * @param intent this method is used to add data and update data in database using intent service.
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        StudentDataBaseHelper studentDataBase = new StudentDataBaseHelper(this);
        String action = intent.getStringExtra(Constants.ACTION_TYPE);
        switch (action) {
            case Constants.ACTION_TYPE_ADD:
                studentDataBase.addData(intent.getStringExtra(Constants.NAME), intent.getStringExtra(Constants.ROLL_NO));
                intent.setAction(Constants.FILTER_ACTION_KEY);
                break;
            case Constants.ACTION_TYPE_EDIT:
                studentDataBase.update_name(intent.getStringExtra(Constants.NAME), intent.getStringExtra(Constants.ROLL_NO));
                intent.setAction(Constants.FILTER_ACTION_KEY);
                break;
            case Constants.ACTION_TYPE_DELETE:
                studentDataBase.deleteContact(intent.getStringExtra(Constants.ROLL_NO));
                intent.setAction(Constants.FILTER_KEY_DELETE);
                break;
            default:
                break;

        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        stopSelf();

    }
}
