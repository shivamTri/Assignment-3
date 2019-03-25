package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundTaskAsync;
import com.example.studentmanagementsystem.model.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;

import java.util.ArrayList;



public class StudentAddUpdateFragment extends Fragment {
    private View view;
    private Context mContext;
    public static final int REQUEST_CODE_ADD=1;
    public static final int REQUEST_CODE_EDIT=0;
    public static final int ASYNC_TASK=0;
    public static final int SERVICE=1;
    public static final int INTENT_SERVICE=2;
    public final static String[] ITEM_DAILOG={"AsyncTask" , "Service" , "Intent Service"};
    private EditText et_name;
    private EditText et_rollNum;
    private Button mButtonAdd;
    private int selectButtonOperation=1;
    private String typeAction;
    private Bundle bundle;
    private StudentDetails editStudentDetail;
    private boolean errorHandling;
    private int select;
    private StudentDataBaseHelper studentDataBaseHelper;
    private CommunicationFragmentInterface mListener;
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
        et_name =view.findViewById(R.id.et_name);
        et_rollNum =view.findViewById(R.id.et_rollNum);
        mButtonAdd=view.findViewById(R.id.addbutton);
    }


    /**
     * This function provide functionalities of add button
     *get data from edittext then validate
     */
    private void addButtonOnClick(){

        String fName = et_name.getText().toString().trim();
        String sRollNo = et_rollNum.getText().toString().trim();

        if (!ValidUtil.validateName(fName)) {
            et_name.setError(getString(R.string.valid_name));
            errorHandling = false;
        }

        if (!ValidUtil.validateRollNumber(sRollNo)) {
            et_rollNum.setError(getString(R.string.valid_rollNumber));
            errorHandling = false;
        }
        else if (ValidUtil.isCheckValidId(studentList,sRollNo)) {
            et_rollNum.setError(getString(R.string.same_roll_error));
            errorHandling = false;
        }
        if (errorHandling) {

            StudentDetails student = new StudentDetails(fName.toUpperCase(),sRollNo);
            bundle.putSerializable(Constants.STUDENT_DATA,student);
            generateAlertDialog(sRollNo,fName,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);

        }
    }

    /**
     * This function provide functionalities for Updation
     *get data from edittext then validate
     */
    private void editButtonOnClick(){
        String fName = et_name.getText().toString().trim();

        if (!ValidUtil.validateName(fName)) {
            et_name.setError(getString(R.string.invalid_name_message));
            errorHandling = false;
        }

        if (errorHandling) {
            bundle.putString(Constants.NAME,fName);
            generateAlertDialog(editStudentDetail.getRollNo(),fName,Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT);
        }
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
            mBuilder.setTitle(R.string.dialog_title_update);
        else
            mBuilder.setTitle(R.string.dialog_title_add);

        mBuilder.setSingleChoiceItems(ITEM_DAILOG, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                select=which;
                dialog.dismiss();

                switch (select){
                    case ASYNC_TASK:
// (new BackSetUpdateData(mContext)).execute(typeOperation,rollNo,fullName);
                        (new BackgroundTaskAsync(mContext)).execute(typeOperation,rollNo,fullName);
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
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
        if (context instanceof CommunicationFragmentInterface) {
            mListener = (CommunicationFragmentInterface) context;
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

    /**
     * this function is being called  in mainActivity for updation by interface method.
     * @param bundleNew
     */
    public void update(Bundle bundleNew){
        bundle=bundleNew;
        typeAction=bundle.getString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY);
        switch (typeAction){
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD:
                selectButtonOperation=REQUEST_CODE_ADD;
                studentList=(ArrayList<StudentDetails>) bundle.getSerializable(Constants.STUDENT_DATA_List);
                et_rollNum.setEnabled(true);

                break;
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT:
                selectButtonOperation=REQUEST_CODE_EDIT;
                mButtonAdd.setText(Constants.BTN_CHANGE_TEXT_UPDATE);
                editStudentDetail=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                et_name.setText(editStudentDetail.getName().toUpperCase());
                et_rollNum.setText(editStudentDetail.getRollNo().toUpperCase());
                et_rollNum.setEnabled(false);
                //mButtonAdd.setVisibility(View.VISIBLE);
                break;
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_VIEW:
                editStudentDetail=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                et_name.setText(editStudentDetail.getName().toUpperCase());
                et_rollNum.setText(editStudentDetail.getRollNo().toUpperCase());
                et_rollNum.setEnabled(false);
                et_name.setEnabled(false);
                mButtonAdd.setVisibility(View.GONE);
                break;
        }
    }
}

