package com.example.studentmanagementsystem.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;

public class BackgroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * in this overridden method student data is being updated ,deleted and added in database.
     * @param intent getting from addupdate fragment
     * @param flags
     * @param startId
     * @return
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StudentDataBaseHelper studentDataBase=new StudentDataBaseHelper(this);
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

        return START_NOT_STICKY;
    }

}
