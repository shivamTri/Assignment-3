package com.example.studentmanagementsystem.constants;

import android.content.Context;
import android.widget.Toast;

public interface Constants  {
    String STUDENT_LIST = "student list";
    String ACTION_TYPE = "type";
    String ACTION_TYPE_ADD = "Add";
    String ACTION_TYPE_VIEW = "View";
    String ACTION_TYPE_EDIT = "Edit";
    String ROLL_NO = "roll no";
    String NAME = "name";
    String STUDENT_DATA = "Student data";
    String[] ITEM_DAILOG={"AsyncTask" , "Service" , "Intent Service"};

    String ADD_TOAST = "Student Added";
    String UPDATE_TOAST = "Updated ";
    int ARRAYLIST_SIZE_ZERO=0;
    int GRIDLAYOUT_SPAN=2;
    String[] itemDialog = {"View", "Edit", "Delete"};
    int VIEW=0;
    int EDIT=1;
    int DELETE=2;
    String []choice = {"name", "roll no"};
    int ASYNC_TASK=0;
    int SERVICE=1;
    int INTENT_SERVICE=2;
    long VIBRATE_MILI_SECOND = 500;
    String FILTER_ACTION_KEY = "key";
    String FILTER_KEY_DELETE = "delete";
    String ACTION_TYPE_DELETE = "delete data";
     int Edit=0;
     int Add=1;
     String DATABASE_NAME="studentManagement.db";
     int DATABASE_VERSION=1;
    String TABLE_NAME="student";
    String COL_NAME="Name";
     String COL_ROLL="Roll_number";


}
