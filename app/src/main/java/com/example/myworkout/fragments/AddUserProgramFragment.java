package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserProgramFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText etAddProgramType;
    private EditText etAddUserId;
    private EditText etAddName;
    private EditText etAddDescription;
    private EditText etAddTiming;

    private String programType;
    private String userId;
    private String name;
    private String description;
    private boolean timing;

    private DataViewModel dataViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddUserProgramFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUserProgramFragment newInstance(String param1, String param2) {
        AddUserProgramFragment fragment = new AddUserProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_add_user_program, container, false);

        etAddProgramType = view.findViewById(R.id.etAddProgramType);
        etAddUserId = view.findViewById(R.id.etAddUserId);
        etAddName = view.findViewById(R.id.etAddName);
        etAddDescription = view.findViewById(R.id.etAddDescription);
        etAddTiming = view.findViewById(R.id.etAddTiming);




        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser!=null) {
            firebaseId = firebaseUser.getUid();
        }

        programType = etAddProgramType.getText().toString();
        userId = etAddUserId.getText().toString();
        name = etAddName.getText().toString();
        description = etAddDescription.getText().toString();

        timing = true;  //todo må fikses


        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        dataViewModel.postUserProgram(getContext(), programType, firebaseId, name, description, timing, userId);

        //todo APIresponse etc....

    }

}
