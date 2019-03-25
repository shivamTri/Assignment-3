package com.example.studentmanagementsystem.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.studentmanagementsystem.adapter.ViewPagerAdapter;
import com.example.studentmanagementsystem.model.CommunicationFragmentInterface;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.StudentAddUpdateFragment;
import com.example.studentmanagementsystem.fragment.StudentListFragment;
import com.example.studentmanagementsystem.adapter.StudentAdaptor;
import com.example.studentmanagementsystem.database.StudentDataBaseHelper;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CommunicationFragmentInterface {
    private RecyclerView student_rv;
    private ArrayList<StudentDetails> studentArrayList = new ArrayList<>();
    private Button mButton;
    private TextView mText;
    private StudentAdaptor mAdapter;
    private String select;
    private static final String[] choice={"name", "roll no"};
    private int mPosition;
    private boolean mToggle = true;
    private int posi;
    private static final String DETAIL_VIEW = "View";
    private static final String DETAIL_EDIT = "Edit";
    private static final String DETAIL_DELETE = "Delete";
    private static final String SORT_BY_ROLL_NUMBER = "roll no";
    private static final String[] listItems = {"View", "Edit", "Delete"};
    private static final String SORT_BY_NAME = "name";
    private StudentDataBaseHelper studentDataBaseHelper;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("StudentActivity");
        // setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);


    }

    public void replaceFrag() {
        if(viewPager.getCurrentItem()==0) {
            viewPager.setCurrentItem(1);
        }else
            viewPager.setCurrentItem(0);
    }



    @Override
    public void communication(Bundle bundle) {
        if(viewPager.getCurrentItem()==0) {
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 1;
            StudentAddUpdateFragment studentAddUpdateFragment = (StudentAddUpdateFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentAddUpdateFragment.update(bundle);
        }else if(viewPager.getCurrentItem()==1){
            String tag = "android:switcher:" + R.id.viewpager_id + ":" + 0;
            StudentListFragment studentListFragment = (StudentListFragment) getSupportFragmentManager().findFragmentByTag(tag);
            studentListFragment.update(bundle);
        }
        replaceFrag();
    }


       /* studentDataBaseHelper=new StudentDataBaseHelper(this);
        studentArrayList=studentDataBaseHelper.getData();
        init();
        if(studentArrayList.size()==0){
            mText.setText(getString(R.string.no_data));
        }

        mAdapter.setOnClickListener(new StudentAdaptor.OnItemClickListener() {
            @Override
            public void onItemCLick(final int position) {

                final AlertDialog.Builder alertBuilder=new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle(R.string.alert_title);
                alertBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        select=listItems[which];
                        Toast.makeText(MainActivity.this,select,Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        switch (select){
                            case DETAIL_VIEW:
                                Intent viewIntent=new Intent(MainActivity.this, StudentActivity.class);
                                viewIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                                viewIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,DETAIL_VIEW);
                                startActivity(viewIntent);
                                break;

                            case DETAIL_EDIT:
                                Intent editIntent=new Intent(MainActivity.this, StudentActivity.class);
                                editIntent.putExtra(Constants.STUDENT_DATA,studentArrayList.get(position));
                                editIntent.putExtra(Constants.TYPE_ACTION_FROM_MAIN_ACTIVITY,DETAIL_EDIT);
                                posi=position;
                                startActivityForResult(editIntent,2);
                                break;
                            case DETAIL_DELETE:
                                AlertDialog.Builder deleteDilog=new AlertDialog.Builder(MainActivity.this);
                                deleteDilog.setTitle(R.string.delete_confirmation);
                                deleteDilog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });


                                deleteDilog.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       studentDataBaseHelper.deleteContact(studentArrayList.get(position).getRollNo());
                                        //backgroundTask.execute("delete_data");
                                        studentArrayList.remove(position);
                                       // backgroundTask.execute("delete_data");
                                        Toast.makeText(MainActivity.this,getString(R.string.dialog_delete),Toast.LENGTH_SHORT).show();
                                        mAdapter.notifyDataSetChanged();
                                        if(studentArrayList.size()==0){
                                            mText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                                AlertDialog alertDelete=deleteDilog.create();
                                alertDelete.show();
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
   /* @Override
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
               /* Toast.makeText(MainActivity.this,getString(R.string.student_changed),Toast.LENGTH_LONG).show();
                if (data == null) {
                    throw new AssertionError();
                }
                String name=data.getStringExtra(Constants.POSITION_STUDENT_NAME);
                String id=data.getStringExtra(Constants.POSITION_STUDENT_ROLL);
               // backgroundTask.execute("update_data");
                //studentDataBaseHelper.update_name(name,id);
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
   /* @Override
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
                            Toast.makeText(MainActivity.this, getString(R.string.sorted_by_roll), Toast.LENGTH_LONG).show();
                            Collections.sort(studentArrayList, new SortByRoll());
                            mAdapter.notifyDataSetChanged();
                            break;

                        case SORT_BY_NAME:
                            Toast.makeText(MainActivity.this, getString(R.string.sorted_by_name), Toast.LENGTH_LONG).show();
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
   /* @Override
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
   /* private void init()
    {

       // mButton =findViewById(R.id.addNext);
       // student_rv = findViewById(R.id.studentlist_tv);
        student_rv.setLayoutManager(new LinearLayoutManager(this));
       // student_rv.setAdapter(mAdapter);
       // mText=findViewById(R.id.nodata);
        mAdapter=new StudentAdaptor(studentArrayList);
        student_rv.setAdapter(mAdapter);
      //  backgroundTask= new BackgroundTask(this);*/

    }



