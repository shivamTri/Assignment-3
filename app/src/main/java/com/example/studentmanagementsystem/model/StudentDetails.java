package com.example.studentmanagementsystem.model;

import java.io.Serializable;
/*
  *this is serialized class as we have to send its object to student activity.
  * this class has two parameters studentName and studentRoll
 */

public class StudentDetails implements Serializable {
    private String studentName;
    private String studentRoll;

    public StudentDetails(final String studentName,final String studentRoll ){
        this.studentName=studentName;
        this.studentRoll=studentRoll;
    }

    public String getName(){
        return studentName;
    }
    public String getRollNo(){
        return studentRoll;
    }

    public void setName(String name){
        this.studentName=name;
    }

    public void setId(String id)
    {
        this.studentRoll=id;
    }
}
