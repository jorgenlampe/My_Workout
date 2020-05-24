package com.example.myworkout.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.UserProgram;

import java.util.ArrayList;

public class ProgramTypeAdapter extends RecyclerView.Adapter<ProgramTypeAdapter.MyViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<ProgramType> programTypes;

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
        public RelativeLayout layout;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvProgramTypeDescription);
            imgRemove = itemView.findViewById(R.id.programTypeDelete);
            imgEdit = itemView.findViewById(R.id.programTypeDelete);
            layout = itemView.findViewById(R.id.program_type_layout);


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

    public ProgramTypeAdapter(ArrayList<ProgramType> types) {
        programTypes = types;
    }

    @Override
    public ProgramTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_type_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProgramType currentProgramType = programTypes.get(position);
        holder.tvDescription.setText(currentProgramType.getDescription());
//todo icon
        //todo holder.layout.setBackgroundColor(Color.parseColor(currentProgramType.getBackColor()));
    }

    @Override
    public int getItemCount() {
        return programTypes.size();
    }
}


