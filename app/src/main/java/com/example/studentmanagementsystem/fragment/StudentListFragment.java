package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.studentmanagementsystem.BroadcasteReciever.StudentBroadcastReciever;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.activity.StudentViewActivity;
import com.example.studentmanagementsystem.adapter.StudentAdapter;
import com.example.studentmanagementsystem.comparator.SortByName;
import com.example.studentmanagementsystem.comparator.SortByRoll;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.Interface.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.dialog.GenerateDialog;
import com.example.studentmanagementsystem.service.BackgroundTaskAsync;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;
import java.util.Collections;


public class StudentListFragment extends Fragment implements StudentBroadcastReciever.SendBroadCastMessage, BackgroundTaskAsync.SendCallBack {
    private CommunicationFragmentInterface communicationFragmentInterface;
    private RecyclerView rv_student;
    private TextView tv_no_data;
    private View view;
    private Context mContext;
    private Button btn_add_student;
    private StudentAdapter studentAdapter;
    private ArrayList<StudentDetails> studentList=new ArrayList<>();
    private GenerateDialog generateDialog;
    private int selectItem=-1;

    int studentPosition;
    private boolean layoutSwitch=false;
    private StudentDataBaseHelper studentDataBase;
    private StudentBroadcastReciever mStudentBroadcastReciever;


    // TODO: Rename and change types of parameters


    public StudentListFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentDataBase=new StudentDataBaseHelper(mContext);
        generateDialog=new GenerateDialog(mContext, this);
        mStudentBroadcastReciever=new StudentBroadcastReciever();
        mStudentBroadcastReciever.setSendBroadCastMessageDelete( this);
        studentList=studentDataBase.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_student_list, container, false);
        init();
        setHasOptionsMenu(true);
        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STUDENT_LIST, studentList);
                bundle.putString(Constants.ACTION_TYPE,Constants.ACTION_TYPE_ADD);
                communicationFragmentInterface.communication(bundle);
            }
        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext =context;
        if (context instanceof CommunicationFragmentInterface) {
            communicationFragmentInterface= (CommunicationFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        communicationFragmentInterface = null;
    }

    private void init() {

        tv_no_data = view.findViewById(R.id.tv_no_data);
        rv_student = view.findViewById(R.id.rv_student);
        rv_student.setLayoutManager(new LinearLayoutManager(mContext));
        btn_add_student = view.findViewById(R.id.btn_add);
        studentAdapter = new StudentAdapter(studentList);
        rv_student.setAdapter(studentAdapter);
        if (studentList.size() == Constants.ARRAYLIST_SIZE_ZERO) {

            tv_no_data.setText(getString(R.string.no_data));
        }

        studentAdapter.setOnClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                adapterClick(position);
            }
        });

    }

    private void adapterClick(final int position) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setSingleChoiceItems(Constants.itemDialog, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectItem = which;
                studentPosition=position;

                dialog.dismiss();

                switch (selectItem) {
                    case Constants.VIEW:
                        viewData();
                        break;
                    case Constants.EDIT:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACTION_TYPE,Constants.ACTION_TYPE_EDIT);
                        bundle.putSerializable(Constants.STUDENT_DATA, studentList.get(position));
                        communicationFragmentInterface.communication(bundle);
                        break;
                    case Constants.DELETE:
                        generateDialog.generateAlertDialog(studentList.get(position).getRollNo(),studentList.get(position).getName(),Constants.ACTION_TYPE_DELETE);
                        break;
                }


            }
        });
        mBuilder.setNeutralButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.togglebutton, menu);
        MenuItem mSpinner = menu.findItem(R.id.itemSpinner);
        mSpinner.setActionView(R.layout.spinner);
        Spinner sorting_spinner = menu.findItem(R.id.itemSpinner).getActionView().findViewById(R.id.spinnerId);
        spinnerAdapter(sorting_spinner);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mySwitch:
                if(!layoutSwitch) {
                    layoutSwitch =true;
                    item.setIcon(R.drawable.ic_grid_on_black_24dp);
                    rv_student.setLayoutManager(new GridLayoutManager(mContext,Constants.GRIDLAYOUT_SPAN));
                    Toast.makeText(mContext,getString(R.string.grid),Toast.LENGTH_LONG).show();
                }
                else {
                    layoutSwitch =false;
                    item.setIcon(R.drawable.ic_grid_off_black_24dp);
                    rv_student.setLayoutManager(new LinearLayoutManager(mContext));
                    Toast.makeText(mContext,getString(R.string.linear),Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void spinnerAdapter(Spinner spinner){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Constants.choice);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mPosition = position;
                switch (Constants.choice[position]) {
                    case Constants.ROLL_NO:
                        Toast.makeText(getActivity(), getString(R.string.sort_roll), Toast.LENGTH_LONG).show();
                        Collections.sort(studentList, new SortByRoll());
                        studentAdapter.notifyDataSetChanged();
                        break;

                    case Constants.NAME:
                        Toast.makeText(getActivity(), getString(R.string.sort_name), Toast.LENGTH_LONG).show();
                        Collections.sort(studentList, new SortByName());
                        studentAdapter.notifyDataSetChanged();
                        break;

                    default:
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void update(Bundle bundle) {
        switch (bundle.getString(Constants.ACTION_TYPE)) {
            case Constants.ACTION_TYPE_ADD:
                int studentInsertPosition = 0;
                StudentDetails sudStudent = (StudentDetails) bundle.getSerializable(Constants.STUDENT_DATA);
                studentList.add(studentInsertPosition, sudStudent);
                if (tv_no_data.getVisibility() == View.VISIBLE) tv_no_data.setVisibility(View.GONE);
                studentAdapter.notifyItemInserted(studentInsertPosition);
                Toast.makeText(mContext, Constants.ADD_TOAST, Toast.LENGTH_SHORT).show();
                break;
            case Constants.ACTION_TYPE_EDIT:
                String sName = bundle.getString(Constants.NAME);
                StudentDetails suStudent = studentList.get(studentPosition);
                suStudent.setName(sName);
                studentAdapter.notifyItemChanged(studentPosition);
                Toast.makeText(mContext, Constants.UPDATE_TOAST, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_KEY_DELETE);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mStudentBroadcastReciever,intentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mStudentBroadcastReciever);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void sendCallMessage(String str) {

        Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
        studentList.remove(studentPosition);
        studentAdapter.notifyItemRemoved(studentPosition);
        listCheclist(studentList);
    }

    @Override
    public void sendBack(String str) {
        if(str.equals(Constants.ACTION_TYPE_DELETE)){
            Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
            studentList.remove(studentPosition);
            studentAdapter.notifyItemRemoved(studentPosition);
            listCheclist(studentList);
        }
    }
    private void viewData(){
        Intent intent = new Intent(mContext, StudentViewActivity.class);
        intent.putExtra(Constants.STUDENT_DATA, studentList.get(studentPosition));
        intent.putExtra(Constants.ACTION_TYPE, Constants.ACTION_TYPE_VIEW);
        mContext.startActivity(intent);
    }
    private void listCheclist(ArrayList<StudentDetails> studentDetailsArrayList){
        if(studentDetailsArrayList.size()==Constants.ARRAYLIST_SIZE_ZERO){
            tv_no_data.setVisibility(View.VISIBLE);
        }

    }
}


