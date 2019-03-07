package com.example.studentmanagementsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;

public class Student extends AppCompatActivity {
   private EditText sName,sRollNo;
   private Button btn_addStudent;
   private StudentDetails mStudentDetails;
   private ArrayList<StudentDetails> studentDetailsArrayList;
   private String selectOnClick;
   private String  position;
   private int buttonWork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        sName=findViewById(R.id.name);
        sRollNo=findViewById(R.id.roll);
        btn_addStudent =findViewById(R.id.addbutton);
        /**
         * selectOnClick is taking the data from intent sent by student Activity.
         */
        selectOnClick=getIntent().getStringExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        /**
         * used if else if to check which action to be performed.
         */

        if(selectOnClick.equals("View")){
            mStudentDetails =(StudentDetails) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
            sName.setText(mStudentDetails.getName().toUpperCase());
            sRollNo.setText(mStudentDetails.getRollNo().toUpperCase());
            sName.setEnabled(false);
            sRollNo.setEnabled(false);
            btn_addStudent.setVisibility(View.GONE);
        }else if(selectOnClick.equals("Add")){
            buttonWork=1;
            studentDetailsArrayList=(ArrayList<StudentDetails>) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
        }else if(selectOnClick.equals("Edit")){
            /**
             * here mStudentDetails is taking data from MainActivity to edit the details of student on the same position.
             */
            buttonWork=2;
            mStudentDetails =(StudentDetails) getIntent().getSerializableExtra(Constants.STUDENT_DATA);
            sName.setText(mStudentDetails.getName().toUpperCase());
            sRollNo.setText(mStudentDetails.getRollNo().toUpperCase());
            position=getIntent().getStringExtra(Constants.POSITION_STUDENT_DATA);
        }

        btn_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonWork==1) {
                    boolean error = true;
                    String name;
                    String roll;
                    name = sName.getText().toString();
                    roll = sRollNo.getText().toString();
                    if (name.equals(""))
                    {
                        sName.setError("enter name");
                        error = false;
                    }
                    else if(!ValidUtil.validateName(name)){
                        sName.setError("enter valid name");
                        error = false;


                    }
                    else if (roll.equals("")) {
                        sRollNo.setError("Enter valid roll number");
                        error = false;
                    }
                    else if(!ValidUtil.validateRollNumber(roll)){
                        sRollNo.setError("roll number is not valid");
                        error = false;
                    }
                    else if (isCheckValidId(roll)) {
                        sRollNo.setError("Roll numbers should be different");
                        error = false;
                    }

                    else if (error) {
                        StudentDetails studentDetails = new StudentDetails(name, roll);
                        Intent intent = new Intent();
                        intent.putExtra(Constants.STUDENT_DATA, studentDetails);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }else if(buttonWork==2){
                    boolean error = true;
                    String name;
                    String roll;
                    name = sName.getText().toString();
                    roll = sRollNo.getText().toString();
                    if (name.equals("")) {
                        sName.setError("Enter Valid name");
                        error = false;
                    } if (roll.equals("")) {
                        sRollNo.setError("Enter valid roll number");
                        error = false;
                    }
                    if (error) {
                       // StudentDetails studentDetails = new StudentDetails(name, roll);
                        Intent eintent = new Intent();
                        eintent.putExtra(Constants.POSITION_STUDENT_NAME,name);
                        eintent.putExtra(Constants.POSITION_STUDENT_DATA,position);
                        eintent.putExtra(Constants.POSITION_STUDENT_ROLL,roll);
                        setResult(RESULT_OK, eintent);
                        finish();
                    }
                }

            }
        });

    }

    /**
     * @param roll is roll number of the student which is going to be matched if there is any existing roll number or not.
     * @return
     */
    private boolean isCheckValidId(final String roll){
        for(StudentDetails rollNumber:studentDetailsArrayList){
            if(rollNumber.getRollNo().equals(roll)){
                return true;
            }
        }
        return false;

    }

}
