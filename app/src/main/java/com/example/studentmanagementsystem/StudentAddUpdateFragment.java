package com.example.studentmanagementsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanagementsystem.activity.MainActivity;
import com.example.studentmanagementsystem.activity.StudentActivity;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundTask;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;

import java.util.ArrayList;



public class StudentAddUpdateFragment extends Fragment {
    private View view;
    private Context mContext;

    public static final int REQUEST_CODE_ADD=2;
    public static final int REQUEST_CODE_EDIT=1;
    public static final int ASYNC_TASK=0;
    public static final int SERVICE=1;
    public static final int INTENT_SERVICE=2;
    public final static String[] ITEM_DAILOG={"AsyncTask" , "Service" , "Intent Service"};
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextId;
    private Button mButtonAdd;
    private int selectButtonOperation=2;
    private String typeAction;
    private Bundle bundle;
    private StudentDetails editStudentDetail;
    private boolean errorHandling;
    private int select;
    private StudentDataBaseHelper studentDataBaseHelper;
    private CommunicationFragment mListener;
    private ArrayList<StudentDetails> studentList=new ArrayList<>();

    public StudentAddUpdateFragment() {
// Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_student_add_update, container, false);
        studentDataBaseHelper=new StudentDataBaseHelper(mContext);
        bundle=new Bundle();
        bundle.putString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);

        initValues();

//set click listener to button
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorHandling=true;
                switch (selectButtonOperation){
                    case REQUEST_CODE_EDIT:
                        editButtonOnClick();
                        break;
                    case REQUEST_CODE_ADD:
                        addButtonOnClick();
                        break;
                }
            }
        });
        return view;
    }

    private void initValues(){
        mEditTextFirstName=view.findViewById(R.id.name);
        mEditTextId=view.findViewById(R.id.roll);
        mButtonAdd=view.findViewById(R.id.add);
    }


    /**
     * This function provide functionalities of add button
     *get data from edittext then validate
     */
    private void addButtonOnClick(){

        String fName = mEditTextFirstName.getText().toString().trim();
        String lName = mEditTextLastName.getText().toString().trim();
        String sRollNo = mEditTextId.getText().toString().trim();

// validation for first name check used set error to edit text
        if (!ValidUtil.validateName(fName)) {
            mEditTextFirstName.setError("valid name");
            errorHandling = false;
        }
// validation for last name check used set error to edit text

// validation for Roll No check used set error to edit text
        if (!ValidUtil.validateRollNumber(sRollNo)) {
            mEditTextId.setError("valid roll");
            errorHandling = false;
        }
//check duplicte Roll No
        else if (ValidUtil.isCheckValidId(studentList,sRollNo)) {
            mEditTextId.setError("different");
            errorHandling = false;
        }
//check if error is present or not
        if (errorHandling) {

            StudentDetails student = new StudentDetails(fName.toUpperCase(),sRollNo);
            bundle.putSerializable(Constants.STUDENT_DATA,student);
            generateAlertDialog(sRollNo,fName+" "+lName,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);

        }
    }

    /**
     * This function provide functionalities for Updation
     *get data from edittext then validate
     */
    private void editButtonOnClick(){
        String fName = mEditTextFirstName.getText().toString().trim();
        String lName = mEditTextLastName.getText().toString().trim();

// validation for first name check used set error to edit text
        if (!ValidUtil.validateName(fName)) {
            mEditTextFirstName.setError("valid name");
            errorHandling = false;
        }
// validation for last name check used set error to edit text

//check if error is present or not
        if (errorHandling) {
            bundle.putString(Constants.FIRST_NAME,fName);
            bundle.putString(Constants.LAST_NAME,lName);
            generateAlertDialog(editStudentDetail.getRollNo(),fName+" "+lName,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT);
        }
    }



    private void setTextOfEditText(StudentDetails student){
        mEditTextFirstName.setText(student.getName().toUpperCase());
        mEditTextId.setText(student.getRollNo().toUpperCase());
    }

    /**
     * This method used to generate Dialog Box having 3 choose for Background thread
     *
     * @param rollNo of String type
     * @param fullName of String type
     * @param typeOperation of String type used to check operation
     */

    private void generateAlertDialog(final String rollNo, final String fullName, final String typeOperation){

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(mContext);
        if(selectButtonOperation==REQUEST_CODE_EDIT)
            mBuilder.setTitle("update");
        else
            mBuilder.setTitle("add");

//setting SingleChoiceItem onClick
        mBuilder.setSingleChoiceItems(ITEM_DAILOG, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//set which choice is selected
                select=which;
                dialog.dismiss();

                switch (select){
                    case ASYNC_TASK:
// (new BackSetUpdateData(mContext)).execute(typeOperation,rollNo,fullName);
                        (new BackgroundTask(mContext)).execute(typeOperation,rollNo,fullName);
                        break;
                    case SERVICE:
                        Intent service=new Intent(mContext, BackgroundService.class);
                        startServiceWork(service,rollNo,fullName,typeOperation);
                        break;
                    case INTENT_SERVICE:
                        Intent intentForService=new Intent(mContext, BackgroundIntentService.class);
                        startServiceWork(intentForService,rollNo,fullName,typeOperation);
                        break;
                }
                mListener.communication(bundle);

            }
        });
        mBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }
    private void startServiceWork(Intent service,final String rollNo, final String fullName, final String typeOperation){
        service.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,typeOperation);
        service.putExtra(Constants.ROLL_NO,rollNo);
        service.putExtra(Constants.STUDENT_FULL_NAME,fullName);
        mContext.startService(service);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        if (context instanceof CommunicationFragment) {
            mListener = (CommunicationFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    public void update(Bundle bundleNew){
        bundle=bundleNew;
        typeAction=bundle.getString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        switch (typeAction){
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD:
                selectButtonOperation=REQUEST_CODE_ADD;
                studentList=(ArrayList<StudentDetails>) bundle.getSerializable(Constants.STUDENT_DATA_List);
                mButtonAdd.setText("add");
                mEditTextId.setEnabled(true);

                break;
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT:
                selectButtonOperation=REQUEST_CODE_EDIT;
                mButtonAdd.setText(Constants.BTN_CHANGE_TEXT_UPDATE);
                editStudentDetail=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                mEditTextFirstName.setText(editStudentDetail.getName().toUpperCase());
                mEditTextId.setText(editStudentDetail.getRollNo().toUpperCase());
                mEditTextId.setEnabled(false);
//mButtonAdd.setVisibility(View.VISIBLE);
                break;
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_VIEW:
                editStudentDetail=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                mEditTextFirstName.setText(editStudentDetail.getName().toUpperCase());
                mEditTextId.setText(editStudentDetail.getRollNo().toUpperCase());
                mEditTextId.setEnabled(false);
                mEditTextFirstName.setEnabled(false);
                mEditTextLastName.setEnabled(false);
                mButtonAdd.setVisibility(View.GONE);
                break;
        }
    }
}

