package com.example.myworkout.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.User;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.entities.UserProgramSession;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSessionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //Fragment for å legge til ny økt

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private ArrayList<UserProgram> userPrograms;

    private EditText etTimeSpent;
    private EditText etDescription;
    private EditText etExtra;
    private Button btnAddSession;
    private Spinner spinner;
    private String user_program_id;

    private String userProgram;

    private EditText etDatePicker;

    private DataViewModel dataViewModel;

    private String mParam1;
    private String mParam2;

    public AddSessionFragment() {

    }


    public static AddSessionFragment newInstance(String param1, String param2) {
        AddSessionFragment fragment = new AddSessionFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_session, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        spinner = view.findViewById(R.id.spinner_userPrograms);
        spinner.setOnItemSelectedListener(this);
        userPrograms = new ArrayList<>();

        etDatePicker = view.findViewById(R.id.etDatePicker);

        etTimeSpent = view.findViewById(R.id.etAddSessionTimeSpent);
        etDescription = view.findViewById(R.id.etAddSessionDescription);
        etExtra = view.findViewById(R.id.etAddSessionExtra);
        btnAddSession = view.findViewById(R.id.btnAddSession);

        subscribeToApiResponse();
        subscribeToErrors();

        //Oppdaterer observer ved backpress
        if (apiResponseObserver != null) {
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser != null) {
            firebaseId = firebaseUser.getUid();
        }

        getActivity().setTitle(R.string.btnAddSession);

        dataViewModel.getUserPrograms(getContext(), firebaseId);

        etDatePicker.setFocusable(false);
        etDatePicker.setClickable(true);

        etDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
    }
});


        btnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float minutes = Float.parseFloat(etTimeSpent.getText().toString());
                float seconds = minutes*60;
                String description = etDescription.getText().toString();
                String extra = etExtra.getText().toString();
                String date = etDatePicker.getText().toString();


               dataViewModel.postUserProgramSession(getContext(), user_program_id, date, seconds, description, extra);
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
                        if (apiResponse.getResponseObject() instanceof UserProgramSession) {

                            NavHostFragment.findNavController(AddSessionFragment.this).navigateUp();



                        } else if (apiResponse.getResponseObject() instanceof ArrayList) {

                            ArrayList list = (ArrayList) apiResponse.getResponseObject();
                            if(list.get(0) instanceof UserProgram) {
                                ArrayList<UserProgram> types = (ArrayList)apiResponse.getResponseObject();
                                userPrograms.addAll(types);
                                ArrayAdapter<UserProgram> adapter =
                                        new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, userPrograms);
                                // Specify the layout to use when the list of choices appears
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // Apply the adapter to the spinner
                                spinner.setAdapter(adapter);
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
        userProgram = userPrograms.get(position).getName();
        user_program_id = userPrograms.get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        userProgram = "0";
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            etDatePicker.setText((year) + "-" + (monthOfYear+1)
                    + "-" + (dayOfMonth));
        }
    };



}

