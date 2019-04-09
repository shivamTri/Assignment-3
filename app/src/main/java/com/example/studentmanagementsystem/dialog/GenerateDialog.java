package com.example.studentmanagementsystem.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.service.BackgroundIntentService;
import com.example.studentmanagementsystem.service.BackgroundService;
import com.example.studentmanagementsystem.service.BackgroundTaskAsync;

/**
 * this class is for generating dialog box for database operations on delete,add etc.
 */
public class GenerateDialog {
        private Context mContext;
        private BackgroundTaskAsync.SendCallBack callBack;

    /**
     * constructor having context and call back as parameter.
     * @param mContext
     * @param callBack
     */
    public GenerateDialog(Context mContext, BackgroundTaskAsync.SendCallBack callBack) {
            this.mContext = mContext;
            this.callBack=callBack;
        }

        /**
         *
         * @param rollNo
         * @param fullName
         * @param typeOperation
         */
        public void generateAlertDialog(final String rollNo, final String fullName, final String typeOperation){

            final AlertDialog.Builder mBuilder=new AlertDialog.Builder(mContext);
            mBuilder.setTitle(R.string.dialog_title);


            mBuilder.setSingleChoiceItems(Constants.ITEM_DAILOG, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    int buttonWork=which;
                    dialog.dismiss();

                    switch (buttonWork){

                        case Constants.ASYNC_TASK:
                            (new BackgroundTaskAsync(mContext,callBack)).execute(typeOperation,rollNo,fullName);
                            break;

                        case Constants.SERVICE:
                            Intent intentService=new Intent(mContext, BackgroundService.class);
                            doTask(intentService,rollNo,fullName,typeOperation);
                            break;

                        case Constants.INTENT_SERVICE:

                            Intent intentForService=new Intent(mContext, BackgroundIntentService.class);
                            doTask(intentForService,rollNo,fullName,typeOperation);
                            break;
                    }

                }
            });

            mBuilder.setNeutralButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog mDialog=mBuilder.create();
            mDialog.show();

        }

        private void doTask(Intent intent,String rollNo,String fullName,String typeOperation){
            intent.putExtra(Constants.ACTION_TYPE,typeOperation);
            intent.putExtra(Constants.ROLL_NO,rollNo);
            intent.putExtra(Constants.NAME,fullName);
            mContext.startService(intent);

        }

    }

