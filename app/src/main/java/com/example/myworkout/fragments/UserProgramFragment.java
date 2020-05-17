package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myworkout.R;


public class UserProgramFragment extends Fragment {

    public UserProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_program, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        TextView tvRid = view.findViewById(R.id.tvRid);
        //Henter Rid
        String Rid = UserProgramFragmentArgs.fromBundle(getArguments()).getUserProgramRid();
        tvRid.setText(Rid);
    }
}
