package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserProgramFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<ProgramType> programTypes;

    private Spinner spinner;
    private EditText etAddUserId;
    private EditText etAddName;
    private EditText etAddDescription;
    private EditText etAddTiming;
    private Button btnAddNewUserProgram;

    private String programType;
    private int userId;
    private String name;



    public int getUserId() {
        return userId;
    }

    private String description;
    private boolean timing;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddUserProgramFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddUserProgramFragment newInstance(String param1, String param2) {
        AddUserProgramFragment fragment = new AddUserProgramFragment();
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
        // Inflate the layout for this fragment'
        View view = inflater.inflate(R.layout.fragment_add_user_program, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        spinner = view.findViewById(R.id.spinner_program_type);
        spinner.setOnItemSelectedListener(this);

        programTypes = new ArrayList<>(); //todo hente liste med program types

        programTypes.add(new ProgramType("1", "3", "test1", "jjsldfjk", "io"));
        programTypes.add(new ProgramType("1", "3", "test1", "jjsldfjk", "iodddd"));
        programTypes.add(new ProgramType("1", "3", "test1", "sadasadjjsldfjk", "io"));

        //String id, String rid, String name, String description, String backColor

        ArrayAdapter<ProgramType> adapter =
                new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, programTypes);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);




// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        etAddUserId = view.findViewById(R.id.etAddUserId);
        etAddName = view.findViewById(R.id.etAddName);
        etAddDescription = view.findViewById(R.id.etAddDescription);
        etAddTiming = view.findViewById(R.id.etAddTiming);

        btnAddNewUserProgram = view.findViewById(R.id.btnAddUserProgram);

        subscribeToApiResponse();
        subscribeToErrors();

        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){



       //todo programType skal velges fra liste...
        // todo finne userID.....
        // userId = etAddUserId.getText().toString();
        name = etAddName.getText().toString();
        description = etAddDescription.getText().toString();


        timing = true;  //todo må fikses

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dataViewModel.getUser(getContext(), firebaseUser.getUid(), false);

        btnAddNewUserProgram.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
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
                        User user = (User) apiResponse.getResponseObject();
                        if (user != null) {
                            // Dersom response på GET, PUT, POST:
                        userId = user.getUser_id();

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

        ProgramType type = (ProgramType) parent.getItemAtPosition(position);
        Log.d("zozo", type.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
