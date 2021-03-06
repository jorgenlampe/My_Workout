package com.example.myworkout.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;



public class ExerciseFragment extends Fragment {

    //Fragment for å vise valgt øvelse. Viser bilde ved hjelp av Glide

    private String rid;

    private ImageView image;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private TextView tvName;
    private TextView tvDescription;

    private DataViewModel dataViewModel;

    public ExerciseFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        getActivity().setTitle(R.string.titleExercise);

        tvName = view.findViewById(R.id.tvExerciseName);
        tvDescription = view.findViewById(R.id.tvExerciseDescription);

        image = view.findViewById(R.id.image);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        subscribeToApiResponse();
        subscribeToErrors();
        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState){

        rid = ExerciseFragmentArgs.fromBundle(getArguments()).getExerciseRid();

        dataViewModel.getExercise(getContext(), rid);

    }

    //todo oppdatere nedenfor

    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {

                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                         Exercise exercise = (Exercise)apiResponse.getResponseObject();

                         tvName.setText(exercise.getName());
                         tvName.setTextSize(20);
                         tvDescription.setText(exercise.getDescription());


                         String url = "https://tusk.systems/trainingapp/icons/";

                         StringBuilder sb = new StringBuilder();
                         sb.append(url);
                         sb.append(exercise.getIcon());

                         String imageUrl = sb.toString();

                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round);

                        Glide.with(getContext()).load
                                (imageUrl).apply(options).into(image);


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
