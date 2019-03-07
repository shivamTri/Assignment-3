package com.example.studentmanagementsystem.comparator;

import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.Comparator;
/*
 * in this class students are being sorted based on their roll numbers.
 * this class is implementing compareator class to compare two roll numbers.
 */

public class SortByRoll implements Comparator<StudentDetails> {
    @Override
    public int compare(StudentDetails studentRollNumber1, StudentDetails studentRollNumber2) {
        return (Integer.parseInt(studentRollNumber1.getRollNo()) < Integer.parseInt(studentRollNumber2.getRollNo()) ? -1 :
                (Integer.parseInt(studentRollNumber1.getRollNo()) > Integer.parseInt(studentRollNumber2.getRollNo()) ? 1: 0));
    }
}
