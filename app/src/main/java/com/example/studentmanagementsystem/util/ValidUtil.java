package com.example.studentmanagementsystem.util;

import com.example.studentmanagementsystem.constants.Constants;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * this is util class to validate ids and roll numebrs of students.
 */

public final class ValidUtil {

    /**
     * Regex have been used to validate name.
     * @param sName name of student as parameter
     * @return
     */
    public static boolean validateName(String sName) {


        Pattern pattern = Pattern.compile(Constants.regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sName);
        return matcher.matches();

    }

    /**
     * Regex have been used to validate roll number.
     * @param sRoll is roll no of student.
     * @return boolean
     */
    public static boolean validateRollNumber(String sRoll) {

        Pattern pattern = Pattern.compile(Constants.regxRoll);
        Matcher matcher = pattern.matcher(sRoll);
        return matcher.matches();

    }

    /**
     * validation is done for not having same roll no
     * @param checkStudentArrayList student arraylist.
     * @param roll student roll no to be checked as unique.
     * @return
     */
    public static boolean isCheckValidId(ArrayList<StudentDetails> checkStudentArrayList,final String roll) {
        for (StudentDetails rollNumber : checkStudentArrayList) {
            if (rollNumber.getRollNo().equals(roll)) {
                return true;
            }
        }
        return false;

    }






}
