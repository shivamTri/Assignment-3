package com.example.studentmanagementsystem.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.StudentDetails;

import java.util.ArrayList;

/**
 * this is adapter class  extending viewholder class.
 */
public class StudentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<StudentDetails> mStudentArrayList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public StudentAdapter(final ArrayList<StudentDetails> name){
        this.mStudentArrayList =name;
    }

    @NonNull
    @Override
    /**
     * view holder method will inflate view on recler view.
     */
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_list,viewGroup,false);
        StudentViewHolder studentViewHolder=new StudentViewHolder(view);
        return studentViewHolder;
    }

    @Override
    /*
     *onBindViewHolder binds data and set the title on the recycler view.
     */
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        StudentDetails title= mStudentArrayList.get(i);
        String studentName=title.getName();
        String studentRoll=title.getRollNo();
        ( (StudentViewHolder) viewHolder).tv_name.setText(studentName);
        ( (StudentViewHolder) viewHolder).tv_roll.setText(studentRoll);

    }

    @Override
    /*
     * this method counts the size of arrayList and will iterate acccording to the size.
     */
    public int getItemCount(){
        return mStudentArrayList.size();
    }


    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_roll;

        public StudentViewHolder(@NonNull final View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.text);
            tv_roll=itemView.findViewById(R.id.tv_roll);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemCLick(position);
                        }
                    }
                }
            });

        }
    }
}








