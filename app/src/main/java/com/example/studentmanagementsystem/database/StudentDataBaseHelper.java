package com.example.studentmanagementsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;

public class StudentDataBaseHelper extends SQLiteOpenHelper {

    private String createTable="CREATE TABLE "+ Constants.TABLE_NAME+"("+Constants.COL_ROLL+" INTEGER PRIMARY KEY,"+Constants.COL_NAME+" TEXT)";


    public StudentDataBaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /**
     * in this method table is created.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);

    }

    /**
     * in this method table is dropped if already exist and being upgraded from old to new version.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * student data is added in by this method into database.
     * @param student_name
     * @param roll_no
     */
    public void addData(String student_name,String roll_no){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.COL_ROLL,Integer.parseInt(roll_no));
        contentValues.put(Constants.COL_NAME,student_name);

        db.insert(Constants.TABLE_NAME,null,contentValues);
        Log.d("inserted","inserted");


    }

    /**
     * getting the data of students into arraylist.
     * @return ArrayList<>
     */

    public ArrayList<StudentDetails> getData(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+Constants.TABLE_NAME,null);
        ArrayList<StudentDetails> arrayOfstore=new ArrayList<>();
        String roll_no;
        String name;
        while(cursor.moveToNext()){
            roll_no=cursor.getString(cursor.getColumnIndex(Constants.COL_ROLL));
            name=cursor.getString(cursor.getColumnIndex(Constants.COL_NAME));
            StudentDetails studentDetails=new StudentDetails(name,roll_no);
            arrayOfstore.add(studentDetails);
        }

        Log.d("update","Updated");
        return arrayOfstore;
    }

    /**
     * perticular student is deleted by getting roll no a parameter.
     * @param rollNo
     */
    public void deleteContact(String rollNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.COL_ROLL + " = ?", new String[] { String.valueOf(Integer.parseInt(rollNo)) });
        db.close();
    }

    /**
     * details of the student is updated in this method on the basis of their roll no.
     * @param name
     * @param rollNo
     */
    public void update_name(String name,String rollNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.COL_NAME,name);
        db.update(Constants.TABLE_NAME, cv, Constants.COL_ROLL+" = ?", new String[]{rollNo});
        db.close();
    }
}
