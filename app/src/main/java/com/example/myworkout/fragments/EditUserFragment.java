package com.example.myworkout.fragments;

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
import android.widget.Toast;

import com.example.myworkout.R;
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

    FirebaseUser firebaseUser;
    String firebaseId = null;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        getActivity().setTitle(R.string.editUser);

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();
                email = etEmail.getText().toString();
                birthYear = Integer.parseInt(etBirthYear.getText().toString());
                System.out.println(birthYear);
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
                            if(user.getBirth_year() == 2020) {
                                etPhoneNumber.setText("");
                            } else {
                                etBirthYear.setText(String.valueOf(user.getBirth_year()));
                            }
                            etEmail.setText(user.getEmail());
                            if(user.getPhone().equals("00000000")) {
                                etPhoneNumber.setText("");
                            } else {
                                etPhoneNumber.setText(user.getPhone());
                            }
                            etUsername.setText(user.getName());
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseId = null;
        if (firebaseUser!=null)
            firebaseId = firebaseUser.getUid();

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        etUsername = view.findViewById(R.id.etEditUsername);
        etPhoneNumber = view.findViewById(R.id.etEditPhoneNumber);
        etEmail = view.findViewById(R.id.etEditEmail);
        etBirthYear = view.findViewById(R.id.etEditBirthYear);

        btnUpdateUser = view.findViewById(R.id.btnUpdateUser);


        subscribeToApiResponse();
        subscribeToErrors();
        dataViewModel.getUser(getActivity(), firebaseId, false);

        return view;
    }
}