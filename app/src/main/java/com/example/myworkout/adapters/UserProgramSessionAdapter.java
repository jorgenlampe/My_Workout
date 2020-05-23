package com.example.myworkout.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.entities.UserProgramSession;

import java.util.ArrayList;

public class UserProgramSessionAdapter extends RecyclerView.Adapter<UserProgramSessionAdapter.MyViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<UserProgramSession> userProgramSessions;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate;
        public TextView tvTimeSpent;
        public TextView tvDescription;
        public TextView tvExtra;
        public ImageView imgRemove;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTimeSpent = itemView.findViewById(R.id.tvTimeSpent);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvExtra = itemView.findViewById(R.id.tvExtra);
            imgRemove = itemView.findViewById(R.id.sessionDelete);

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
        }
    }

    public UserProgramSessionAdapter(ArrayList<UserProgramSession> sessions) {
        userProgramSessions = sessions;
    }

    @Override
    public UserProgramSessionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserProgramSession currentSession = userProgramSessions.get(position);
        holder.tvDate.setText(String.valueOf(currentSession.getDate()));
        holder.tvTimeSpent.setText(String.valueOf(currentSession.getTime_spent()));
        holder.tvTimeSpent.setText(String.valueOf(currentSession.getTime_spent()) + " sekunder");
        holder.tvDescription.setText(currentSession.getDescription());
        holder.tvExtra.setText(currentSession.getExtra_json_data());
    }

    @Override
    public int getItemCount() {
        return userProgramSessions.size();
    }
}


