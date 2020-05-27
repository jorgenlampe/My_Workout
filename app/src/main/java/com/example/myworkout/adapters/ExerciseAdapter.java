package com.example.myworkout.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.UserProgram;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

    //Adapter-klasse for å vise alle øvelsene i en RecyclerView

    private OnItemClickListener mListener;
    private ArrayList<Exercise> exercises;

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
        public CardView card;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvExerciseName);
            tvName.setTextSize(20);
            tvDescription = itemView.findViewById(R.id.tvExerciseDescription);
            imgRemove = itemView.findViewById(R.id.exerciseDelete);
            imgEdit = itemView.findViewById(R.id.exerciseEdit);
            card = itemView.findViewById(R.id.exerciseCard);

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

    public ExerciseAdapter(ArrayList<Exercise> exerciseList) {
        exercises = exerciseList;
    }

    @Override
    public ExerciseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);
        holder.tvDescription.setText(currentExercise.getDescription());
        holder.tvName.setText(currentExercise.getName());
        try {
            holder.card.setCardBackgroundColor(Color.parseColor(currentExercise.getInfobox_color()));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}


