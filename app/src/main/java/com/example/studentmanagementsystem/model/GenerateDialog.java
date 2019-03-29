package com.example.studentmanagementsystem.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.service.BackgroundIntentService;
import com.example.studentmanagementsystem.service.BackgroundService;
import com.example.studentmanagementsystem.service.BackgroundTask;

public class GenerateDialog {
    private Context mContext;
    private static final String[] ITEM_DAILOG={"Async task","Service","Intent service"};
    private static final int ASYNC_TASK=0;
    private static final int SERVICE=1;
    private static final int INTENT_SERVICE=2;
   // private final static String ALERT_TITLE="Choose Action";



    public GenerateDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void generateAlertDialog(final String rollNo, final String fullName, final String typeOperation){

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(mContext);
        mBuilder.setTitle(R.string.alert_title_delete);


//setting SingleChoiceItem onClick
        mBuilder.setSingleChoiceItems(ITEM_DAILOG, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//set which choice is selected
                int buttonWork=which;
                dialog.dismiss();

                switch (buttonWork){

                    case ASYNC_TASK:
                        (new BackgroundTask(mContext, (BackgroundTask.CallBack)mContext)).execute(typeOperation,rollNo,fullName);
                        break;

                    case SERVICE:
                        Intent intentService=new Intent(mContext, BackgroundService.class);
                        doTask(intentService,rollNo,fullName,typeOperation);
                        break;

                    case INTENT_SERVICE:


                        Intent intentForService=new Intent(mContext, BackgroundIntentService.class);
                        doTask(intentForService,rollNo,fullName,typeOperation);
                        break;
                }

            }
        });

        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

    private void doTask(Intent intent,String rollNo,String fullName,String typeOperation){
        intent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,typeOperation);
        intent.putExtra(Constants.POSITION_STUDENT_ROLL,rollNo);
        intent.putExtra(Constants.POSITION_STUDENT_NAME,fullName);
        mContext.startService(intent);

    }


}
