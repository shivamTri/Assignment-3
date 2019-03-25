package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.activity.StudentActivity;
import com.example.studentmanagementsystem.adapter.StudentAdaptor;
import com.example.studentmanagementsystem.comparator.SortByName;
import com.example.studentmanagementsystem.comparator.SortByRoll;
import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.model.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;
import java.util.Collections;


public class StudentListFragment extends Fragment {
    private CommunicationFragmentInterface mCommunicationFragmentInterface;
    private View view;
    private Context mContext;
    private static final String SORT_BY_ROLL_NUMBER = "roll no";
    private static final String SORT_BY_NAME = "name";
    public static final int VIEW = 0;
    public static final int EDIT = 1;
    public static final int DELETE = 2;
    public static final int REQUEST_CODE_ADD = 2;
    public static final int REQUEST_CODE_EDIT = 1;
    public final static String[] itemDialog = {"VIEW", "EDIT", "DELETE"};
    private static final String NO_DATA = "NO STUDENT ADDED";
    private ArrayList<StudentDetails> mStudent = new ArrayList<>();
    private static final String[] choice = {"name", "roll no"};

    private Button mButtonAddCuurent;
    private StudentAdaptor mAdapter;
    private TextView mTextView;
    private Intent mIntentForStudentDetailActivity;
    private int selectItem = -1;
    private int positionEditStudentData;
    private RecyclerView mRecyclerView;
    private boolean toogleLayout = false;
    private StudentDataBaseHelper mStudentDatabaseHelper;

    public StudentListFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStudentDatabaseHelper = new StudentDataBaseHelper(mContext);
        mStudent = mStudentDatabaseHelper.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_list, container, false);
// mStudent=mStudentDatabaseHelper.getData();
//For initializing values and set up Adapter
        initValues();
//(new BackProcessGetData(mContext,mContext)).execute();


//set Onclick on Add btn

        setHasOptionsMenu(true);
        mButtonAddCuurent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// do something when the corky2 is clicked
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STUDENT_DATA_List, mStudent);
                bundle.putString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY, Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD);
                mCommunicationFragmentInterface.communication(bundle);
            }
        });
        return view;
    }


    private void onClickOfAdapter(final int pos) {

// String Array for Alert Dialog Choice


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle(Constants.CHOOSE_ALERT_DIALOG_TITLE);

//setting SingleChoiceItem onClick
        mBuilder.setSingleChoiceItems(itemDialog, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//set which choice is selected
                selectItem = which;

                dialog.dismiss();

                switch (selectItem) {
                    case VIEW:
                        positionEditStudentData = pos;
                        viewSendAnotherActivity();
                        break;
                    case EDIT:
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY, Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT);
                        bundle.putSerializable(Constants.STUDENT_DATA, mStudent.get(pos));
                        positionEditStudentData = pos;
                        mCommunicationFragmentInterface.communication(bundle);
//set adapter position of item Clicked
                        break;
                    case DELETE:
                        deleteDialog(pos);
                        break;
                }
//condition for View if view choice is selected


            }
        });
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void initValues() {
        mTextView = (TextView) view.findViewById(R.id.mTextView);

        mRecyclerView = view.findViewById(R.id.student_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mButtonAddCuurent = view.findViewById(R.id.stu_add);
// setting ArrayList to Adapter of RecycleView
        mAdapter = new StudentAdaptor(mStudent);
        mRecyclerView.setAdapter(mAdapter);
        if (mStudent.size() == Constants.CHECK_ARRAYLIST_SIZE_ZERO || mStudent == null) {
            mTextView.setText(NO_DATA);
        }

// settting the onclick to Adapter
        mAdapter.setOnClickListener(new StudentAdaptor.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                onClickOfAdapter(position);

            }
        });

    }


    private void deleteDialog(final int pos) {
//setting new Dialog box for asking ok/cencel
        AlertDialog.Builder builderForAlert = new AlertDialog.Builder(mContext);
        builderForAlert.setTitle(Constants.DELETE_ALERT_DIALOG_TITLE);
        builderForAlert.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builderForAlert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//Object is Deleted when pressed ok from Student ArrayList where item is clicked
// mStudentDatabaseHelper=new StudentDatabaseHelper(mContext);
// mStudentDatabaseHelper.deleteContact(mStudent.get(pos).getmId());
                mStudentDatabaseHelper.deleteContact(mStudent.get(pos).getRollNo());
                mStudent.remove(pos);
                mAdapter.notifyDataSetChanged();

//condition for checking ArrayList size is 0 if yes then setting back textView to Vissible
                if (mStudent.size() == Constants.CHECK_ARRAYLIST_SIZE_ZERO && mTextView.getVisibility() == View.GONE || mStudent == null) {
                    mTextView.setVisibility(View.VISIBLE);
                }
                Toast.makeText(mContext, Constants.DELETE_TOAST, Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog newDialog1 = builderForAlert.create();
        newDialog1.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof CommunicationFragmentInterface) {
            mCommunicationFragmentInterface = (CommunicationFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Cummunication");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCommunicationFragmentInterface = null;
    }

    public void update(Bundle bundleFrom2Fragment) {

        switch (bundleFrom2Fragment.getString(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY)) {
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_ADD:


//Add Object in Student ArrayList at possition
                int possitionInsertStudent = 0;
//get data from another Activity

                StudentDetails sudStudent = (StudentDetails) bundleFrom2Fragment.getSerializable(Constants.STUDENT_DATA);
//mStudentDatabaseHelper.addData(sudStudent.getmId(),sudStudent.getmFirstName()+" "+sudStudent.getmLastName());
                mStudent.add(possitionInsertStudent, sudStudent);
// mStudentDatabaseHelper.addData(sudStudent.getmId(),sudStudent.getmFirstName()+" "+sudStudent.getmLastName());

//As size of ArrayList>0 setting visibility of back textView to gone
                if (mTextView.getVisibility() == View.VISIBLE) mTextView.setVisibility(View.GONE);
                mAdapter.notifyItemInserted(possitionInsertStudent);
                Toast.makeText(mContext, Constants.ADD_TOAST, Toast.LENGTH_SHORT).show();

                break;
            case Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_EDIT:

                String fName = bundleFrom2Fragment.getString(Constants.FIRST_NAME);
                String lName = bundleFrom2Fragment.getString(Constants.LAST_NAME);

//get Object from itemClicked in RecycleView
                StudentDetails suStudent = mStudent.get(positionEditStudentData);
                suStudent.setName(fName);
                mAdapter.notifyItemChanged(positionEditStudentData);
// mStudentDatabaseHelper.update_name(suStudent.getmId(),fName+" "+lName);
//mStudentDatabaseHelper.update_name(fName+" "+lName,suStudent.getmId());
                Toast.makeText(mContext, Constants.UPDATE_TOAST, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.togglebutton, menu);
        MenuItem mSpinner = menu.findItem(R.id.itemSpinner);
        mSpinner.setActionView(R.layout.spinner);
        Spinner sorting_spinner = menu.findItem(R.id.itemSpinner).getActionView().findViewById(R.id.spinnerId);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, choice);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorting_spinner.setAdapter(spinnerAdapter);
        sorting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int mPosition = position;
                switch (choice[position]) {
                    case SORT_BY_ROLL_NUMBER:
                        Toast.makeText(getActivity(), getString(R.string.sorted_by_roll), Toast.LENGTH_LONG).show();
                        Collections.sort(mStudent, new SortByRoll());
                        mAdapter.notifyDataSetChanged();
                        break;

                    case SORT_BY_NAME:
                        Toast.makeText(getActivity(), getString(R.string.sorted_by_name), Toast.LENGTH_LONG).show();
                        Collections.sort(mStudent, new SortByName());
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        return;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * This method provide the fuctionalities after Menuitem selected
     *
     * @param item
     * @return
     */

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

//This case used to Sort Student ArrayList by Name using Compatrator class


//This case used to change Linear Layout to Grid or vice versa of RecycleView
            case R.id.mySwitch:
                if(!toogleLayout) {
                    toogleLayout=true;
                    item.setIcon(R.drawable.ic_grid_on_black_24dp);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
                    Toast.makeText(mContext,Constants.GRID_LAYOUT_RECYCLER_VIEW,Toast.LENGTH_LONG).show();
                }else {
                    toogleLayout=false;
                    item.setIcon(R.drawable.ic_grid_off_black_24dp);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    Toast.makeText(mContext,Constants.LINEAR_LAYOUT_RECYCLER_VIEW,Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void viewSendAnotherActivity() {
        Intent intent = new Intent(mContext, StudentActivity.class);
        intent.putExtra(Constants.STUDENT_DATA, mStudent.get(positionEditStudentData));
        intent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY, Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_VIEW);
        mContext.startActivity(intent);
    }
}


