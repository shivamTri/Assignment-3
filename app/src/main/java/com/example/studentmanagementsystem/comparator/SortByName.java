package com.example.studentmanagementsystem.comparator;

import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.Comparator;

public class SortByName implements Comparator<StudentDetails> {

        /**
         * this class is implementing Comparator class to get the names in orders.
         * @return ordered names of the arrayList.
         */

        @Override
        public  int compare(StudentDetails studentName1, StudentDetails studentName2) {
            return studentName1.getName().compareTo(studentName2.getName());

        }
}
