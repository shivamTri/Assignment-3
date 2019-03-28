package com.example.studentmanagementsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundTask;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
   private EditText sName,sRollNo;
   private Button btn_addStudent;
   private StudentDetails mStudentDetails;
   private ArrayList<StudentDetails> studentDetailsArrayList;
   private String selectOnClick;
   private StudentBroadcastReceiver mStudentBroadcastReceiver;
   private int buttonWork;
    private static final String DETAIL_VIEW="View";
   private static final String DETAIL_ADD="Add";
   private static final String DETAIL_EDIT="Edit";
   private static final int REQUEST_CODE_EDIT=2;
   private static final int ASYNC_TASK=0;
   private static final int SERVICE=1;
   private static final int INTENT_SERVICE=2;
   private static final String[] ITEM_DAILOG={"Async task","Service","Intent service"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudentBroadcastReceiver=new StudentBroadcastReceiver();
        setContentView(R.layout.activity_student);
        init();

        /**
         * selectOnClick is taking the data from intent sent by student Activity.
         */
        selectOnClick=getIntent().getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        /**
         * used if else if to check which action to be performed.
         */
        switch (selectOnClick){
            case DETAIL_VIEW:
                mStudentDetails =(StudentDetails) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
                sName.setText(mStudentDetails.getName().toUpperCase());
                sRollNo.setText(mStudentDetails.getRollNo().toUpperCase());
                sName.setEnabled(false);
                sRollNo.setEnabled(false);
                btn_addStudent.setVisibility(View.GONE);
                break;
            case DETAIL_ADD:
                buttonWork=1;
                studentDetailsArrayList=(ArrayList<StudentDetails>) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
                break;
            case DETAIL_EDIT:
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
                            generateAlertDialog(roll,name,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);
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
                            generateAlertDialog(roll,name,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT);

                        }
                        break;

                     default:
                         return;

                }


            }
        });

    }

    /**
     * @param roll is roll number of the student which is going to be matched if there is any existing roll number or not.
     * @return
     */



    private void init(){
        sName=findViewById(R.id.name);
        sRollNo=findViewById(R.id.roll);
        btn_addStudent =findViewById(R.id.addbutton);

    }

    private void generateAlertDialog(final String rollNo, final String fullName, final String typeOperation){

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(StudentActivity.this);
        if(buttonWork==REQUEST_CODE_EDIT)
            mBuilder.setTitle(R.string.dialog_title_update);
        else
            mBuilder.setTitle(R.string.dialog_title_add);

//setting SingleChoiceItem onClick
        mBuilder.setSingleChoiceItems(ITEM_DAILOG, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//set which choice is selected
                buttonWork=which;
                dialog.dismiss();

                switch (buttonWork){
                    case ASYNC_TASK:
                        (new BackgroundTask(StudentActivity.this)).execute(typeOperation,rollNo,fullName);
                        finish();
                        break;
                    case SERVICE:
                        Intent intentService=new Intent(StudentActivity.this, BackgroundService.class);
                        doTask(intentService,rollNo,fullName,typeOperation);

                        break;
                    case INTENT_SERVICE:
                        Intent intentForService=new Intent(StudentActivity.this, BackgroundIntentService.class);
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
        startService(intent);

    }
    public class StudentBroadcastReceiver extends BroadcastReceiver {
        private Bundle sendBundleFromThis;

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
