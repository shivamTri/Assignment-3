package com.example.studentmanagementsystem.util;

import com.example.studentmanagementsystem.activity.StudentActivity;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * this is util class to validate ids and roll numebrs of students.
 */

public final class ValidUtil {



    /*
     * Regex have been used to validate name.
     */
    public static boolean validateName(String txt) {

        String regx = "[a-zA-Z]+\\.?";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.matches();

    }
    /*
     *Regex have been used to validate roll number.
     */
    public static boolean validateRollNumber(String txt) {

        String regx = "^[1-9][0-9]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(txt);
        return matcher.matches();

    }
    public static boolean isCheckValidId(ArrayList<StudentDetails> checkStudentArrayList,final String roll) {
        for (StudentDetails rollNumber : checkStudentArrayList) {
            if (rollNumber.getRollNo().equals(roll)) {
                return true;
            }
        }
        return false;

    }






}
