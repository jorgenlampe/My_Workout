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
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.UserProgramExercise;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProgramTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProgramTypeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private EditText etAddDescription;
    private EditText etAddIcon;
    private EditText etAddBackColor;

    private Button btnAddProgramType;

    private String description;
    private String icon;
    private String backColor;

    private DataViewModel dataViewModel;

    public AddProgramTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProgramTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProgramTypeFragment newInstance(String param1, String param2) {
        AddProgramTypeFragment fragment = new AddProgramTypeFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_program_type, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        etAddDescription = view.findViewById(R.id.etAddDescription);
        etAddIcon = view.findViewById(R.id.etAddIcon);
        etAddBackColor = view.findViewById(R.id.etAddBackColor);

        btnAddProgramType = view.findViewById(R.id.btnAddProgramType);

        subscribeToApiResponse();
        subscribeToErrors();


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        btnAddProgramType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = etAddDescription.getText().toString();
                icon = etAddIcon.getText().toString();
                backColor = etAddBackColor.getText().toString();

                //Sjekker at bruker har fyllt ut feltene
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill out form before sending!", Toast.LENGTH_LONG).show();
                    return;
                }

                //Sjekker at farge er i riktig format
                if (!backColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
                    Toast.makeText(getContext(), "Wrong color. Must be in HEX", Toast.LENGTH_LONG).show();
                    return;
                }
                dataViewModel.postProgramType(getContext(), description, icon, backColor);
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



                            NavHostFragment.findNavController(AddProgramTypeFragment.this).navigateUp();





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



}

