package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myworkout.R;
import com.example.myworkout.adapters.ExerciseAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.entities.UserProgramExercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;

import java.util.ArrayList;


public class AddExerciseFromListDetailFragment extends Fragment {

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private TextView tvExerciseName;
    private TextView tvExerciseDescription;
    private ImageView image;
    private String color;
    private Button btnAddToUserProgram;


    private String exerciseRid;
    private String user_program_id;
    private String app_exercise_id;

    private DataViewModel dataViewModel;


    public AddExerciseFromListDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_exercise_from_list_detail, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        tvExerciseName = view.findViewById(R.id.tvExerciseName);
        tvExerciseDescription = view.findViewById(R.id.tvExerciseDescription);
        image = view.findViewById(R.id.image);
        btnAddToUserProgram = view.findViewById(R.id.btnAddToUserProgram);

        //todo image
        //todo textboxColor

        subscribeToApiResponse();
        subscribeToErrors();


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {


        user_program_id = AddExerciseFromListDetailFragmentArgs.fromBundle(getArguments()).getUserProgramId();
        exerciseRid = AddExerciseFromListDetailFragmentArgs.fromBundle(getArguments()).getExerciseRid();
        app_exercise_id = AddExerciseFromListDetailFragmentArgs.fromBundle(getArguments()).getAppExerciseId();


        dataViewModel.getExercise(getContext(), exerciseRid);

        btnAddToUserProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataViewModel.postUserProgramExercise(getContext(), user_program_id, app_exercise_id);
            }
        });

    }

    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:

            apiResponseObserver = new Observer<ApiResponse>() {

                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        System.out.println(apiResponse.getResponseObject().getClass());
                        if (apiResponse.getResponseObject() instanceof Exercise) {
                            Exercise exercise = (Exercise) apiResponse.getResponseObject();

                            tvExerciseName.setText(exercise.getName());
                            tvExerciseName.setTextSize(40);
                            tvExerciseDescription.setText(exercise.getDescription());
                            tvExerciseDescription.setTextSize(20);

                            //todo finne url til bilde
                            String url = "https://tusk.systems/trainingapp/v2/api.php/app_exercises/icons8-ninja_filled.png";

                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.mipmap.ic_launcher_round)
                                    .error(R.mipmap.ic_launcher_round);

                            Glide.with(getContext()).load
                                    (url).apply(options).into(image);

                            //Navigerer tilbake til listen når objekt er lagt til
                        } else if(apiResponse.getResponseObject() instanceof UserProgramExercise) {
                            NavHostFragment.findNavController(AddExerciseFromListDetailFragment.this).navigateUp();
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
