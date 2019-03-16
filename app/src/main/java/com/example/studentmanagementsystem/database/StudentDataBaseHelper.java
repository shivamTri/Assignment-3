package com.example.studentmanagementsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;

public class StudentDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="studentManagement.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="student";
    private static final String COL_NAME="Name";
    private static final String COL_ROLL="Roll_number";

    private String createTable="CREATE TABLE "+TABLE_NAME+"("+COL_ROLL+" INTEGER PRIMARY KEY,"+COL_NAME+" TEXT)";


    public StudentDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String createTable="CREATE TABLE"+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"NAME TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addData(String roll_no,String student_name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_ROLL,Integer.parseInt(roll_no));
        contentValues.put(COL_NAME,student_name);

        db.insert(TABLE_NAME,null,contentValues);
        Log.d("inserted","inserted");


    }

    public ArrayList<StudentDetails> getData(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        ArrayList<StudentDetails> arrayOfstore=new ArrayList<>();
        String roll_no;
        String name;
        while(cursor.moveToNext()){
            roll_no=cursor.getString(cursor.getColumnIndex(COL_ROLL));
            name=cursor.getString(cursor.getColumnIndex(COL_NAME));
            StudentDetails studentDetails=new StudentDetails(name,roll_no);
            arrayOfstore.add(studentDetails);
        }

        Log.d("update","Updated");
        return arrayOfstore;
    }

    public void deleteContact(String rollNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ROLL + " = ?", new String[] { String.valueOf(Integer.parseInt(rollNo)) });
        db.close();
    }

    public void update_name(String name,String rollNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,name);
        db.update(TABLE_NAME, cv, COL_ROLL+" = ?", new String[]{rollNo});
        //db.execSQL("UPDATE "+TABLE_NAME+" SET "+COL_NAME+" = "+name+" WHERE "+COL_ROLL+"="+Integer.parseInt(rollNo));
        db.close();
    }
}
