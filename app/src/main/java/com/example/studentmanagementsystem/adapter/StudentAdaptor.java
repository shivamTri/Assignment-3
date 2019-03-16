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
public class StudentAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<StudentDetails> mStudentArrrayList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public void setOnClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public StudentAdaptor(final ArrayList<StudentDetails> name){
        this.mStudentArrrayList =name;
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
        StudentDetails title= mStudentArrrayList.get(i);
        String studentName=title.getName();
        ( (StudentViewHolder) viewHolder).textView.setText(studentName);
    }

    @Override
    /*
     * this method counts the size of arrayList and will iterate acccording to the size.
     */
    public int getItemCount(){
        return mStudentArrrayList.size();
    }


    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public StudentViewHolder(@NonNull final View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.text);
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








