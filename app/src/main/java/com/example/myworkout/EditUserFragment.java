package com.example.myworkout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;



public class EditUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String username;
    private String phoneNumber;
    private String email;
    private int birthYear;

    private EditText etUsername;
    private EditText etPhoneNumber;
    private EditText etEmail;
    private EditText etBirthYear;

    private Button btnUpdateUser;


    public EditUserFragment() {
        // Required empty public constructor
    }

    //onCreateVieww.... View view = inflater.inflate(R.layout.fragment_user, container, false); return view..

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseId = null;

                if (firebaseUser!=null)
                    firebaseId = firebaseUser.getUid();

                username = etUsername.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();
                email = etEmail.getText().toString();
                birthYear = Integer.parseInt(etBirthYear.getText().toString());
                int ageInMinutes = (Calendar.getInstance().get(Calendar.YEAR) - birthYear) * 365 * 24 * 60;

                dataViewModel.putUser(getContext(), firebaseId, username, phoneNumber, email, ageInMinutes);
                toUserFragment();
            }
        });



    }

    public void toUserFragment(){
        Navigation.findNavController(getView()).navigate(R.id.action_editUserFragment_to_userFragment);
    }

    private void subscribeToApiResponse() {

        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        User user = (User) apiResponse.getResponseObject();
                        if (user != null) {
                            // Dersom response på GET, PUT, POST:

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        etUsername = view.findViewById(R.id.etEditUsername);
        etPhoneNumber = view.findViewById(R.id.etEditPhoneNumber);
        etEmail = view.findViewById(R.id.etEditEmail);
        etBirthYear = view.findViewById(R.id.etEditBirthYear);

        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);


        subscribeToApiResponse();
        subscribeToErrors();

        return view;
    }
}