package com.example.myworkout.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myworkout.R;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.fragments.UserProgramsFragment;

import java.util.List;

public class UserProgramAdapter extends RecyclerView.Adapter<UserProgramAdapter.MyViewHolder> {

    public UserProgramsFragment userProgramsFragment;
    private List<UserProgram> userPrograms;

    public UserProgramAdapter(List<UserProgram> programs) {
        userPrograms = programs;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {  //implements View.OnClickListener?

        private TextView tv1;
        private TextView tv2;
        public UserProgramsFragment userProgramsFragment;

        public MyViewHolder(UserProgramsFragment fragment) {   //onCheckedChangeListener???

            super(fragment.getView());  //????

            userProgramsFragment = fragment;

            tv1 = itemView.findViewById(R.id.tvUserProgramName);
            tv2 = itemView.findViewById(R.id.tvUserProgramDescription);
            itemView.setOnClickListener((View.OnClickListener) this);   //????
        }

    }

        // Create new views (invoked by the layout manager)
        @Override
        public UserProgramAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
            // create a new view
            UserProgramsFragment view = new UserProgramsFragment();
            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.tv1.setText("heisann");
            holder.userProgramsFragment.setUserProgram(userPrograms.get(position));
            //.setQuestion(questions.get(position), answersChosen[position]);
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return userPrograms.size();
        }
    }


