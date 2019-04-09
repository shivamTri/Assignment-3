package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentmanagementsystem.BroadcasteReciever.StudentBroadcastReciever;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.Interface.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.dialog.GenerateDialog;
import com.example.studentmanagementsystem.service.BackgroundTaskAsync;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.util.ValidUtil;

import java.util.ArrayList;



public class StudentAddUpdateFragment extends Fragment implements BackgroundTaskAsync.SendCallBack, StudentBroadcastReciever.SendBroadCastMessage {
    private CommunicationFragmentInterface communicationFragmentInterface;
    private View view;
    private ArrayList<StudentDetails> studentDetailsArrayList;
    private EditText et_name;
    private EditText et_rollNo;
    private Button btn_add_student;
    private int buttonOperation=-1;
    private boolean errorHandling;
    private Bundle bundle;
    private Context mContext;
    private GenerateDialog generateDialog;
    private StudentBroadcastReciever studentBroadcastReciever;


    private StudentDetails studentDetails;


    public StudentAddUpdateFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static StudentAddUpdateFragment newInstance(String param1, String param2) {
        StudentAddUpdateFragment fragment = new StudentAddUpdateFragment();
        //Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("onRecieve", "onCreate: ");

        super.onCreate(savedInstanceState);



    }

    /**
     * in this method layout is inflated and views are created .
     * button onclick is being performed here.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onRecieve", "onCreateView: ");
        generateDialog=new GenerateDialog(mContext, (BackgroundTaskAsync.SendCallBack) this);
        studentBroadcastReciever=new StudentBroadcastReciever();
        studentBroadcastReciever.setSendBroadCastMessageUpdate(this);

        view = inflater.inflate(R.layout.fragment_student_add_update, container, false);
        init();
        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorHandling=true;
                switch(buttonOperation){
                    case Constants.Edit:
                        editStudentData();
                        break;
                    case Constants.Add:
                        addStudentData();
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

    /**
     * in this method validation of name and roll no is being done.
     * data is being passed to services.
     */

    private void addStudentData() {
        String sName = et_name.getText().toString().trim();
        String sRollNo = et_rollNo.getText().toString().trim();

        if (!ValidUtil.validateName(sName)) {
            et_name.setError(getString(R.string.invalid_name));
            errorHandling = false;
        }
        if (!ValidUtil.validateRollNumber(sRollNo)) {
            et_rollNo.setError(getString(R.string.invalid_roll_no));
            errorHandling = false;
        }
        if (ValidUtil.isCheckValidId(studentDetailsArrayList,sRollNo)) {
            et_rollNo.setError(getString(R.string.same_rollNum_error));
            errorHandling = false;
        }
        if (errorHandling) {

            StudentDetails student = new StudentDetails(sName.toUpperCase(),sRollNo);
            bundle.putSerializable(Constants.STUDENT_DATA,student);
            generateDialog.generateAlertDialog(sRollNo,sName,Constants.ACTION_TYPE_ADD);
        }

    }


    /**
     * in this method data of student is being altered in database by using services.
     */
    private void editStudentData() {
        String fName = et_name.getText().toString().trim();

        if (!ValidUtil.validateName(fName)) {
            et_name.setError(getString(R.string.invalid_name));
            errorHandling = false;
        }

        if (errorHandling) {
            bundle.putString(Constants.NAME,fName);
            generateDialog.generateAlertDialog(studentDetails.getRollNo(),fName,Constants.ACTION_TYPE_EDIT);

        }

    }

    // TODO: Rename method, update argument and hook method into UI event

    /**
     * this method is called when fragment is attached.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        Log.d("onRecieve", "onAttach: ");

        super.onAttach(context);
        mContext=context;
        if (context instanceof CommunicationFragmentInterface) {
            communicationFragmentInterface = (CommunicationFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    /**
     * this method is called when fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        communicationFragmentInterface = null;

    }

    /**
     * this method is getting the data from fragment and checking which action to be performed and doing the same.
     * @param bundle
     */
    public void addStudent(Bundle bundle) {
        String type=bundle.getString(Constants.ACTION_TYPE);
        this.bundle=bundle;
        switch (type){
            case Constants.ACTION_TYPE_ADD:
                buttonOperation=Constants.Add;
                studentDetailsArrayList=(ArrayList<StudentDetails>) bundle.getSerializable(Constants.STUDENT_LIST);
                et_rollNo.setEnabled(true);
                break;
            case Constants.ACTION_TYPE_EDIT:
                buttonOperation=Constants.Edit;
                studentDetails=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                et_name.setText(studentDetails.getName().toUpperCase());
                et_rollNo.setText(studentDetails.getRollNo().toUpperCase());
                et_rollNo.setEnabled(false);
                break;
            case Constants.ACTION_TYPE_VIEW:
                studentDetails=(StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                et_name.setText(studentDetails.getName().toUpperCase());
                et_rollNo.setText(studentDetails.getRollNo().toUpperCase());
                et_name.setEnabled(false);
                et_name.setEnabled(false);
                btn_add_student.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    /**
     * all views are being initialized here.
     */
    private void init(){
        btn_add_student=view.findViewById(R.id.btn_add_student);
        et_name=view.findViewById(R.id.et_name);
        et_rollNo =view.findViewById(R.id.et_rollNum);

    }

    /**
     * getting callback from async for delete opearation.
     * @param s
     */

    @Override
    public void sendBack(String s) {
        if(!s.equals(Constants.ACTION_TYPE_DELETE)) {
            Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
            clearEditText(et_name,et_rollNo);
            communicationFragmentInterface.communication(bundle);
        }
    }
    /**
     * Broadcast reciever is being registered in this method.
     */

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(studentBroadcastReciever,intentFilter);
        Log.d("onRecieve", "onResume: ");
    }

    /**
     * Broadcast reciever is un registered .
     */
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(studentBroadcastReciever);
        //communicationFragmentInterface.communication(bundle);

    }

    /**
     * getting callback from broadcast reciever.
     * @param str
     */
    @Override
    public void sendCallMessage(String str) {
        Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
        clearEditText(et_name, et_rollNo);

        communicationFragmentInterface.communication(bundle);
    }

    /**
     * for clearing the edit texts after the task is done in fragments.
     * @param et_name
     * @param et_roll_no
     */
    private void clearEditText(EditText et_name,EditText et_roll_no){
        et_name.getText().clear();
        et_roll_no.getText().clear();
    }


}

