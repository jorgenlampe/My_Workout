package com.example.myworkout.adapters;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.fragments.UserProgramsFragment;

import java.util.ArrayList;

public class UserProgramAdapter extends RecyclerView.Adapter<UserProgramAdapter.MyViewHolder> {

    //Adapter-klasse for å vise alle treningsprogrammene i en RecyclerView

    private OnItemClickListener mListener;
    private ArrayList<UserProgram> userPrograms;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvDescription;
        public ImageView imgRemove;
        public ImageView imgEdit;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserProgramName);
            tvName.setTextSize(20);
            tvDescription = itemView.findViewById(R.id.tvUserProgramDescription);
            imgRemove = itemView.findViewById(R.id.programDelete);
            imgEdit = itemView.findViewById(R.id.programEdit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }

    public UserProgramAdapter(ArrayList<UserProgram> programs) {
        userPrograms = programs;
    }

    @Override
    public UserProgramAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_program_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserProgram currentProgram = userPrograms.get(position);
        holder.tvDescription.setText(currentProgram.getDescription());
        holder.tvName.setText(currentProgram.getName());
    }

    @Override
    public int getItemCount() {
        return userPrograms.size();
    }
}


