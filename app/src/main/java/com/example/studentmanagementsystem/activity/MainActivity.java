package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.Nullable;
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
import com.example.studentmanagementsystem.model.StudentDetails;
import com.example.studentmanagementsystem.comparator.SortByRoll;
import com.example.studentmanagementsystem.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<StudentDetails> studentArrayList=new ArrayList<>();
    private Button mButton;
    private TextView mtext;
    private StudentAdaptor mAdapter;
    private String select;
    private String [] choice={"name","roll no"};
    private int mPostion;
    private boolean mToggle =true;
    @Override

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Student");



        recyclerView= findViewById(R.id.studentlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new StudentAdaptor(studentArrayList);

        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new StudentAdaptor.OnItemClickListener() {
            @Override
            public void onItemCLick(final int position) {

                final String [] listItems={"View","Edit","Delete"};
                final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Choose action");
                alertBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        select=listItems[which];
                        Toast.makeText(MainActivity.this,select,Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                        if(select.equals("View")){
                            Intent viewIntent=new Intent(MainActivity.this, Student.class);
                            viewIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                            viewIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,"View");
                            startActivity(viewIntent);
                        }

                        else if(select.equals("Edit")){
                            Intent editIntent=new Intent(MainActivity.this,Student.class);
                            editIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                            editIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,"Edit");
                            /**
                             * here sendiing the data from this activity to another with the use of putExtra .
                             */
                            editIntent.putExtra(Constants.POSITION_STUDENT_DATA,Integer.toString(position));
                            startActivityForResult(editIntent,2);
                        }

                        else if(select.equals("Delete")){
                            AlertDialog.Builder deleteDilog=new AlertDialog.Builder(MainActivity.this);
                            deleteDilog.setTitle("Do you really want to delete the item..");
                            deleteDilog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                            deleteDilog.setPositiveButton("Detele", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    studentArrayList.remove(position);
                                    Toast.makeText(MainActivity.this,"Student deleted",Toast.LENGTH_SHORT).show();
                                    mAdapter.notifyDataSetChanged();
                                    if(studentArrayList.size()==0){
                                        mtext.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                            AlertDialog alertDelete=deleteDilog.create();
                            alertDelete.show();
                        }

                    }

                });
                alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog mDialog=alertBuilder.create();
                mDialog.show();


            }


        });

        mtext=findViewById(R.id.nodata);
        if(studentArrayList.size()==0){
            mtext.setText(Constants.NO_DATA);
        }

        mButton =findViewById(R.id.addNext);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity=new Intent(MainActivity.this,Student.class);
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

                if(choice[mPostion].equals("roll no")){

                    Collections.sort(studentArrayList,new SortByRoll());
                    mAdapter.notifyDataSetChanged();
                }
                else if(choice[mPostion].equals("name")){
                    Collections.sort(studentArrayList,(new SortByName()));
                    mAdapter.notifyDataSetChanged();
                }

                Toast.makeText(this, "student added", Toast.LENGTH_SHORT).show();
                if (mtext.getVisibility() == View.VISIBLE) {
                    mtext.setVisibility(View.INVISIBLE);
                }
                mAdapter.notifyItemInserted(position);
            }

        }
        else if(requestCode==2 ){
            if( resultCode==RESULT_OK) {
                /**
                 * here taking data from StudentActivity and setting the data name and roll number on the selected position in the list.
                 */
                Toast.makeText(MainActivity.this,"changed",Toast.LENGTH_LONG).show();
                int posi = Integer.parseInt(data.getStringExtra(Constants.POSITION_STUDENT_DATA));
                String name=data.getStringExtra(Constants.POSITION_STUDENT_NAME);
                String id=data.getStringExtra(Constants.POSITION_STUDENT_ROLL);
                StudentDetails std=studentArrayList.get(posi);
                std.setName(name);
                std.setId(id);
                mAdapter.notifyItemChanged(posi);

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
        Spinner s = menu.findItem(R.id.itemSpinner).getActionView().findViewById(R.id.spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mPostion = position;
                if (choice[position].equals("roll no")) {
                    Toast.makeText(MainActivity.this, "Sorted by roll", Toast.LENGTH_LONG).show();
                    Collections.sort(studentArrayList, new SortByRoll());
                    mAdapter.notifyDataSetChanged();


                } else if (choice[position] == "name") {
                    Toast.makeText(MainActivity.this, "Sorted by name ", Toast.LENGTH_LONG).show();

                    Collections.sort(studentArrayList, new SortByName());
                    mAdapter.notifyDataSetChanged();
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
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        }

        else {
             mToggle =true;
             item.setIcon(R.drawable.ic_grid_off_black_24dp);
             recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        }
        return super.onOptionsItemSelected(item);
    }
}







