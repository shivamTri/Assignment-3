package com.example.studentmanagementsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.service.BackgroundTask;
import com.example.studentmanagementsystem.model.GenerateDialog;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements BackgroundTask.CallBack {
   private EditText sName,sRollNo;
   private Button btn_addStudent;
   private StudentDetails mStudentDetails;
   private ArrayList<StudentDetails> studentDetailsArrayList;
   private String selectOnClick;
   private StudentBroadcastReceiver mStudentBroadcastReceiver;
   private int buttonWork;
   private GenerateDialog generateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentBroadcastReceiver=new StudentBroadcastReceiver();
        setContentView(R.layout.activity_student);
        generateDialog=new GenerateDialog(this);
        init();

        /**
         * selectOnClick is taking the data from intent sent by student Activity.
         */
        selectOnClick=getIntent().getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        /**
         * used if else if to check which action to be performed.
         */
        switch (selectOnClick){
            case Constants.DETAIL_VIEW:
                mStudentDetails =(StudentDetails) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
                sName.setText(mStudentDetails.getName().toUpperCase());
                sRollNo.setText(mStudentDetails.getRollNo().toUpperCase());
                sName.setEnabled(false);
                sRollNo.setEnabled(false);
                btn_addStudent.setVisibility(View.GONE);
                break;
            case Constants.DETAIL_ADD:
                buttonWork=1;
                studentDetailsArrayList=(ArrayList<StudentDetails>) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
                break;
            case Constants.DETAIL_EDIT:
                sRollNo.setEnabled(false);
                buttonWork=2;
                mStudentDetails =(StudentDetails) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
                sName.setText(mStudentDetails.getName().toUpperCase());
                sRollNo.setText(mStudentDetails.getRollNo().toUpperCase());
               // position=getIntent().getStringExtra(Constants.POSITION_STUDENT_DATA);
                break;
             default:
                 return;

        }

        btn_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String roll;
                name = sName.getText().toString().trim();
                roll = sRollNo.getText().toString().trim();
                switch (buttonWork) {
                    case 1:
                        if (name.equals("")) {
                            sName.setError(getString(R.string.blank_field));
                        } else if (!ValidUtil.validateName(name)) {
                            sName.setError(getString(R.string.invalid_name_message));

                        }
                        else if (roll.equals("")) {
                            sRollNo.setError(getString(R.string.blank_field));
                        }
                        else if (!ValidUtil.validateRollNumber(roll)) {
                            sRollNo.setError(getString(R.string.invalid_rollNo_message));
                        }
                        else if ( ValidUtil.isCheckValidId(roll,studentDetailsArrayList)) {
                            sRollNo.setError(getString(R.string.same_rollNo_message));
                        }
                        else{

                            StudentDetails studentDetails = new StudentDetails(name, roll);
                            Intent addIntent = new Intent();
                            addIntent.putExtra(Constants.STUDENT_DATA, studentDetails);
                            setResult(RESULT_OK, addIntent);
                            generateDialog.generateAlertDialog(roll,name,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);
                        }
                        break;

                    case 2:

                        if (name.equals("")) {
                            sName.setError(Constants.SET_ERROR_NAME);
                        }
                        else if (roll.equals("")) {
                          sRollNo.setError(getString(R.string.no_data));
                        }
                        else{
                            Intent eIntent = new Intent();
                            eIntent.putExtra(Constants.POSITION_STUDENT_NAME,name);
                           // eIntent.putExtra(Constants.POSITION_STUDENT_DATA,position);
                            eIntent.putExtra(Constants.POSITION_STUDENT_ROLL,roll);
                            setResult(RESULT_OK, eIntent);
                            generateDialog.generateAlertDialog(roll,name,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT);

                        }
                        break;

                     default:
                         return;

                }


            }
        });

    }

    private void init(){
        sName=findViewById(R.id.name);
        sRollNo=findViewById(R.id.roll);
        btn_addStudent =findViewById(R.id.addbutton);

    }

    @Override
    public void sendBack(String str) {
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        finish();
    }



    /**
     * in this method vibrator is  added as it vibrates when data is added or updated.
     */
    public class StudentBroadcastReceiver extends BroadcastReceiver {
        //private Bundle sendBundleFromThis;
        //recieving data signal from service and intent service whether action has been performed or not.
        @Override
        public void onReceive(Context context, Intent intent) {

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(Constants.VIBRATE_MILI_SECOND);
            Toast.makeText(context, intent.getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY), Toast.LENGTH_SHORT).show();
           finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStudentBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStudentBroadcastReceiver,intentFilter);
    }
}
