package com.example.studentmanagementsystem.service;

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
    private SendCallBack callBack;

    public BackgroundTaskAsync(Context mContext,SendCallBack callBack) {
        this.mContext = mContext;
        this.callBack=callBack;
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
            case Constants.ACTION_TYPE_ADD:
                dataBaseHelper.addData( full_name,roll_no);
                break;
            case Constants.ACTION_TYPE_EDIT:
                dataBaseHelper.update_name(full_name,roll_no);
                break;
            case Constants.ACTION_TYPE_DELETE:
                dataBaseHelper.deleteContact(roll_no);
                break;

            default:
                break;
        }
        return method;

    }

    @Override
    protected void onPostExecute(String s) {
        callBack.sendBack(s);
    }
    public interface SendCallBack{
        void sendBack(String s);

    }





}
