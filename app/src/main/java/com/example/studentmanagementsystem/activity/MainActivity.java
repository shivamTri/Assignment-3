package com.example.studentmanagementsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.comparator.SortByName;
import com.example.studentmanagementsystem.adapter.StudentAdaptor;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.service.BackgroundTask;
import com.example.studentmanagementsystem.model.GenerateDialog;
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.comparator.SortByRoll;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements BackgroundTask.CallBack {
    private RecyclerView student_rv;
    private ArrayList<StudentDetails> studentArrayList=new ArrayList<>();
    private Button mButton;
    private TextView mText;
    private StudentAdaptor mAdapter;
    private String select;
    private static final String [] choice={"name","roll no"};
    private int mPosition;
    private boolean mToggle =true;
    private int mEditPosition;
    private static final String SORT_BY_ROLL_NUMBER ="roll no";
    private static final String [] listItems={"View","Edit","Delete"};
    private  static final String SORTED_BY_ROLL="Sorted by roll number";
    private int mDeletePosition;
    private GenerateDialog generateDialog;
    private StudentBroadcastReceiver mStudentBroadcastReceiver;

   // private Context mContext;
    private static final String[] ITEM_DAILOG={"Async task","Service","Intent service"};

    // private  static final String SORTED_BY_NAME="Sorted by name";
    private static final String SORT_BY_NAME="name";
    private StudentDataBaseHelper studentDataBaseHelper;


    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStudentBroadcastReceiver=new StudentBroadcastReceiver();
        generateDialog=new GenerateDialog(this);
        setTitle("StudentActivity");

        studentDataBaseHelper=new StudentDataBaseHelper(this);
        studentArrayList=studentDataBaseHelper.getData();
        init();
        if(studentArrayList.size()==0){

            mText.setText(getString(R.string.no_data));
        }

        mAdapter.setOnClickListener(new StudentAdaptor.OnItemClickListener() {
            @Override
            public void onItemCLick(final int position) {

                final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle(Constants.ALERT_TITLE);
                alertBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        select=listItems[which];
                        Toast.makeText(MainActivity.this,select,Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        mEditPosition =position;

                        switch (select){
                            case Constants.DETAIL_VIEW:
                                Intent viewIntent=new Intent(MainActivity.this, StudentActivity.class);
                                viewIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                                viewIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,Constants.DETAIL_VIEW);
                                startActivity(viewIntent);
                                break;

                            case Constants.DETAIL_EDIT:
                                Intent editIntent=new Intent(MainActivity.this, StudentActivity.class);
                                editIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                                editIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,Constants.DETAIL_EDIT);
                                startActivityForResult(editIntent,2);
                                break;
                            case Constants.DETAIL_DELETE:
                                generateDialog.generateAlertDialog(studentArrayList.get(position).getRollNo(),
                                        studentArrayList.get(position).getName(),
                                        Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY_DELETE);


                        }

                    }

                });

                alertBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog mDialog=alertBuilder.create();
                mDialog.show();

            }


        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nextActivity=new Intent(MainActivity.this, StudentActivity.class);
                nextActivity.putExtra(Constants.STUDENT_DATA,studentArrayList);
                nextActivity.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,"Add");
                startActivityForResult(nextActivity,1);
            }
        });

    }

    /**
     *
     * @param requestCode with request code its differentiating which startActivity is this.
     * @param resultCode  which result is coming from the StudentActivity
     * @param data intent type data from StudentActivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 ) {
            if (resultCode == RESULT_OK) {
                int position = 0;
                StudentDetails studentDetails = (StudentDetails) data.getSerializableExtra(Constants.STUDENT_DATA);
                studentArrayList.add(position, studentDetails);
                //backgroundTask.execute("add_data");
            //  studentDataBaseHelper.addData(studentDetails.getRollNo(),studentDetails.getName());

                if(choice[mPosition].equals(getString(R.string.roll_no))){

                    Collections.sort(studentArrayList,new SortByRoll());
                    mAdapter.notifyDataSetChanged();
                }
                else if(choice[mPosition].equals(getString(R.string.name))){
                    Collections.sort(studentArrayList,(new SortByName()));
                    mAdapter.notifyDataSetChanged();
                }
                Toast.makeText(this, getString(R.string.toast_student_added), Toast.LENGTH_SHORT).show();
                if (mText.getVisibility() == View.VISIBLE) {
                    mText.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyItemInserted(position);
            }

        }
        else if(requestCode==2 ){
            if( resultCode==RESULT_OK) {
                /**
                 * here taking data from StudentActivity and setting the data name and roll number on the selected position in the list.
                 */
                Toast.makeText(MainActivity.this,getString(R.string.student_changed),Toast.LENGTH_LONG).show();
                if (data == null) {
                    throw new AssertionError();
                }
                String name=data.getStringExtra(Constants.POSITION_STUDENT_NAME);
                String id=data.getStringExtra(Constants.POSITION_STUDENT_ROLL);
               // backgroundTask.execute("update_data");
                //studentDataBaseHelper.update_name(name,id);
                StudentDetails std=studentArrayList.get(mEditPosition);
                std.setName(name);
                std.setId(id);
                mAdapter.notifyItemChanged(mEditPosition);

            }
        }
    }

    /**
     *this method is setting spinner on toolbar and giving access to spinner to perform action
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.togglebutton, menu);
        MenuItem mSpinner = menu.findItem(R.id.itemSpinner);
        mSpinner.setActionView(R.layout.spinner);
        Spinner sorting_spinner = menu.findItem(R.id.itemSpinner).getActionView().findViewById(R.id.spinnerId);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, choice);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorting_spinner.setAdapter(spinnerAdapter);
        sorting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPosition=position ;
                    switch (choice[position]) {
                        case SORT_BY_ROLL_NUMBER:
                            Toast.makeText(MainActivity.this, SORTED_BY_ROLL, Toast.LENGTH_LONG).show();
                            Collections.sort(studentArrayList, new SortByRoll());
                            mAdapter.notifyDataSetChanged();
                            break;

                        case SORT_BY_NAME:
                            Toast.makeText(MainActivity.this, SORT_BY_NAME, Toast.LENGTH_LONG).show();
                            Collections.sort(studentArrayList, new SortByName());
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
        return true;
    }

    /**
     * this method is giving access to change the layout of recyclerview from linearLayout to gridLayout.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle){
            mToggle =false;
            item.setIcon(R.drawable.ic_grid_on_black_24dp);
            student_rv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        }
        else {
             mToggle =true;
             item.setIcon(R.drawable.ic_grid_off_black_24dp);
             student_rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }
        return super.onOptionsItemSelected(item);
    }
    private void init()
    {

        mButton =findViewById(R.id.addNext);
        mText=findViewById(R.id.nodata);
        student_rv = findViewById(R.id.studentlist_tv);
        student_rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new StudentAdaptor(studentArrayList);
        student_rv.setAdapter(mAdapter);


    }

    @Override
    public void sendBack(String str) {
        studentArrayList.remove(mEditPosition);
        mAdapter.notifyItemChanged(mEditPosition);
        if(studentArrayList.size()==0){
            mText.setVisibility(View.VISIBLE);
        }

    }

   public class StudentBroadcastReceiver extends BroadcastReceiver {
       //private Bundle sendBundleFromThis;

       @Override
       public void onReceive(Context context, Intent intent) {

           Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
           vibrator.vibrate(Constants.VIBRATE_MILI_SECOND);
           studentArrayList.remove(mEditPosition);
           mAdapter.notifyItemRemoved(mEditPosition);
           if(studentArrayList.size()==0){
               mText.setVisibility(View.VISIBLE);
           }

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
        IntentFilter intentFilter = new IntentFilter(Constants.FILTER_KEY_DELETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStudentBroadcastReceiver,intentFilter);
    }

}







