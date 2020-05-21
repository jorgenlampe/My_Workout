package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.ExerciseAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExerciseFromListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExerciseFromListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String user_program_id;
    private String exerciseRid;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private ExerciseAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExerciseFromListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExerciseFromList.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExerciseFromListFragment newInstance(String param1, String param2) {
        AddExerciseFromListFragment fragment = new AddExerciseFromListFragment();
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
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_add_exercise_from_list, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

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

        dataViewModel.getExercises(getContext());

        user_program_id = AddExerciseFragmentArgs.fromBundle(getArguments()).getUserProgramId();


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

                            mAdapter = new ExerciseAdapter(exercises);
                            recyclerView.setAdapter(mAdapter);
                            //bruker onitemclicklistener interface laget i adapter..
                            mAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    AddExerciseFromListFragmentDirections.ActionToAddExerciseFromListDetailFragment actionToAddExerciseFromListDetailFragment =
    AddExerciseFromListFragmentDirections.actionToAddExerciseFromListDetailFragment(user_program_id,
                                                    exercises.get(position).getRid(), exercises.get(position).getId());
                                    NavHostFragment.findNavController(AddExerciseFromListFragment.this).navigate(actionToAddExerciseFromListDetailFragment);
                                }

                                @Override
                                public void onDeleteClick(int position) {

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
