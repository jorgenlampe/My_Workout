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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.User;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class EditUserProgramFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Fragment for å endre treningsprogrammer

    private ArrayList<ProgramType> programTypes;

    private Spinner spinner;
    private EditText editName;
    private EditText editDescription;
    private CheckBox editTiming;
    private Button btnEditUserProgram;

    private String programType;
    private String userId;
    private String name;
    private String description;
    private boolean timing;
    String rid;
    private UserProgram currentUserProgram;
    int userProgramCounter = 0; //Primitiv måte å sjekke om programmet har blitt endret...redirekter tilbake til listen om programmet har blitt endret

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;


    public EditUserProgramFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_user_program, container, false);

        getActivity().setTitle(R.string.btnEditUserProgram);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        spinner = view.findViewById(R.id.spinner_edit_program_type);
        spinner.setOnItemSelectedListener(this);
        programTypes = new ArrayList<>();

        editName = view.findViewById(R.id.editProgramName);
        editDescription = view.findViewById(R.id.editProgramDescription);
        editTiming = view.findViewById(R.id.editTiming);

        btnEditUserProgram = view.findViewById(R.id.btnEditUserProgram);

        subscribeToApiResponse();
        subscribeToErrors();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        rid = EditUserProgramFragmentArgs.fromBundle(getArguments()).getUserProgramRid();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dataViewModel.getUser(getContext(), firebaseUser.getUid(), true);
        dataViewModel.getProgramTypes(getContext(), true);
        dataViewModel.getUserProgram(getContext(), rid);

        btnEditUserProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();
                description = editDescription.getText().toString();
                timing = editTiming.isChecked();
                //Sjekker at bruker har fyllt ut feltene
                if(name.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill out form before sending!", Toast.LENGTH_LONG).show();
                    return;
                }
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseId = null;
                if (firebaseUser!=null) {
                    firebaseId = firebaseUser.getUid();
                }
                dataViewModel.putUserProgram(getContext(), rid, userId, programType, name, description, timing);
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
                        if (apiResponse.getResponseObject() instanceof User) {
                            User user = (User) apiResponse.getResponseObject();
                            if (user != null) {
                                userId = String.valueOf(user.getUser_id());
                            }
                        } else if (apiResponse.getResponseObject() instanceof ArrayList) {
                            ArrayList list = (ArrayList) apiResponse.getResponseObject();
                            if(list.get(0) instanceof ProgramType) {
                                ArrayList<ProgramType> types = (ArrayList)apiResponse.getResponseObject();
                                programTypes.addAll(types);
                                ArrayAdapter<ProgramType> adapter =
                                        new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, programTypes);
                                // Specify the layout to use when the list of choices appears
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // Apply the adapter to the spinner
                                spinner.setAdapter(adapter);
                            }
                        } else if (apiResponse.getResponseObject() instanceof UserProgram) {
                            if (userProgramCounter == 0) {
                                currentUserProgram = (UserProgram) apiResponse.getResponseObject();
                                editName.setText(currentUserProgram.getName());
                                editDescription.setText(currentUserProgram.getDescription());
                                if (currentUserProgram.getUse_timing() == 1) {
                                    editTiming.setChecked(true);
                                }
                                for (int i = 0; i < programTypes.size(); i++) {
                                    if (programTypes.get(i).getId().equals(currentUserProgram.getApp_program_type_id())) {
                                        spinner.setSelection(i);
                                    }
                                }
                                userProgramCounter++;
                            } else {
                                NavHostFragment.findNavController(EditUserProgramFragment.this).navigateUp();
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
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        if (apiError != null)
                            Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        programType = programTypes.get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        programType = "0";
    }
}
