package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.ExerciseAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;

import java.util.ArrayList;


public class UserProgramFragment extends Fragment {

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private TextView tvExerciseName;
    private TextView tvExerciseDescription;

    private String rid;
    private String user_program_id;

    private Button btnAddExercise;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private ExerciseAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public UserProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_program, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        tvExerciseName = view.findViewById(R.id.tvExerciseName);
        tvExerciseDescription = view.findViewById(R.id.tvExerciseDescription);

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());//bedre ytelse med fast størrelse på layout
        recyclerView.setLayoutManager(layoutManager);


        subscribeToApiResponse();
        subscribeToErrors();


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        dataViewModel.getExercises(getContext());   //todo skal vel være getUserProgramExercises

        rid = UserProgramFragmentArgs.fromBundle(getArguments()).getUserProgramRid();
        Log.d("ridzz", rid);
        user_program_id = UserProgramFragmentArgs.fromBundle(getArguments()).getUserProgramId();

        btnAddExercise = getView().findViewById(R.id.btnAddExercise);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(view).navigate(R.id.actionToAddExerciseFragment);

                UserProgramFragmentDirections.ActionToAddExerciseFragment actionToAddExerciseFragment = UserProgramFragmentDirections.actionToAddExerciseFragment(user_program_id);
                NavHostFragment.findNavController(UserProgramFragment.this).navigate(actionToAddExerciseFragment);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());

        //TextView tvRid = view.findViewById(R.id.tvRid);
        //Henter Rid

        //tvRid.setText(Rid);
    }


    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:

            apiResponseObserver = new Observer<ApiResponse>() {

                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        final ArrayList<Exercise> exercises = (ArrayList) apiResponse.getResponseObject();
                        if (exercises.size() > 0) {
                            // Dersom response på GET, PUT, POST:
                            mAdapter = new ExerciseAdapter(exercises);
                            recyclerView.setAdapter(mAdapter);
                            //bruker onitemclicklistener interface laget i adapter..
                            mAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    UserProgramFragmentDirections.ActionToExerciseFragment actionToExerciseFragment = UserProgramFragmentDirections.actionToExerciseFragment(exercises.get(position).getRid());
                                    NavHostFragment.findNavController(UserProgramFragment.this).navigate(actionToExerciseFragment);
                                }
                            });
                        }
                    }

                }
            };

            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);

        }

    }


    private void subscribeToErrors() {

        if (apiErrorObserver == null) {
            // Observerer endringer i errorMessage:
            apiErrorObserver = new Observer<ApiError>() {
                @Override
                public void onChanged(ApiError apiError) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        if (apiError != null)
                            Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }


}
