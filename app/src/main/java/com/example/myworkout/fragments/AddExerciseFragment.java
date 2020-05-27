package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.UserProgramExercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.example.myworkout.helpers.GlideApp;


public class AddExerciseFragment extends Fragment {

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;
    private Observer<String> selectedImageObserver = null;



    private EditText etAddName;
    private EditText etAddDescription;
    private EditText etAddIcon;
    private EditText etAddInfoboxColor;
    private ImageView imgSelected;

    private Button btnAddNewExercise;
    private Button btnAddIcon;

    private String description;
    private String name;
    private String infoboxColor;

    private String user_program_id;
    private String app_exercise_id;
    private String iconUrl;

    private DataViewModel dataViewModel;

    final private String IMAGE_PREFIX = "https://tusk.systems/trainingapp/icons/";

    public AddExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_exercise, container, false);

        getActivity().setTitle(R.string.btnAddExercise);

        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);

        etAddName = view.findViewById(R.id.etAddName);
        etAddDescription = view.findViewById(R.id.etAddDescription);
        etAddInfoboxColor = view.findViewById(R.id.etAddInfoboxColor);
        imgSelected = view.findViewById(R.id.selectedImage);

        btnAddNewExercise = view.findViewById(R.id.btnAddNewExercise);
        btnAddIcon = view.findViewById(R.id.btnAddIcon);

        subscribeToApiResponse();
        subscribeToErrors();
        subscribeToIconSelector();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        user_program_id = AddExerciseFragmentArgs.fromBundle(getArguments()).getUserProgramId();
        iconUrl = AddExerciseFragmentArgs.fromBundle(getArguments()).getIconUrl();
        if(iconUrl != null) {
            GlideApp.with(getContext()).load(IMAGE_PREFIX + iconUrl).into(imgSelected);
            btnAddIcon.setText(R.string.btnSelectDifferentIcon);
        }
        btnAddIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExerciseFragmentDirections.ActionAddExerciseFragmentToImagesFragment toImagesFragment = AddExerciseFragmentDirections.actionAddExerciseFragmentToImagesFragment(user_program_id);
                NavHostFragment.findNavController(AddExerciseFragment.this).navigate(toImagesFragment);
            }
        });

        btnAddNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etAddName.getText().toString();
                description = etAddDescription.getText().toString();
                infoboxColor = etAddInfoboxColor.getText().toString();

                if(iconUrl == null) {
                    Toast.makeText(getContext(), "choose icon!", Toast.LENGTH_LONG).show();
                    return;
                }

                //Sjekker at bruker har fyllt ut feltene
                if (name.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill out form before sending!", Toast.LENGTH_LONG).show();
                    return;
                }

                //Sjekker at farge er i riktig format
                if(!infoboxColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
                    Toast.makeText(getContext(), "Wrong color. Must be in HEX", Toast.LENGTH_LONG).show();
                    return;
                }
                dataViewModel.postExercise(getContext(), name, description, iconUrl, infoboxColor);
            }
        });

    }


    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " ("  + ")", Toast.LENGTH_SHORT).show();

                        if (apiResponse.getResponseObject() instanceof Exercise){
                            Exercise exercise = (Exercise)apiResponse.getResponseObject();

                            app_exercise_id = exercise.getId();

                            dataViewModel.postUserProgramExercise(getContext(), user_program_id, app_exercise_id);
                            NavHostFragment.findNavController(AddExerciseFragment.this).navigateUp();

                        }
                        if (apiResponse.getResponseObject() instanceof UserProgramExercise){
                            UserProgramExercise userProgramExercise = (UserProgramExercise)apiResponse.getResponseObject();

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
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        if (apiError != null)
                            Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }

    private void subscribeToIconSelector() {
        if (selectedImageObserver == null) {
            // Observerer endringer:
            selectedImageObserver = new Observer<String>() {
                @Override
                public void onChanged(String selectedImage) {
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), "image selected successfully", Toast.LENGTH_SHORT).show();
                        System.out.println("selected image " + selectedImage);
                        GlideApp.with(getContext()).load(IMAGE_PREFIX + selectedImage).into(imgSelected);
                        btnAddIcon.setText(R.string.btnSelectDifferentIcon);

                    }
                }
            };
            dataViewModel.getSelectedImage().observe(getViewLifecycleOwner(), selectedImageObserver);
        }
    }

}
