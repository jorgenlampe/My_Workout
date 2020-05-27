package com.example.myworkout.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class AddUserProgramFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Fragment for å registrere nytt treningsprogram. Kobles til programtype som velges fra en spinner

    private ArrayList<ProgramType> programTypes;

    private Spinner spinner;
    private EditText etAddName;
    private EditText etAddDescription;
    private CheckBox etAddTiming;
    private Button btnAddNewUserProgram;

    private String programType;
    private int userId;
    private String name;
    private String description;
    private boolean timing;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;

    public AddUserProgramFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_user_program, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        spinner = view.findViewById(R.id.spinner_program_type);
        spinner.setOnItemSelectedListener(this);
        programTypes = new ArrayList<>();

        etAddName = view.findViewById(R.id.etAddName);
        etAddDescription = view.findViewById(R.id.etAddDescription);
        etAddTiming = view.findViewById(R.id.brukTiming);

        btnAddNewUserProgram = view.findViewById(R.id.btnAddUserProgram);

        subscribeToApiResponse();
        subscribeToErrors();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getActivity().setTitle(R.string.btnAddUserProgram);

        dataViewModel.getUser(getContext(), firebaseUser.getUid(), true);
        dataViewModel.getProgramTypes(getContext(), true);

        btnAddNewUserProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etAddName.getText().toString();
                description = etAddDescription.getText().toString();
                timing = etAddTiming.isChecked();
                System.out.println("sasdasdsd" + timing);
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
                dataViewModel.postUserProgram(getContext(), programType, firebaseId, name, description, timing, userId);
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
                               //Dersom response på GET, PUT, POST:
                               userId = user.getUser_id();
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
                            //går tilbake til listen dersom programmet ble laget
                            NavHostFragment.findNavController(AddUserProgramFragment.this).navigateUp();
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
