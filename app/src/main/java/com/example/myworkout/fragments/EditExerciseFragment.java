package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;


public class EditExerciseFragment extends Fragment {

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private EditText editName;
    private EditText editDescription;
    private EditText editIcon;
    private EditText editColor;

    private Button btnEditExercise;

    private String description;
    private String name;
    private String icon;
    private String infoboxColor;
    private String rid;

    private boolean redirect = true;

    private DataViewModel dataViewModel;

    public EditExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_exercise, container, false);


        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        editName = view.findViewById(R.id.editExerciseName);
        editDescription = view.findViewById(R.id.editExerciseDescription);
        editColor = view.findViewById(R.id.editExerciseColor);
        editIcon = view.findViewById(R.id.editExerciseIcon);

        btnEditExercise = view.findViewById(R.id.btnEditExercise);

        subscribeToApiResponse();
        subscribeToErrors();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        rid = EditExerciseFragmentArgs.fromBundle(getArguments()).getExerciseRid();
        dataViewModel.getExercise(getContext(), rid);

        btnEditExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();
                description = editDescription.getText().toString();
                icon = editIcon.getText().toString();
                infoboxColor = editColor.getText().toString();

                //Sjekker at bruker har fyllt ut feltene
                if (name.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill out form before sending!", Toast.LENGTH_LONG).show();
                    return;
                }
                dataViewModel.putExercise(getContext(), rid, name, description, icon, infoboxColor);
            }
        });

    }

    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();

                        if (apiResponse.getResponseObject() instanceof Exercise) {
                            if(redirect) {
                                Exercise exercise = (Exercise)apiResponse.getResponseObject();
                                editName.setText(exercise.getName());
                                editDescription.setText(exercise.getDescription());
                                editColor.setText(exercise.getInfobox_color());
                                editIcon.setText(exercise.getIcon());
                                redirect = false;
                            } else {
                                NavHostFragment.findNavController(EditExerciseFragment.this).navigateUp();
                            }
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
