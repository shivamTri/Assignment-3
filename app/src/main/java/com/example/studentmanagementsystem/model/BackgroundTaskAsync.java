package com.example.studentmanagementsystem.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;


/**
 * this is background class for adding and updating data from data base.
 */
public class BackgroundTaskAsync extends AsyncTask<String,Void,String> {
    private Context mContext;

    public BackgroundTaskAsync(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * this method is called when Async executes.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     *
     * @param params parameters which are being passed from the Student activity.
     * @return null
     */
    @Override
    protected String doInBackground(String... params) {

        StudentDataBaseHelper dataBaseHelper = new StudentDataBaseHelper(mContext);

        String method = params[0];
        String roll_no = params[1];
        String full_name = params[2];
        switch (method) {
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD:
                dataBaseHelper.addData(roll_no, full_name);
                return Constants.ADD_TOAST;

            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT:
                dataBaseHelper.update_name(full_name,roll_no);
                return Constants.UPDATE_TOAST;

            default:
        }
        return null;

    }






}
